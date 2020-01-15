package com.genolo.venue_reservation_system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.genolo.venue_reservation_system.model.Appointment;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * <p>
 * 预约表 Mapper 接口
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
public interface AppointmentMapper extends BaseMapper<Appointment> {

    List<Appointment> countNumberOfPersons(@Param(Constants.WRAPPER) Wrapper wrapper);

}
