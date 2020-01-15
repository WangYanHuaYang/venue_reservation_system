package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.TeamUser;
import com.genolo.venue_reservation_system.service.TeamUserService;
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
 * 团队人员表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-14
 */
@Api(tags = {"团队人员表接口"})
@RestController
@RequestMapping("/team-user")
public class TeamUserController {

    @Autowired
    TeamUserService baseService;

    /**
    * @Description: 新增 团队人员表
    * @Param: [team_user]
    * @Author: wyhy
    * @Date: 2018/9/30
    */
    @ApiOperation("新增 TeamUser")
    @RequestMapping(value = "/saveTeamUser",method = RequestMethod.PUT)
        private Msg saveTeamUser(@RequestBody TeamUser team_user){
        team_user.setCreateTime(LocalDateTime.now());
        team_user.setUpdateTime(LocalDateTime.now());
        boolean state=baseService.save(team_user);
        if (state){
            return Msg.SUCCESS();
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 TeamUser (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 TeamUser (非逻辑删除)")
    @RequestMapping(value = "/deleteTeamUser",method = RequestMethod.DELETE)
        private Msg deleteTeamUser(@RequestParam(value = "id",defaultValue = "no")String id){
        boolean state=baseService.removeById(id);
        if (state){
            return Msg.SUCCESS();
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 TeamUser
     * @Param: [team_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 TeamUser")
    @RequestMapping(value = "/updateTeamUser",method = RequestMethod.POST)
        private Msg updateTeamUser(@RequestBody TeamUser team_user){
        team_user.setUpdateTime(LocalDateTime.now());
        boolean state=baseService.updateById(team_user);
        if (state){
            return Msg.SUCCESS();
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 TeamUser
     * @Param: [team_user]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 TeamUser")
    @RequestMapping(value = "/selectTeamUsers",method = RequestMethod.POST)
        private Msg selectTeamUsers(@RequestBody TeamUser team_user,@RequestParam(value = "pageNum",defaultValue = "1")Integer pageNum,@RequestParam(value = "pageSize",defaultValue = "1")Integer pageSize){
        Page<TeamUser> page=new Page<TeamUser>(pageNum,pageSize);
        IPage<TeamUser> state=baseService.page(page,new QueryWrapper<TeamUser>().setEntity(team_user));
        if (state.getSize()>0){
            return Msg.SUCCESS().add("resultSet",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 TeamUser
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 TeamUser")
    @RequestMapping(value = "/selectTeamUserById",method = RequestMethod.GET)
        private Msg selectTeamUserById(@RequestParam(value = "id",defaultValue = "no")String id){
    TeamUser state=baseService.getById(id);
        if (state!=null){
            return Msg.SUCCESS().add("result",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 TeamUser
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 TeamUser")
    @RequestMapping(value = "/importTeamUser",method = RequestMethod.POST)
        private Msg importTeamUser(@RequestParam(value = "file")MultipartFile file){
        boolean state=baseService.imputTeamUsers(file);
        if (state){
            return Msg.SUCCESS().add("result",state);
        }else{
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 TeamUser
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 TeamUser")
    @RequestMapping(value = "/exportTeamUser",method = RequestMethod.GET)
        private void exportTeamUser(@RequestBody TeamUser team_user,HttpServletResponse response){
        List<TeamUser> resultSet=baseService.list(new QueryWrapper<TeamUser>().setEntity(team_user));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet,team_user.toString(),"1",TeamUser.class,LocalDateTime.now().format(df)+".xlsx",response);
    }

}
