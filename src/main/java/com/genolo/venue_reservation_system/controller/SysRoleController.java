package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.model.SysRole;
import com.genolo.venue_reservation_system.service.SysRoleService;
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
 * 角色表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"角色表接口"})
@RestController
@RequestMapping("/sys-role")
public class SysRoleController {

    @Autowired
    SysRoleService baseService;

    /**
     * @Description: 新增 角色表
     * @Param: [sys_role]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 SysRole")
    @RequestMapping(value = "/saveSysRole", method = RequestMethod.PUT)
    private Msg saveSysRole(@RequestBody SysRole sys_role) {
        sys_role.setCreateTime(LocalDateTime.now());
        sys_role.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.save(sys_role);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 SysRole (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 SysRole (非逻辑删除)")
    @RequestMapping(value = "/deleteSysRole", method = RequestMethod.DELETE)
    private Msg deleteSysRole(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 SysRole
     * @Param: [sys_role]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 SysRole")
    @RequestMapping(value = "/updateSysRole", method = RequestMethod.POST)
    private Msg updateSysRole(@RequestBody SysRole sys_role) {
        sys_role.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.updateById(sys_role);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 SysRole
     * @Param: [sys_role]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 SysRole")
    @RequestMapping(value = "/selectSysRoles", method = RequestMethod.POST)
    private Msg selectSysRoles(@RequestBody SysRole sys_role, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<SysRole> page = new Page<SysRole>(pageNum, pageSize);
        QueryWrapper<SysRole> wrapper = new QueryWrapper<SysRole>().setEntity(sys_role);
        wrapper.orderBy(true, false, "update_time,create_time");
        IPage<SysRole> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 SysRole
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 SysRole")
    @RequestMapping(value = "/selectSysRoleById", method = RequestMethod.POST)
    private Msg selectSysRoleById(@RequestParam(value = "id", defaultValue = "no") String id) {
        SysRole state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 SysRole
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 SysRole")
    @RequestMapping(value = "/importSysRole", method = RequestMethod.POST)
    private Msg importSysRole(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputSysRoles(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 SysRole
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 SysRole")
    @RequestMapping(value = "/exportSysRole", method = RequestMethod.POST)
    private void exportSysRole(@RequestBody SysRole sys_role, HttpServletResponse response) {
        List<SysRole> resultSet = baseService.list(new QueryWrapper<SysRole>().setEntity(sys_role));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, sys_role.toString(), "1", SysRole.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
