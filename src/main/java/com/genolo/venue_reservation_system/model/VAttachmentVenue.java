package com.genolo.venue_reservation_system.model;

import java.math.BigDecimal;

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
 * @since 2020-01-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ExcelTarget("v_attachment_venue")
@TableName(autoResultMap = true)
@ApiModel(value="VAttachmentVenue对象", description="VIEW")
public class VAttachmentVenue extends Model<VAttachmentVenue> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "场馆名称")
    @Excel(name = "场馆名称")
    @TableField(condition = SqlCondition.LIKE)
    private String venueName;

    @ApiModelProperty(value = "所属学校")
    @Excel(name = "所属学校")
    @TableField(condition = SqlCondition.LIKE)
    private String schoolName;

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

    @ApiModelProperty(value = "场馆项目")
    @Excel(name = "场馆项目")
    @TableField(condition = SqlCondition.LIKE,typeHandler = FastjsonTypeHandler.class)
    private List<String> venueProject;

    @ApiModelProperty(value = "场馆面积")
    @Excel(name = "场馆面积")
    private Double area;

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

    @ApiModelProperty(value = "预约开始时间")
    @Excel(name = "预约开始时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime startTime;

    @ApiModelProperty(value = "预约结束时间")
    @Excel(name = "预约结束时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm")
    private LocalDateTime endTime;

    @ApiModelProperty(value = "可容纳人数")
    @Excel(name = "可容纳人数")
    private Integer maxPersons;

    @ApiModelProperty(value = "已预约人数")
    @Excel(name = "已预约人数")
    private Integer numberOfPersons;


    @Override
    protected Serializable pkVal() {
        return null;
    }

}
