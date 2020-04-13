package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.genolo.venue_reservation_system.dao.SysUserMapper;
import com.genolo.venue_reservation_system.dao.TeamUserMapper;
import com.genolo.venue_reservation_system.model.MessageCenter;
import com.genolo.venue_reservation_system.dao.MessageCenterMapper;
import com.genolo.venue_reservation_system.model.News;
import com.genolo.venue_reservation_system.model.SysUser;
import com.genolo.venue_reservation_system.model.TeamUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *  消息中心表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class MessageCenterService extends BaseService<MessageCenterMapper, MessageCenter> {

    @Autowired
    TeamUserMapper teamUserMapper;
    @Autowired
    SysUserMapper sysUserMapper;

    /**
    * @Description: 导入 MessageCenter (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputMessageCenters(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<MessageCenter> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),MessageCenter.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    /**
     * @Description: 发送多人消息
     * @Param: [entity, users]
     * @return: boolean
     * @Author: WYHY
     * @Date: 2020/1/17
     */
    public boolean save(MessageCenter entity,List<String> userIds) {
        boolean success=false;
        List<MessageCenter> messageCenters=new ArrayList<MessageCenter>();
        for (String userId:userIds){
            TeamUser user=teamUserMapper.selectById(userId);
            MessageCenter message=new MessageCenter();
            message.setSendUserId(entity.getSendUserId());
            message.setSendUserName(entity.getSendUserName());
            message.setReserve1(entity.getReserve1());
            message.setMessageContent(entity.getMessageContent());
            message.setSendTime(LocalDateTime.now());
            message.setCreateTime(LocalDateTime.now());
            message.setUpdateTime(LocalDateTime.now());
            if (user!=null){
                if (!StringUtils.isBlank(user.getId())){
                    message.setReceiveUserId(user.getId());
                }
                if (!StringUtils.isBlank(user.getMembersName())){
                    message.setReceiveUserName(user.getMembersName());
                }
                if (!StringUtils.isBlank(user.getMembersPhone())){
                    message.setReceiveUserPhone(user.getMembersPhone());
                }
                if (!StringUtils.isBlank(user.getMembersEMail())){
                    message.setReceiveUserEMail(user.getMembersEMail());
                }
                messageCenters.add(message);
            }else {
                SysUser sysUser=sysUserMapper.selectById(userId);
                if (sysUser!=null){
                    if (!StringUtils.isBlank(sysUser.getId())){
                        message.setReceiveUserId(sysUser.getId());
                    }
                    if (!StringUtils.isBlank(sysUser.getUserName())){
                        message.setReceiveUserName(sysUser.getUserName());
                    }
                    if (!StringUtils.isBlank(sysUser.getPhoneNumber())){
                        message.setReceiveUserPhone(sysUser.getPhoneNumber());
                    }
                    if (!StringUtils.isBlank(sysUser.getEMail())){
                        message.setReceiveUserEMail(sysUser.getEMail());
                    }
                    messageCenters.add(message);
                }
            }
        }
        success=saveBatch(messageCenters,messageCenters.size());
        return success;
    }

    @Override
    public <E extends IPage<MessageCenter>> E page(E page, Wrapper<MessageCenter> queryWrapper) {
        QueryWrapper<MessageCenter> wrapper=(QueryWrapper<MessageCenter>) queryWrapper;
        MessageCenter news=queryWrapper.getEntity();
        if (news.getStime()!=null){
            wrapper.ge("update_time",news.getStime());
        }
        if (news.getEtime()!=null){
            wrapper.le("update_time",news.getEtime());
        }
//        wrapper.groupBy("message_content,reserve1");
        E p=super.page(page, wrapper);
        return p;
    }

    @Override
    public MessageCenter getById(Serializable id) {
        MessageCenter messageCenter=super.getById(id);
        QueryWrapper<MessageCenter> wrapper = new QueryWrapper<MessageCenter>();
        wrapper.eq("message_content",messageCenter.getMessageContent());
        wrapper.eq("reserve1",messageCenter.getReserve1());
        List<MessageCenter> messageCenters=getBaseMapper().selectList(wrapper);
        StringBuilder receiveId = new StringBuilder();
        StringBuilder receiveName = new StringBuilder();
        StringBuilder receivePhone = new StringBuilder();
        StringBuilder receiveEmail = new StringBuilder();
        for (MessageCenter mc:messageCenters){
            receiveId.append(mc.getReceiveUserId());
            receiveName.append(mc.getReceiveUserName());
            receivePhone.append(mc.getReceiveUserPhone());
            receiveEmail.append(mc.getReceiveUserEMail());
        }
        messageCenter.setReceiveUserId(receiveId.toString());
        messageCenter.setReceiveUserName(receiveName.toString());
        messageCenter.setReceiveUserPhone(receivePhone.toString());
        messageCenter.setReceiveUserEMail(receiveEmail.toString());
        return super.getById(id);
    }
}
