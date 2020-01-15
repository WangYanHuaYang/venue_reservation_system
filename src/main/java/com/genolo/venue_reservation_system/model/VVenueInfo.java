package com.genolo.venue_reservation_system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.io.Serializable;
import java.util.List;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * VIEW
 * </p>
 *
 * @author wyhy
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(autoResultMap = true)
@ExcelTarget("v_venue_info")
@ApiModel(value="VVenueInfo对象", description="VIEW")
public class VVenueInfo extends Model<VVenueInfo> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @Excel(name = "id")
    private String id;

    @ApiModelProperty(value = "场馆名称")
    @Excel(name = "场馆名称")
    @TableField(condition = SqlCondition.LIKE)
    private String venueName;

    @ApiModelProperty(value = "省")
    @Excel(name = "省")
    private String province;

    @ApiModelProperty(value = "市")
    @Excel(name = "市")
    private String city;

    @ApiModelProperty(value = "区")
    @Excel(name = "区")
    private String district;

    @ApiModelProperty(value = "详细地址")
    @Excel(name = "详细地址")
    private String address;

    @ApiModelProperty(value = "经度")
    @Excel(name = "经度")
    private Double longitude;

    @ApiModelProperty(value = "纬度")
    @Excel(name = "纬度")
    private Double latitude;

    @ApiModelProperty(value = "场馆面积")
    @Excel(name = "场馆面积")
    private Double area;

    @ApiModelProperty(value = "可容纳人数")
    @Excel(name = "可容纳人数")
    private Integer numberOfPersons;

    @ApiModelProperty(value = "学校名称")
    @Excel(name = "字典名称")
    @TableField(condition = SqlCondition.LIKE)
    private String schoolName;

    @ApiModelProperty(value = "场馆项目")
    @Excel(name = "场馆项目")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> venueProject;

    @ApiModelProperty(value = "适宜人群")
    @Excel(name = "适宜人群")
    private String suitableCrowd;

    @ApiModelProperty(value = "开馆时间")
    @Excel(name = "开馆时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm")
    private LocalTime openTime;

    @ApiModelProperty(value = "闭馆时间")
    @Excel(name = "闭馆时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm")
    private LocalTime closeTime;

    @ApiModelProperty(value = "备注")
    @Excel(name = "备注")
    private String remarks;

    @ApiModelProperty(value = "午休开始时间")
    @Excel(name = "午休开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm")
    private LocalTime lunchStartTime;

    @ApiModelProperty(value = "午休结束时间")
    @Excel(name = "午休结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="HH:mm")
    private LocalTime lunchEndTime;

    @ApiModelProperty(value = "单次时间段 单位：小时")
    @Excel(name = "单次时间段 单位：小时")
    private Double timePeriod;

    @ApiModelProperty(value = "单次间隔时间 单位：分")
    @Excel(name = "单次间隔时间 单位：分")
    private Integer restTime;

    @ApiModelProperty(value = "审核状态 0_未提交 1_待审核 2_审核通过")
    @Excel(name = "审核状态 0_未提交 1_待审核 2_审核通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "用户名")
    @Excel(name = "用户名")
    @TableField(condition = SqlCondition.LIKE)
    private String createUserId;

    @ApiModelProperty(value = "用户名")
    @Excel(name = "用户名")
    @TableField(condition = SqlCondition.LIKE)
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

    @ApiModelProperty(value = "场馆制度")
    @Excel(name = "场馆制度")
    private String reserve1;

    @ApiModelProperty(value = "场馆区域划分")
    @Excel(name = "场馆区域划分")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> reserve2;

    @ApiModelProperty(value = "场馆简介")
    @Excel(name = "场馆简介")
    private String reserve3;

    @ApiModelProperty(value = "轮播图")
    @Excel(name = "轮播图")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> reserve4;

    @ApiModelProperty(value = "平面图")
    @Excel(name = "平面图")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> reserve5;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
