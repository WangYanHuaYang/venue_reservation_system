package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.VVenueInfo;
import com.genolo.venue_reservation_system.service.VVenueInfoService;
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
 * VIEW 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-14
 */
@Api(tags = {"场馆信息接口"})
@RestController
@RequestMapping("/v-venue-info")
public class VVenueInfoController {

    @Autowired
    VVenueInfoService baseService;

    /**
     * @Description: 多条件查询 VVenueInfo
     * @Param: [v_venue_info]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 VVenueInfo")
    @RequestMapping(value = "/selectVVenueInfos", method = RequestMethod.POST)
    private Msg selectVVenueInfos(@RequestBody VVenueInfo v_venue_info, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                                  @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<VVenueInfo> page = new Page<VVenueInfo>(pageNum, pageSize);
        IPage<VVenueInfo> state = baseService.page(page, new QueryWrapper<VVenueInfo>().setEntity(v_venue_info));
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 VVenueInfo
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 VVenueInfo")
    @RequestMapping(value = "/selectVVenueInfoById", method = RequestMethod.GET)
    private Msg selectVVenueInfoById(@RequestParam(value = "id", defaultValue = "no") String id) {
        VVenueInfo state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据区域统计 VVenueInfo
     * @Param: [vVenueInfo]
     * @Author: wyhy
     * @Date: 2020/02/20
     */
    @ApiOperation("根据区域统计 VVenueInfo")
    @RequestMapping(value = "/countAreaVenue", method = RequestMethod.POST)
    private Msg countAreaVenue(@RequestBody VVenueInfo v_venue_info,
                               @RequestParam(name = "isProvince",required = false) boolean isProvince,
                               @RequestParam(name = "isCity",required = false) boolean isCity,
                               @RequestParam(name = "isDistrict",required = false) boolean isDistrict) {
        List<VVenueInfo> state = baseService.countAreaVenue(v_venue_info,isProvince,isCity,isDistrict);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 VVenueInfo
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 VVenueInfo")
    @RequestMapping(value = "/exportVVenueInfo", method = RequestMethod.GET)
    private void exportVVenueInfo(@RequestBody VVenueInfo v_venue_info, HttpServletResponse response) {
        List<VVenueInfo> resultSet = baseService.list(new QueryWrapper<VVenueInfo>().setEntity(v_venue_info));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, v_venue_info.toString(), "1", VVenueInfo.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
