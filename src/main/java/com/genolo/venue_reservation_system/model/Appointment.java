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
 * 预约表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(autoResultMap = true)
@ExcelTarget("appointment")
@ApiModel(value="Appointment对象", description="预约表")
public class Appointment extends Model<Appointment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    private String id;

    @ApiModelProperty(value = "预约人")
    @Excel(name = "预约人")
    @TableField(condition = SqlCondition.LIKE)
    private String appointmentUserName;

    @ApiModelProperty(value = "预约团队")
    @Excel(name = "预约团队")
    @TableField(condition = SqlCondition.LIKE)
    private String appointmentTeamName;

    @ApiModelProperty(value = "联系电话")
    @Excel(name = "联系电话")
    private String appointmentPhoneNumber;

    @ApiModelProperty(value = "预约人数")
    @Excel(name = "预约人数")
    private Integer numberOfPersons;

    @ApiModelProperty(value = "场馆id")
    @Excel(name = "场馆id")
    private String venueId;

    @ApiModelProperty(value = "场馆名称")
    @Excel(name = "场馆名称")
    @TableField(condition = SqlCondition.LIKE)
    private String venueName;

    @ApiModelProperty(value = "所属学校")
    @Excel(name = "所属学校")
    @TableField(condition = SqlCondition.LIKE)
    private String venueSchool;

    @ApiModelProperty(value = "场馆地址")
    @Excel(name = "场馆地址")
    private String venueAddress;

    @ApiModelProperty(value = "预约开始时间")
    @Excel(name = "预约开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "预约结束时间")
    @Excel(name = "预约结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "有效状态 0_无效 1_有效")
    @Excel(name = "有效状态 0_无效 1_有效")
    private Integer effectiveState;

    @ApiModelProperty(value = "审核状态 0_未提交 1_待审核 2_审核通过")
    @Excel(name = "审核状态 0_未提交 1_待审核 2_审核通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "审核人id")
    @Excel(name = "审核人id")
    private String auditUserId;

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


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
