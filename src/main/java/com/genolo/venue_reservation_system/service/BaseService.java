package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.enums.SqlMethod;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.TableInfo;
import com.baomidou.mybatisplus.core.toolkit.*;
import com.baomidou.mybatisplus.core.toolkit.sql.SqlHelper;
import org.apache.ibatis.binding.MapperMethod;
import org.apache.ibatis.session.SqlSession;
import org.mybatis.spring.SqlSessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Service 继承类 自定义（ 泛型：M 是 mapper 对象，T 是实体 ， PK 是主键泛型 ）
 *
 * @author WYHY from (hubin)
 * @since 2018-06-23
 */
public class BaseService<M extends BaseMapper<T>, T> {

    @Autowired
    protected M baseMapper;

    /**
     * 判断数据库操作是否成功
     *
     * @param result 数据库操作返回影响条数
     * @return boolean
     */
    protected boolean retBool(Integer result) {
        return SqlHelper.retBool(result);
    }

    protected Class<T> currentModelClass() {
        return ReflectionKit.getSuperClassGenericType(this.getClass(), 1);
    }

    /**
     * 批量操作 SqlSession
     */
    protected SqlSession sqlSessionBatch() {
        return SqlHelper.sqlSessionBatch(currentModelClass());
    }

    /**
     * 释放sqlSession
     *
     * @param sqlSession session
     */
    protected void closeSqlSession(SqlSession sqlSession) {
        SqlSessionUtils.closeSqlSession(sqlSession, GlobalConfigUtils.currentSessionFactory(currentModelClass()));
    }

    /**
     * 获取SqlStatement
     *
     * @param sqlMethod
     * @return
     */
    protected String sqlStatement(SqlMethod sqlMethod) {
        return SqlHelper.table(currentModelClass()).getSqlStatement(sqlMethod.getMethod());
    }

    public boolean save(T entity) {
        return retBool(baseMapper.insert(entity));
    }

