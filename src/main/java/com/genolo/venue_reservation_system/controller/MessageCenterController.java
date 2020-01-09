package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.model.MessageCenter;
import com.genolo.venue_reservation_system.service.MessageCenterService;
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
 * 消息中心表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {" 消息中心表接口"})
@RestController
@RequestMapping("/message-center")
public class MessageCenterController {

    @Autowired
    MessageCenterService baseService;

    /**
     * @Description: 新增  消息中心表
     * @Param: [message_center]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 MessageCenter")
    @RequestMapping(value = "/saveMessageCenter", method = RequestMethod.PUT)
    private Msg saveMessageCenter(@RequestBody MessageCenter message_center) {
        message_center.setCreateTime(LocalDateTime.now());
        message_center.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.save(message_center);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 MessageCenter (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 MessageCenter (非逻辑删除)")
    @RequestMapping(value = "/deleteMessageCenter", method = RequestMethod.DELETE)
    private Msg deleteMessageCenter(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 MessageCenter
     * @Param: [message_center]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 MessageCenter")
    @RequestMapping(value = "/updateMessageCenter", method = RequestMethod.POST)
    private Msg updateMessageCenter(@RequestBody MessageCenter message_center) {
        boolean state = baseService.updateById(message_center);
        message_center.setUpdateTime(LocalDateTime.now());
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 MessageCenter
     * @Param: [message_center]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 MessageCenter")
    @RequestMapping(value = "/selectMessageCenters", method = RequestMethod.POST)
    private Msg selectMessageCenters(@RequestBody MessageCenter message_center, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<MessageCenter> page = new Page<MessageCenter>(pageNum, pageSize);
        QueryWrapper<MessageCenter> wrapper = new QueryWrapper<MessageCenter>().setEntity(message_center);
        wrapper.orderBy(true, false, "update_time");
        IPage<MessageCenter> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 MessageCenter
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 MessageCenter")
    @RequestMapping(value = "/selectMessageCenterById", method = RequestMethod.POST)
    private Msg selectMessageCenterById(@RequestParam(value = "id", defaultValue = "no") String id) {
        MessageCenter state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 MessageCenter
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 MessageCenter")
    @RequestMapping(value = "/importMessageCenter", method = RequestMethod.POST)
    private Msg importMessageCenter(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputMessageCenters(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 MessageCenter
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 MessageCenter")
    @RequestMapping(value = "/exportMessageCenter", method = RequestMethod.POST)
    private void exportMessageCenter(@RequestBody MessageCenter message_center, HttpServletResponse response) {
        List<MessageCenter> resultSet = baseService.list(new QueryWrapper<MessageCenter>().setEntity(message_center));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, message_center.toString(), "1", MessageCenter.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
