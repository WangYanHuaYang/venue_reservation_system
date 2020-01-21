package com.genolo.venue_reservation_system.config;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.genolo.venue_reservation_system.dao.AppointmentMapper;
import com.genolo.venue_reservation_system.model.Appointment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @program: venue_reservation_system
 * @description: 静态定时任务配置类
 * @author: WYHY
 * @create: 2020-01-16 09:19
 **/
@Configuration
@Slf4j
@EnableScheduling
public class StaticScheduleTaskConfig {

    @Autowired
    @SuppressWarnings("all")
    AppointmentMapper appointmentMapper;

    StringBuilder logAppointment=new StringBuilder();

    @Scheduled(cron = "0 0 0 * * ? ")
    private void cleanAppointment(){
        log.info("定时清理过期预约开始》》》》");
        QueryWrapper<Appointment> wrapper=new QueryWrapper<Appointment>();
        wrapper.eq("effective_state",1);
        wrapper.le("start_time",LocalDateTime.now());
        List<Appointment> appointments=appointmentMapper.selectList(wrapper);
        for (Appointment appointment:appointments){
            logAppointment.delete(0,logAppointment.length());
            appointment.setEffectiveState(5);
            logAppointment.append("被清理的过期预约：-> ");
            logAppointment.append(appointment.toString());
            log.info(logAppointment.toString());
            appointmentMapper.updateById(appointment);
        }
        log.info("过期预约清理结束《《《《");
    }
}
