package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.genolo.venue_reservation_system.model.Appointment;
import com.genolo.venue_reservation_system.dao.AppointmentMapper;
import com.genolo.venue_reservation_system.model.News;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
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

    /**
     * @Description: 获取预约表中的团队或个人
     * @Param: [teamType 是否是团队,appointment 条件查询对象]
     * @return: java.util.List<com.genolo.venue_reservation_system.model.Appointment>
     * @Author: WYHY
     * @Date: 2020/1/14
     */
    public IPage<Appointment> persionOrTeam(Page<Appointment> page,boolean teamType, Appointment appointment) throws Exception{
        QueryWrapper<Appointment> wrapper=new QueryWrapper<Appointment>().setEntity(appointment);
        if (teamType){
            wrapper.isNotNull("reserve1");
            wrapper.groupBy("reserve1");
        }else {
            wrapper.isNotNull("reserve2");
            wrapper.groupBy("reserve2");
        }
        wrapper.orderByAsc("update_time");
        IPage<Appointment> result=getBaseMapper().selectPage(page,wrapper);
        return  result;
    }

    @Override
    public <E extends IPage<Appointment>> E page(E page, Wrapper<Appointment> queryWrapper) {
        QueryWrapper<Appointment> wrapper=(QueryWrapper<Appointment>) queryWrapper;
        Appointment appointment=queryWrapper.getEntity();
        if (appointment.getStime()!=null){
            wrapper.ge("start_time",appointment.getStime());
        }
        if (appointment.getEtime()!=null){
            wrapper.le("start_time",appointment.getEtime());
        }
        E p=super.page(page, wrapper);
        List<Appointment> ps=p.getRecords();
        for (int i=0;i<ps.size();i++){
            if (ps.get(i).getEffectiveState()==1){
                if (ps.get(i).getStartTime().isBefore(LocalDateTime.now())){
                    ps.get(i).setEffectiveState(3);
                }else if (ps.get(i).getStartTime().isAfter(LocalDateTime.now())){
                    ps.get(i).setEffectiveState(2);
                }
                if (ps.get(i).getEndTime().isBefore(LocalDateTime.now())){
                    ps.get(i).setEffectiveState(4);
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
    public List<Appointment> countNumberOfPersons(boolean isSchool,boolean isYear,boolean isMonth,boolean isDay,Appointment appointment){
        QueryWrapper<Appointment> wrapper=new QueryWrapper<Appointment>().setEntity(appointment);
        if (appointment.getEffectiveState()==null){
            wrapper.eq("effective_state",0);
        }
        if (appointment.getAuditStatus()==null){
            wrapper.eq("audit_status",2);
        }
        if(isSchool){
            wrapper.groupBy("venue_school");
        }
        if(isYear){
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y')");
        }else if(isMonth){
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m')");
        }else if(isDay){
            wrapper.groupBy("DATE_FORMAT(start_time,'%Y-%m-%d')");
        }
        List<Appointment> result=getBaseMapper().countNumberOfPersons(wrapper);
        return result;
    }
}
