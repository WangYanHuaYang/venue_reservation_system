package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.SysPermission;
import com.genolo.venue_reservation_system.service.SysPermissionService;
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
 * 权限表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"权限表接口"})
@RestController
@RequestMapping("/sys-permission")
public class SysPermissionController {

    @Autowired
    SysPermissionService baseService;

    /**
     * @Description: 新增 权限表
     * @Param: [sys_permission]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 SysPermission")
    @RequestMapping(value = "/saveSysPermission", method = RequestMethod.PUT)
    private Msg saveSysPermission(@RequestBody SysPermission sys_permission) {
        sys_permission.setCreateTime(LocalDateTime.now());
        boolean state = baseService.save(sys_permission);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 SysPermission (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 SysPermission (非逻辑删除)")
    @RequestMapping(value = "/deleteSysPermission", method = RequestMethod.DELETE)
    private Msg deleteSysPermission(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 SysPermission
     * @Param: [sys_permission]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 SysPermission")
    @RequestMapping(value = "/updateSysPermission", method = RequestMethod.POST)
    private Msg updateSysPermission(@RequestBody SysPermission sys_permission) {
        boolean state = baseService.updateById(sys_permission);
        sys_permission.setUpdateTime(LocalDateTime.now());
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 SysPermission
     * @Param: [sys_permission]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 SysPermission")
    @RequestMapping(value = "/selectSysPermissions", method = RequestMethod.POST)
    private Msg selectSysPermissions(@RequestBody SysPermission sys_permission, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<SysPermission> page = new Page<SysPermission>(pageNum, pageSize);
        IPage<SysPermission> state = baseService.page(page, new QueryWrapper<SysPermission>().setEntity(sys_permission));
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 SysPermission
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 SysPermission")
    @RequestMapping(value = "/selectSysPermissionById", method = RequestMethod.POST)
    private Msg selectSysPermissionById(@RequestParam(value = "id", defaultValue = "no") String id) {
        SysPermission state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 SysPermission
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 SysPermission")
    @RequestMapping(value = "/importSysPermission", method = RequestMethod.POST)
    private Msg importSysPermission(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputSysPermissions(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 SysPermission
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 SysPermission")
    @RequestMapping(value = "/exportSysPermission", method = RequestMethod.POST)
    private void exportSysPermission(@RequestBody SysPermission sys_permission, HttpServletResponse response) {
        List<SysPermission> resultSet = baseService.list(new QueryWrapper<SysPermission>().setEntity(sys_permission));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, sys_permission.toString(), "1", SysPermission.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
