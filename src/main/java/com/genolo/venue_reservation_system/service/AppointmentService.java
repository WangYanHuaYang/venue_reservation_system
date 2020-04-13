package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genolo.venue_reservation_system.dao.VAttachmentVenueMapper;
import com.genolo.venue_reservation_system.exceptions.CustomException;
import com.genolo.venue_reservation_system.model.Appointment;
import com.genolo.venue_reservation_system.dao.AppointmentMapper;
import com.genolo.venue_reservation_system.model.News;
import com.genolo.venue_reservation_system.model.VAttachmentVenue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 预约表 服务类
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class AppointmentService extends BaseService<AppointmentMapper, Appointment> {

    @Autowired
    VAttachmentVenueMapper attachmentVenueMapper;

    /**
     * @Description: 导入 Appointment (存在则刷新，不存在则新增)
     * @Param: [file]
     * @return: void
     * @Author: wyhy
     * @Date: 2020-01-03
     */
    public boolean imputAppointments(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state = false;
        try {
            List<Appointment> resultSet = ExcelImportUtil.importExcel(file.getInputStream(), Appointment.class, params);
            state = saveOrUpdateBatch(resultSet, resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    /**
     * @Description: 获取预约表中的团队或个人
     * @Param: [teamType 是否是团队,appointment 条件查询对象]
     * @return: java.util.List<com.genolo.venue_reservation_system.model.Appointment>
     * @Author: WYHY
     * @Date: 2020/1/14
     */
    public IPage<Appointment> persionOrTeam(Page<Appointment> page, Boolean teamType, Appointment appointment) throws Exception {
        QueryWrapper<Appointment> wrapper = new QueryWrapper<Appointment>().setEntity(appointment);
        if (teamType == null) {
            wrapper.groupBy("reserve1,reserve2");
        } else if (teamType) {
            wrapper.isNotNull("reserve1");
            wrapper.groupBy("reserve1");
        } else {
            wrapper.isNotNull("reserve2");
            wrapper.groupBy("reserve2");
        }
        wrapper.orderByAsc("update_time");
        IPage<Appointment> result = getBaseMapper().selectPage(page, wrapper);
        return result;
    }

    @Override
    public <E extends IPage<Appointment>> E page(E page, Wrapper<Appointment> queryWrapper) {
        QueryWrapper<Appointment> wrapper = (QueryWrapper<Appointment>) queryWrapper;
        Appointment appointment = queryWrapper.getEntity();
        if (appointment.getStime() != null) {
            wrapper.ge("start_time", appointment.getStime());
        }
        if (appointment.getEtime() != null) {
            wrapper.le("start_time", appointment.getEtime());
        }
        wrapper.orderByAsc("update_time");
        E p = super.page(page, wrapper);
        List<Appointment> ps = p.getRecords();
        for (int i = 0; i < ps.size(); i++) {
            if (ps.get(i).getEffectiveState() != null && ps.get(i).getEffectiveState() == 1) {
                if (ps.get(i).getStartTime() != null && ps.get(i).getEndTime() != null) {
                    if (ps.get(i).getStartTime().isBefore(LocalDateTime.now())) {
                        ps.get(i).setEffectiveState(3);
                    } else if (ps.get(i).getStartTime().isAfter(LocalDateTime.now())) {
                        ps.get(i).setEffectiveState(2);
                    }
                    if (ps.get(i).getEndTime().isBefore(LocalDateTime.now())) {
                        ps.get(i).setEffectiveState(4);
                    }
                }
            }
        }
        return p;
    }

    /**
     * @Description: 根据年月日，学校名称统计预约人数
     * @Param: [isSchool 是否根据学校, isYear 是否根据年, isMonth 是否根据月, isDay 是否根据日, appointment 其他条件]
     * @return: java.util.List<com.genolo.venue_reservation_system.model.Appointment>
     * @Author: WYHY
     * @Date: 2020/1/14
     */
    public List<Appointment> countNumberOfPersons(boolean isSchool, boolean isVenue, boolean isYear, boolean isMonth, boolean isDay, boolean isHotTime, Appointment appointment) {
        if (appointment.getEffectiveState() == null) {
            appointment.setEffectiveState(0);
        }
        if (appointment.getAuditStatus() == null) {
            appointment.setAuditStatus(2);
        }
        QueryWrapper<Appointment> wrapper = new QueryWrapper<Appointment>().setEntity(appointment);
        System.out.println("11111   " + wrapper.getSqlSegment());
        if (isSchool) {
            wrapper.groupBy("venue_school");
        }
        if (isVenue) {
            wrapper.groupBy("venue_name");
        }
        if (isYear) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y')");
        } else if (isMonth) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m')");
        } else if (isDay) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m-%d')");
        }
        if (isHotTime) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%H')");
        }
        wrapper.orderByDesc("number_of_persons");
        List<Appointment> result = getBaseMapper().countNumberOfPersons(wrapper);
        return result;
    }

    /**
     * @Description: 根据年月日，学校名称统计审核通过率
     * @Param: [isSchool 是否根据学校, isYear 是否根据年, isMonth 是否根据月, isDay 是否根据日, appointment 其他条件]
     * @return: java.util.List<com.genolo.venue_reservation_system.model.Appointment>
     * @Author: WYHY
     * @Date: 2020/1/14
     */
    public List<Appointment> countAuditStatus(boolean isSchool, boolean isVenue, boolean isYear, boolean isMonth, boolean isDay, Appointment appointment) {
        if (appointment.getEffectiveState() == null) {
            appointment.setEffectiveState(0);
        }
        QueryWrapper<Appointment> wrapper = new QueryWrapper<Appointment>().setEntity(appointment);
        System.out.println("11111   " + wrapper.getSqlSegment());
        if (isSchool) {
            wrapper.groupBy("venue_school");
        }
        if (isVenue) {
            wrapper.groupBy("venue_name");
        }
        if (isYear) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y')");
        } else if (isMonth) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m')");
        } else if (isDay) {
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m-%d')");
        }
        List<Appointment> result = getBaseMapper().countAuditStatus(wrapper);
        return result;
    }

    @Override
    public boolean save(Appointment entity) throws CustomException {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setDelState(1);
        entity.setEffectiveState(1);
        entity.setAuditStatus(1);
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<Appointment>();
        appointmentQueryWrapper.eq("venue_id", entity.getVenueId());
        appointmentQueryWrapper.and(w -> w.between("start_time", entity.getStartTime(), entity.getEndTime())
                .or()
                .between("end_time", entity.getStartTime(), entity.getEndTime()));
        appointmentQueryWrapper.and(w -> w.eq("reserve1",entity.getReserve1())
        .or()
        .eq("reserve2",entity.getReserve2()));
        appointmentQueryWrapper.and(w -> w.ne("audit_status",2).or().ne("audit_status",3));
//        wrapper.between("start_time",entity.getStartTime(),entity.getEndTime());
//        wrapper.between("end_time",entity.getStartTime(),entity.getEndTime());
        List<Appointment> appointments=getBaseMapper().selectList(appointmentQueryWrapper);
        if (appointments == null||appointments.size()<=0){
            QueryWrapper<VAttachmentVenue> wrapper = new QueryWrapper<VAttachmentVenue>();
            wrapper.eq("id", entity.getVenueId());
            wrapper.and(w -> w.between("start_time", entity.getStartTime(), entity.getEndTime())
                    .or()
                    .between("end_time", entity.getStartTime(), entity.getEndTime()));
            List<VAttachmentVenue> attachmentVenues = attachmentVenueMapper.selectList(wrapper);
            if (attachmentVenues != null && attachmentVenues.size() > 0) {
                int nowNumber = entity.getNumberOfPersons();
                for (VAttachmentVenue v : attachmentVenues) {
                    if (v.getNowNumber() != null) {
                        nowNumber += v.getNowNumber();
                    }
                }
                if (attachmentVenues.get(0).getNumberOfPersons() > nowNumber) {
                    return super.save(entity);
                } else {
                    StringBuilder message = new StringBuilder();
                    int surplus = attachmentVenues.get(0).getNumberOfPersons() + entity.getNumberOfPersons() - nowNumber;
                    message.append("当前时段预约人数还剩余 【");
                    message.append(surplus);
                    message.append("】 人！");
                    throw new CustomException(500, message.toString());
                }
            } else {
                return super.save(entity);
            }
        }else {
            throw new CustomException(500, "请不要重复预约！");
        }
    }

    @Override
    public boolean updateById(Appointment entity) {
        QueryWrapper<Appointment> appointmentQueryWrapper = new QueryWrapper<Appointment>();
        appointmentQueryWrapper.eq("venue_id", entity.getVenueId());
        appointmentQueryWrapper.and(w -> w.between("start_time", entity.getStartTime(), entity.getEndTime())
                .or()
                .between("end_time", entity.getStartTime(), entity.getEndTime()));
        appointmentQueryWrapper.and(w -> w.eq("reserve1",entity.getReserve1())
                .or()
                .eq("reserve2",entity.getReserve2()));
        appointmentQueryWrapper.and(w -> w.ne("audit_status",2).or().ne("audit_status",3));
//        wrapper.between("start_time",entity.getStartTime(),entity.getEndTime());
//        wrapper.between("end_time",entity.getStartTime(),entity.getEndTime());
        List<Appointment> appointments=getBaseMapper().selectList(appointmentQueryWrapper);
        if (appointments == null||appointments.size()<=0) {
            QueryWrapper<VAttachmentVenue> wrapper = new QueryWrapper<VAttachmentVenue>();
            wrapper.eq("id", entity.getVenueId());
            wrapper.and(w -> w.between("start_time", entity.getStartTime(), entity.getEndTime())
                    .or()
                    .between("end_time", entity.getStartTime(), entity.getEndTime()));
            List<VAttachmentVenue> attachmentVenues = attachmentVenueMapper.selectList(wrapper);
            if (attachmentVenues != null && attachmentVenues.size() > 0) {
                int nowNumber = entity.getNumberOfPersons();
                for (VAttachmentVenue v : attachmentVenues) {
                    if (v.getNowNumber() != null) {
                        nowNumber += v.getNowNumber();
                    }
                }
                if (attachmentVenues.get(0).getNumberOfPersons() > nowNumber) {
                    return super.save(entity);
                } else {
                    StringBuilder message = new StringBuilder();
                    int surplus = attachmentVenues.get(0).getNumberOfPersons() + entity.getNumberOfPersons() - nowNumber;
                    message.append("当前时段预约人数还剩余 【");
                    message.append(surplus);
                    message.append("】 人！");
                    throw new CustomException(500, message.toString());
                }
            } else {
                return super.updateById(entity);
            }
        }else {
            throw new CustomException(500, "已存在相同预约，请不要重复审核！");
        }
    }
}
