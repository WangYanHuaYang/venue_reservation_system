package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.Util.Utils;
import com.genolo.venue_reservation_system.model.LoginBean;
import com.genolo.venue_reservation_system.model.SysUser;
import com.genolo.venue_reservation_system.exceptions.CustomException;
import com.genolo.venue_reservation_system.service.SysUserService;
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
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"用户表接口"})
@RestController
@RequestMapping("/sys-user")
public class SysUserController {

    @Autowired
    SysUserService baseService;

    /**
     * @Description: 新增 用户表
     * @Param: [sys_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 SysUser and 获取邀请码")
    @RequestMapping(value = "/saveSysUser", method = RequestMethod.PUT)
    private Msg saveSysUser(@Valid @RequestBody SysUser sys_user){
        boolean state=false;
        try {
            state = baseService.save(sys_user);
        }catch (Exception e){
            if (e.getMessage().contains("phone_number_UNIQUE")){
                throw new CustomException(500,"手机号已被注册");
            }else if (e.getMessage().contains("e_mail_UNIQUE")){
                throw new CustomException(500,"邮箱已被注册");
            }
        }
        if (state) {
            return Msg.SUCCESS().add("InvitationCode",sys_user.getId());
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 SysUser (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 SysUser (非逻辑删除)")
    @RequestMapping(value = "/deleteSysUser", method = RequestMethod.DELETE)
    private Msg deleteSysUser(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 SysUser
     * @Param: [sys_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 SysUser and 使用邀请码注册")
    @RequestMapping(value = "/updateSysUser", method = RequestMethod.POST)
    private Msg updateSysUser(@Valid @RequestBody SysUser sys_user) {
        boolean state = baseService.updateById(sys_user);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 重置密码
     * @Param: [sys_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.POST)
    private Msg resetPassword(@RequestParam(name = "id")String id) {
        boolean state = baseService.resetPassword(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 SysUser
     * @Param: [sys_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 SysUser")
    @RequestMapping(value = "/selectSysUsers", method = RequestMethod.POST)
    private Msg selectSysUsers(@RequestBody SysUser sys_user, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<SysUser> page = new Page<SysUser>(pageNum, pageSize);
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>().setEntity(sys_user);
        wrapper.isNotNull("password");
        wrapper.orderBy(true, false, "update_time");
        IPage<SysUser> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 SysUser 树
     * @Param: [sys_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 SysUser 树")
    @RequestMapping(value = "/sysUsersTree", method = RequestMethod.POST)
    private List<SysUser> selectSysUsersTree(@RequestBody SysUser sys_user) {
        QueryWrapper<SysUser> wrapper = new QueryWrapper<SysUser>().setEntity(sys_user);
        wrapper.isNotNull("password");
        wrapper.orderBy(true, false, "update_time");
        List<SysUser> state = baseService.list(wrapper);
        return state;
    }

    /**
     * @Description: 根据id查询 SysUser
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 SysUser")
    @RequestMapping(value = "/selectSysUserById", method = RequestMethod.POST)
    private Msg selectSysUserById(@RequestParam(value = "id", defaultValue = "no") String id) {
        SysUser state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 用户登录
     * @Param: [usercode 用户名, pwd 密码]
     * @return: com.genolo.venue_reservation_system.Util.Msg
     * @Author: WYHY
     * @Date: 2020/1/6
     */
    @ApiOperation("登录")
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    private Msg Login(@NotBlank(message = "用户名不能为空")@RequestParam(value = "usercode") String usercode,
                      @NotBlank(message = "密码不能为空")@RequestParam(value = "pwd") String pwd) {
        try {
                LoginBean loginBean = baseService.login(usercode, Utils.MD5(pwd));
                switch (loginBean.getState()) {
                    case 401:
                    case 404:
                        return Msg.CUSTOM_MSG(loginBean.getState(), loginBean.getUserName());
                    default:
                        return Msg.SUCCESS().add("loginBean", loginBean);
                }
        } catch (Exception e) {
            e.printStackTrace();
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 SysUser
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 SysUser")
    @RequestMapping(value = "/importSysUser", method = RequestMethod.POST)
    private Msg importSysUser(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputSysUsers(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 SysUser
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 SysUser")
    @RequestMapping(value = "/exportSysUser", method = RequestMethod.POST)
    private void exportSysUser(@RequestBody SysUser sys_user, HttpServletResponse response) {
        List<SysUser> resultSet = baseService.list(new QueryWrapper<SysUser>().setEntity(sys_user));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, sys_user.toString(), "1", SysUser.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
