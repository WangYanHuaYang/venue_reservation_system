package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.model.Venue;
import com.genolo.venue_reservation_system.service.VenueService;
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
 * 场馆表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"场馆表接口"})
@RestController
@RequestMapping("/venue")
public class VenueController {

    @Autowired
    VenueService baseService;

    /**
     * @Description: 新增 场馆表
     * @Param: [venue]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 Venue")
    @RequestMapping(value = "/saveVenue", method = RequestMethod.PUT)
    private Msg saveVenue(@RequestBody Venue venue) {
        venue.setCreateTime(LocalDateTime.now());
        venue.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.save(venue);
        if (state) {
            return Msg.SUCCESS().add("venueId",venue.getId());
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 Venue (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 Venue (非逻辑删除)")
    @RequestMapping(value = "/deleteVenue", method = RequestMethod.DELETE)
    private Msg deleteVenue(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 Venue
     * @Param: [venue]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 Venue")
    @RequestMapping(value = "/updateVenue", method = RequestMethod.POST)
    private Msg updateVenue(@RequestBody Venue venue) {
        venue.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.updateById(venue);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 Venue
     * @Param: [venue]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 Venue")
    @RequestMapping(value = "/selectVenues", method = RequestMethod.POST)
    private Msg selectVenues(@RequestBody Venue venue, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<Venue> page = new Page<Venue>(pageNum, pageSize);
        QueryWrapper<Venue> wrapper = new QueryWrapper<Venue>().setEntity(venue);
        wrapper.orderBy(true, false, "update_time");
        IPage<Venue> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 Venue
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 Venue")
    @RequestMapping(value = "/selectVenueById", method = RequestMethod.POST)
    private Msg selectVenueById(@RequestParam(value = "id", defaultValue = "no") String id) {
        Venue state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 Venue
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 Venue")
    @RequestMapping(value = "/importVenue", method = RequestMethod.POST)
    private Msg importVenue(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputVenues(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 Venue
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 Venue")
    @RequestMapping(value = "/exportVenue", method = RequestMethod.POST)
    private void exportVenue(@RequestBody Venue venue, HttpServletResponse response) {
        List<Venue> resultSet = baseService.list(new QueryWrapper<Venue>().setEntity(venue));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, venue.toString(), "1", Venue.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
