package com.genolo.venue_reservation_system.service;

import com.genolo.venue_reservation_system.model.Appointment;
import com.genolo.venue_reservation_system.dao.AppointmentMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 预约表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class AppointmentService extends BaseService<AppointmentMapper, Appointment> {

    /**
    * @Description: 导入 Appointment (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputAppointments(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<Appointment> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),Appointment.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

}
