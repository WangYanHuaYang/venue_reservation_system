package com.genolo.venue_reservation_system.service;

import com.genolo.venue_reservation_system.model.TeamUser;
import com.genolo.venue_reservation_system.dao.TeamUserMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 团队人员表 服务类
 * @author wyhy
 * @since 2020-01-14
 */
@Service
public class TeamUserService extends BaseService<TeamUserMapper, TeamUser> {

    /**
    * @Description: 导入 TeamUser (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-14
    */
    public boolean imputTeamUsers(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<TeamUser> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),TeamUser.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

}
