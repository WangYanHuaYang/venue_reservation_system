package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.genolo.venue_reservation_system.Util.ServiceUtil;
import com.genolo.venue_reservation_system.Util.Utils;
import com.genolo.venue_reservation_system.dao.SysPermissionMapper;
import com.genolo.venue_reservation_system.dao.SysRoleMapper;
import com.genolo.venue_reservation_system.exceptions.CustomException;
import com.genolo.venue_reservation_system.model.*;
import com.genolo.venue_reservation_system.dao.SysUserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class SysUserService extends BaseService<SysUserMapper, SysUser> {

    @Autowired
    SysRoleMapper sysRoleMapper;
    @Autowired
    SysPermissionMapper sysPermissionMapper;

    /**
    * @Description: 导入 SysUser (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputSysUsers(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<SysUser> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),SysUser.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    /**
     * @Description: 用户登录
     * @Param: [usercode 身份证号或手机号, pwd 密码]
     * @return: com.genolo.smart_meeting_room.model.LoginBean
     * @Author: wyhy
     * @Date: 2019/9/12 11:01
     */
    public LoginBean login(String usercode, String pwd) throws Exception{
        LoginBean loginBean=new LoginBean();
        String column;
        if (Utils.checkEmail(usercode)){
            column="e_mail";
        }else if (Utils.Phone(usercode)){
            column="phone_number";
        }else {
            loginBean.setState(401);
            loginBean.setUserName("用户名格式错误");
            return loginBean;
        }
        SysUser user=getBaseMapper().selectOne(new QueryWrapper<SysUser>().eq(column,usercode));
        if (user!=null){
            if (pwd.equals(user.getPassword())){
                List<SysRole> roleList=new ArrayList<SysRole>();
                SysRole role=null;
                String[] roleNames = new String[user.getRoles().size()];
                for (int i=0;i<user.getRoles().size();i++){
                    role=sysRoleMapper.selectOne(new QueryWrapper<SysRole>().eq("id",user.getRoles().get(i)));
                    roleList.add(role);
                    roleNames[i]=role.getRoleName();
                }
                loginBean.setId(user.getId());
                loginBean.setRoleNames(roleNames);
                loginBean.setState(200);
                loginBean.setEMail(user.getEMail());
                loginBean.setOrganization(user.getOrganization());
                loginBean.setUserName(user.getUserName());
                loginBean.setPhoneNumber(user.getPhoneNumber());
                loginBean.setPermissionTree(getPermissionFromRole(roleList));
                return loginBean;
            }else {
                loginBean.setState(401);
                loginBean.setUserName("密码错误");
                return loginBean;
            }
        }else {
            loginBean.setState(404);
            loginBean.setUserName("用户不存在");
            return loginBean;
        }
    }

    /**
     * @Description: 获取角色和权限
     * @Param: [roles角色列表]
     * @return: java.util.List<com.genolo.smart_meeting_room.model.SysPermissionTree>
     * @Author: wyhy
     * @Date: 2019/9/12 11:04
     */
    public List<SysPermissionTree> getPermissionFromRole(List<SysRole> roles){
        List<String> permissionIdlist=new ArrayList<String>();
        for (int i =0;i<roles.size();i++){
            permissionIdlist.addAll(roles.get(i).getPermissions());
        }
        Utils.twoClear(permissionIdlist);
        List<SysPermissionTree> permissionList=new ArrayList<SysPermissionTree>();
        for (int i=0;i<permissionIdlist.size();i++){
            SysPermission sysPermission=sysPermissionMapper.selectOne(new QueryWrapper<SysPermission>().eq("id",permissionIdlist.get(i)));
            if (sysPermission!=null){
                SysPermissionTree permission=new SysPermissionTree(sysPermission);
                permissionList.add(permission);
            }
        }
        permissionList=permissionList.stream().sorted(Comparator.comparing(SysPermissionTree::getSort)).collect(Collectors.toList());
        return ServiceUtil.makePermissionTree(permissionList);
    }

    /**
     * @Description: 重置密码
     * @Param: [id]
     * @return: boolean
     * @Author: WYHY
     * @Date: 2020/1/15
     */
    public boolean resetPassword(String id){
        SysUser user=new SysUser();
        user.setId(id);
        user.setPassword(Utils.MD5(Utils.MD5("888888")));
        return retBool(getBaseMapper().updateById(user));
    }

    @Override
    public boolean updateById(SysUser entity) {
        entity.setUpdateTime(LocalDateTime.now());
        SysUser user=getBaseMapper().selectById(entity.getId());
        if (!Utils.MD5(entity.getOldPassword()).equals(user.getPassword())){
            throw new CustomException(500,"原密码错误");
        }
        if (entity.getOldPassword()==null){
            entity.setPassword(null);
        }
        return super.updateById(entity);
    }

    @Override
    public boolean save(SysUser entity) {
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setPassword(Utils.MD5(entity.getPassword()));
        return super.save(entity);
    }
}
