package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.FileUtils;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.service.AttachmentService;
import com.genolo.venue_reservation_system.service.FileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 附件表	 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {" 附件表接口"})
@RestController
@RequestMapping("/attachment")
public class AttachmentController {

    @Autowired
    AttachmentService baseService;

    @Autowired
    FileService fileService;

    @Value("${current-project.file-prefix-path}")
    private String filePrefixPath;

    /**
     * @Description: 新增  附件表
     * @Param: [attachment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 Attachment")
    @RequestMapping(value = "/saveAttachment", method = RequestMethod.PUT)
    private Msg saveAttachment(Attachment attachment,
                               @NotBlank(message = "场馆名不能为空") @RequestParam(value = "venueName") String venueName,
                               @NotBlank(message = "文件名不能为空") @RequestParam(value = "fileName") String fileName,
                               @NotBlank(message = "所属学校名不能为空") @RequestParam(value = "schoolName") String schoolName,
                               @RequestParam(value = "file") MultipartFile file) {
        Attachment state = null;
        StringBuilder fileVisitPath = new StringBuilder();
        try {
            state = baseService.save(file, attachment, venueName, schoolName, fileName);
            fileVisitPath.append(filePrefixPath);
            fileVisitPath.append(state.getAttachmentUrl());
            state.setAttachmentUrl(fileVisitPath.toString());
            if (state != null) {
                return Msg.SUCCESS().add("file", state);
            } else {
                return Msg.FAIL();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Msg.CUSTOM_MSG(500, e.getMessage());
        }
    }

    /**
     * @Description: 删除 Attachment (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 Attachment (非逻辑删除)")
    @RequestMapping(value = "/deleteAttachment", method = RequestMethod.DELETE)
    private Msg deleteAttachment(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 Attachment
     * @Param: [attachment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 Attachment")
    @RequestMapping(value = "/updateAttachment", method = RequestMethod.POST)
    private Msg updateAttachment(@RequestBody Attachment attachment) {
        attachment.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.updateById(attachment);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 Attachment
     * @Param: [attachment]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 Attachment")
    @RequestMapping(value = "/selectAttachments", method = RequestMethod.POST)
    private Msg selectAttachments(@RequestBody Attachment attachment, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<Attachment> page = new Page<Attachment>(pageNum, pageSize);
        QueryWrapper<Attachment> wrapper = new QueryWrapper<Attachment>().setEntity(attachment);
        wrapper.orderBy(true, false, "update_time");
        IPage<Attachment> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 Attachment
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 Attachment")
    @RequestMapping(value = "/selectAttachmentById", method = RequestMethod.POST)
    private Msg selectAttachmentById(@RequestParam(value = "id", defaultValue = "no") String id) {
        Attachment state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 获取日志文件
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("获取日志文件")
    @RequestMapping(value = "/getLogDic", method = RequestMethod.POST)
    private Msg getLogDic() {
        return Msg.SUCCESS().add("logFile",fileService.getLogDic());
    }

    /**
     * @Description: 导入 Attachment
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 Attachment")
    @RequestMapping(value = "/importAttachment", method = RequestMethod.POST)
    private Msg importAttachment(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputAttachments(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 Attachment
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 Attachment")
    @RequestMapping(value = "/exportAttachment", method = RequestMethod.POST)
    private void exportAttachment(@RequestBody Attachment attachment, HttpServletResponse response) {
        List<Attachment> resultSet = baseService.list(new QueryWrapper<Attachment>().setEntity(attachment));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, attachment.toString(), "1", Attachment.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
