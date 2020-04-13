package com.genolo.venue_reservation_system.dao;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.genolo.venue_reservation_system.model.Appointment;
import com.genolo.venue_reservation_system.model.VVenueInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * VIEW Mapper 接口
 * </p>
 *
 * @author wyhy
 * @since 2020-01-14
 */
public interface VVenueInfoMapper extends BaseMapper<VVenueInfo> {

    List<VVenueInfo> countAreaVenue(@Param(Constants.WRAPPER) Wrapper<VVenueInfo> queryWrapper);

}
