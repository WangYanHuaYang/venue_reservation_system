package com.genolo.venue_reservation_system.service;

import com.genolo.venue_reservation_system.model.SysRole;
import com.genolo.venue_reservation_system.dao.SysRoleMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 角色表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class SysRoleService extends BaseService<SysRoleMapper, SysRole> {

    /**
    * @Description: 导入 SysRole (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputSysRoles(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<SysRole> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),SysRole.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

}
