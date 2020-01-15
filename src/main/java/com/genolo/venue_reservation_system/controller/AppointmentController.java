package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Appointment;
import com.genolo.venue_reservation_system.service.AppointmentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 预约表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"预约表接口"})
@RestController
@RequestMapping("/appointment")
public class AppointmentController {

    @Autowired
    AppointmentService baseService;

    /**
     * @Description: 新增 预约表
     * @Param: [appointment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 Appointment")
    @RequestMapping(value = "/saveAppointment", method = RequestMethod.PUT)
    private Msg saveAppointment(@RequestBody Appointment appointment) {
        appointment.setCreateTime(LocalDateTime.now());
        appointment.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.save(appointment);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 Appointment (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 Appointment (非逻辑删除)")
    @RequestMapping(value = "/deleteAppointment", method = RequestMethod.DELETE)
    private Msg deleteAppointment(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 Appointment
     * @Param: [appointment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 Appointment")
    @RequestMapping(value = "/updateAppointment", method = RequestMethod.POST)
    private Msg updateAppointment(@RequestBody Appointment appointment) {
        appointment.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.updateById(appointment);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 Appointment
     * @Param: [appointment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 Appointment")
    @RequestMapping(value = "/selectAppointments", method = RequestMethod.POST)
    private Msg selectAppointments(@RequestBody Appointment appointment, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<Appointment> page = new Page<Appointment>(pageNum, pageSize);
        QueryWrapper<Appointment> wrapper=new QueryWrapper<Appointment>().setEntity(appointment);
        wrapper.orderBy(true,false,"update_time");
        IPage<Appointment> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 获取预约表中的团队或个人
     * @Param: [appointment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("获取预约表中的团队或个人")
    @RequestMapping(value = "/getPersionOrTeam", method = RequestMethod.POST)
    private Msg getPersionOrTeam(@RequestBody Appointment appointment,@RequestParam("isTeam") boolean isTeam, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        try {
            IPage<Appointment> state = baseService.persionOrTeam(new Page<Appointment>(pageNum, pageSize),isTeam, appointment);
            if (state.getSize() > 0) {
                return Msg.SUCCESS().add("resultSet", state);
            } else {
                return Msg.SUCCESS().add("resultSet", "暂无数据");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.CUSTOM_MSG(500,"服务端错误!");
        }
    }

    /**
     * @Description: 根据年月日，学校名称统计预约人数
     * @Param: [isSchool 是否根据学校, isYear 是否根据年, isMonth 是否根据月, isDay 是否根据日, appointment 其他条件]
     * @return: java.util.List<com.genolo.venue_reservation_system.model.Appointment>
     * @Author: WYHY
     * @Date: 2020/1/14
     */
    @ApiOperation("根据年月日，学校名称统计预约人数")
    @RequestMapping(value = "/countNumberOfPersons", method = RequestMethod.POST)
    private Msg countNumberOfPersons(@RequestParam(name = "isSchool") boolean isSchool,
                                     @RequestParam(name = "isYear") boolean isYear,
                                     @RequestParam(name = "isMonth") boolean isMonth,
                                     @RequestParam(name = "isDay") boolean isDay,
                                     @RequestBody Appointment appointment){
        List<Appointment> state = baseService.countNumberOfPersons(isSchool,isYear,isMonth,isDay,appointment);
        if (state.size()>0) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.SUCCESS().add("resultSet", "暂无数据");
        }
    }

    /**
     * @Description: 根据id查询 Appointment
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 Appointment")
    @RequestMapping(value = "/selectAppointmentById", method = RequestMethod.POST)
    private Msg selectAppointmentById(@RequestParam(value = "id", defaultValue = "no") String id) {
        Appointment state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 Appointment
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 Appointment")
    @RequestMapping(value = "/importAppointment", method = RequestMethod.POST)
    private Msg importAppointment(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputAppointments(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 Appointment
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 Appointment")
    @RequestMapping(value = "/exportAppointment", method = RequestMethod.POST)
    private void exportAppointment(@RequestBody Appointment appointment, HttpServletResponse response) {
        List<Appointment> resultSet = baseService.list(new QueryWrapper<Appointment>().setEntity(appointment));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, appointment.toString(), "1", Appointment.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
