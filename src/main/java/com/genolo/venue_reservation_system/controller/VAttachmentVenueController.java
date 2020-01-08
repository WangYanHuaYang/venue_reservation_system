package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.VAttachmentVenue;
import com.genolo.venue_reservation_system.service.VAttachmentVenueService;
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
 * @since 2020-01-08
 */
@Api(tags = {"预约详细信息接口"})
@RestController
@RequestMapping("/v-attachment-venue")
public class VAttachmentVenueController {

    @Autowired
    VAttachmentVenueService baseService;


    /**
     * @Description: 多条件查询 VAttachmentVenue
     * @Param: [v_attachment_venue]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 VAttachmentVenue")
    @RequestMapping(value = "/selectVAttachmentVenues",method = RequestMethod.POST)
        private Msg selectVAttachmentVenues(@RequestBody VAttachmentVenue v_attachment_venue,@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "1")Integer pageSize){
        Page<VAttachmentVenue> page=new Page<VAttachmentVenue>(pageNum,pageSize);
        IPage<VAttachmentVenue> state=baseService.page(page,new QueryWrapper<VAttachmentVenue>().setEntity(v_attachment_venue));
        if (state.getSize()>0){
            return Msg.SUCCESS().add("resultSet",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 VAttachmentVenue
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 VAttachmentVenue")
    @RequestMapping(value = "/selectVAttachmentVenueById",method = RequestMethod.POST)
        private Msg selectVAttachmentVenueById(@RequestParam(value = "id",defaultValue = "no")String id){
    VAttachmentVenue state=baseService.getById(id);
        if (state!=null){
            return Msg.SUCCESS().add("result",state);
        }else{
            return Msg.FAIL();
        }
    }


    /**
     * @Description: 导出 VAttachmentVenue
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 VAttachmentVenue")
    @RequestMapping(value = "/exportVAttachmentVenue",method = RequestMethod.POST)
        private void exportVAttachmentVenue(@RequestBody VAttachmentVenue v_attachment_venue,HttpServletResponse response){
        List<VAttachmentVenue> resultSet=baseService.list(new QueryWrapper<VAttachmentVenue>().setEntity(v_attachment_venue));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet,v_attachment_venue.toString(),"1",VAttachmentVenue.class,LocalDateTime.now().format(df)+".xlsx",response);
    }

}