    /**
     * 批量插入
     *
     * @param entityList
     * @param batchSize
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean saveBatch(Collection<T> entityList, int batchSize) {
        int i = 0;
        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
        try(SqlSession batchSqlSession = sqlSessionBatch()) {
            for (T anEntityList : entityList) {
                batchSqlSession.insert(sqlStatement, anEntityList);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    /**
     * TableId 注解存在更新记录，否插入一条记录
     *
     * @param entity 实体对象
     * @return boolean
     */
    public boolean saveOrUpdate(T entity) {
        if (null != entity) {
            Class<?> cls = entity.getClass();
            TableInfo tableInfo = TableInfoHelper.getTableInfo(cls);
            if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                Object idVal = ReflectionKit.getMethodValue(cls, entity, tableInfo.getKeyProperty());
                if (StringUtils.checkValNull(idVal)) {
                    return save(entity);
                } else {
                    /*
                     * 更新成功直接返回，失败执行插入逻辑
                     */
                    return updateById(entity) || save(entity);
                }
            } else {
                throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
            }
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<T> entityList) {
        return saveOrUpdateBatch(entityList, 30);
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean saveOrUpdateBatch(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        Class<?> cls = null;
        TableInfo tableInfo = null;
        int i = 0;
        try(SqlSession batchSqlSession = sqlSessionBatch()){
            for (T anEntityList : entityList) {
                if (i == 0) {
                    cls = anEntityList.getClass();
                    tableInfo = TableInfoHelper.getTableInfo(cls);
                }
                if (null != tableInfo && StringUtils.isNotEmpty(tableInfo.getKeyProperty())) {
                    Object idVal = ReflectionKit.getMethodValue(cls, anEntityList, tableInfo.getKeyProperty());
                    if (StringUtils.checkValNull(idVal)) {
                        String sqlStatement = sqlStatement(SqlMethod.INSERT_ONE);
                        batchSqlSession.insert(sqlStatement, anEntityList);
                    } else {
                        String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
                        MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                        param.put(Constants.ENTITY, anEntityList);
                        batchSqlSession.update(sqlStatement, param);
                    }
                    if (i >= 1 && i % batchSize == 0) {
                        batchSqlSession.flushStatements();
                    }
                    i++;
                } else {
                    throw ExceptionUtils.mpe("Error:  Can not execute. Could not find @TableId.");
                }
            }
        }
        return true;
    }

    public boolean removeById(Serializable id) {
        int s=baseMapper.deleteById(id);
        return SqlHelper.delBool(s==0?-1:s);
    }

    public boolean removeByMap(Map<String, Object> columnMap) {
        if (ObjectUtils.isEmpty(columnMap)) {
            throw ExceptionUtils.mpe("removeByMap columnMap is empty.");
        }
        int s=baseMapper.deleteByMap(columnMap);
        return SqlHelper.delBool(s==0?-1:s);
    }

    public boolean remove(Wrapper<T> wrapper) {
        int s=baseMapper.delete(wrapper);
        return SqlHelper.delBool(s==0?-1:s);
    }

    public boolean removeByIds(Collection<? extends Serializable> idList) {
        int s=baseMapper.deleteBatchIds(idList);
        return SqlHelper.delBool(s==0?-1:s);
    }

    public boolean updateById(T entity) {
        return retBool(baseMapper.updateById(entity));
    }

    public boolean update(T entity, Wrapper<T> updateWrapper) {
        return retBool(baseMapper.update(entity, updateWrapper));
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updateBatchById(Collection<T> entityList, int batchSize) {
        if (CollectionUtils.isEmpty(entityList)) {
            throw new IllegalArgumentException("Error: entityList must not be empty");
        }
        int i = 0;
        String sqlStatement = sqlStatement(SqlMethod.UPDATE_BY_ID);
        try(SqlSession batchSqlSession = sqlSessionBatch()) {
            for (T anEntityList : entityList) {
                MapperMethod.ParamMap<T> param = new MapperMethod.ParamMap<>();
                param.put(Constants.ENTITY, anEntityList);
                batchSqlSession.update(sqlStatement, param);
                if (i >= 1 && i % batchSize == 0) {
                    batchSqlSession.flushStatements();
                }
                i++;
            }
            batchSqlSession.flushStatements();
        }
        return true;
    }

    public T getById(Serializable id) {
        return baseMapper.selectById(id);
    }

    public Collection<T> listByIds(Collection<? extends Serializable> idList) {
        return baseMapper.selectBatchIds(idList);
    }

    public Collection<T> listByMap(Map<String, Object> columnMap) {
        return baseMapper.selectByMap(columnMap);
    }

    public T getOne(Wrapper<T> queryWrapper, boolean throwEx) {
        if (throwEx) {
            return baseMapper.selectOne(queryWrapper);
        }
        return SqlHelper.getObject(baseMapper.selectList(queryWrapper));
    }

    public Map<String, Object> getMap(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(baseMapper.selectMaps(queryWrapper));
    }

    public Object getObj(Wrapper<T> queryWrapper) {
        return SqlHelper.getObject(baseMapper.selectObjs(queryWrapper));
    }

    public int count(Wrapper<T> queryWrapper) {
        return SqlHelper.retCount(baseMapper.selectCount(queryWrapper));
    }

    public List<T> list(Wrapper<T> queryWrapper) {
        return baseMapper.selectList(queryWrapper);
    }

    public IPage<T> page(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectPage(page, queryWrapper);
    }

    public List<Map<String, Object>> listMaps(Wrapper<T> queryWrapper) {
        return baseMapper.selectMaps(queryWrapper);
    }

    public List<Object> listObjs(Wrapper<T> queryWrapper) {
        return baseMapper.selectObjs(queryWrapper).stream().filter(Objects::nonNull).collect(Collectors.toList());
    }

    public IPage<Map<String, Object>> pageMaps(IPage<T> page, Wrapper<T> queryWrapper) {
        return baseMapper.selectMapsPage(page, queryWrapper);
    }

}