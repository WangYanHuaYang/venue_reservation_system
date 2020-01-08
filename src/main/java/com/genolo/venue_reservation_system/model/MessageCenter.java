package com.genolo.venue_reservation_system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *  消息中心表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(autoResultMap = true)
@ExcelTarget("message_center")
@ApiModel(value="MessageCenter对象", description=" 消息中心表")
public class MessageCenter extends Model<MessageCenter> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "消息体")
    @Excel(name = "消息体")
    @TableField(condition = SqlCondition.LIKE)
    private String messageContent;

    @ApiModelProperty(value = "发送人id")
    @Excel(name = "发送人id")
    private String sendUserId;

    @ApiModelProperty(value = "发送人姓名")
    @Excel(name = "发送人姓名")
    @TableField(condition = SqlCondition.LIKE)
    private String sendUserName;

    @ApiModelProperty(value = "发送时间")
    @Excel(name = "发送时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime sendTime;

    @ApiModelProperty(value = "发送状态 0_未发送 1_已发送")
    @Excel(name = "发送状态 0_未发送 1_已发送")
    private Integer sendState;

    @ApiModelProperty(value = "读取状态 0_未读 1_已读")
    @Excel(name = "读取状态 0_未读 1_已读")
    private Integer readState;

    @ApiModelProperty(value = "接收人id")
    @Excel(name = "接收人id")
    private String receiveUserId;

    @ApiModelProperty(value = "接收人姓名")
    @Excel(name = "接收人姓名")
    @TableField(condition = SqlCondition.LIKE)
    private String receiveUserName;

    @ApiModelProperty(value = "接收人电话")
    @Excel(name = "接收人电话")
    private String receiveUserPhone;

    @ApiModelProperty(value = "接收人邮箱")
    @Excel(name = "接收人邮箱")
    private String receiveUserEMail;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除状态 0_删除 1_未删除")
    @Excel(name = "删除状态 0_删除 1_未删除")
    private Integer delState;

    @Excel(name = "")
    private String reserve1;

    @Excel(name = "")
    private String reserve2;

    @Excel(name = "")
    private String reserve3;

    @Excel(name = "")
    private String reserve4;

    @Excel(name = "")
    private String reserve5;

    @Excel(name = "")
    private String reserve6;

    @Excel(name = "")
    private String reserve7;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
