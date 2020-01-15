package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.genolo.venue_reservation_system.model.News;
import com.genolo.venue_reservation_system.dao.NewsMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 新闻公告表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class NewsService extends BaseService<NewsMapper, News> {

    /**
    * @Description: 导入 News (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputNewss(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<News> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),News.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public <E extends IPage<News>> E page(E page, Wrapper<News> queryWrapper) {
        QueryWrapper<News> wrapper=(QueryWrapper<News>) queryWrapper;
        News news=queryWrapper.getEntity();
        if (news.getStime()!=null){
            wrapper.ge("release_time",news.getStime());
        }
        if (news.getEtime()!=null){
            wrapper.le("release_time",news.getEtime());
        }
        E p=super.page(page, wrapper);
        return p;
    }
}
