package com.genolo.venue_reservation_system.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 新闻公告表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ExcelTarget("news")
@ApiModel(value="News对象", description="新闻公告表")
public class News extends Model<News> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "新闻标题")
    @Excel(name = "新闻标题")
    private String newsTitle;

    @ApiModelProperty(value = "新闻内容")
    @Excel(name = "新闻内容")
    private String newsContent;

    @ApiModelProperty(value = "创建人id")
    @Excel(name = "创建人id")
    private String createUserId;

    @ApiModelProperty(value = "创建人姓名")
    @Excel(name = "创建人姓名")
    private String createUserName;

    @ApiModelProperty(value = "机构名称")
    @Excel(name = "机构名称")
    private String organization;

    @ApiModelProperty(value = "发布时间")
    @Excel(name = "发布时间")
    private LocalDateTime releaseTime;

    @ApiModelProperty(value = "发布状态 0_未发布 1_已发布 2_取消发布")
    @Excel(name = "发布状态 0_未发布 1_已发布 2_取消发布")
    private Integer releaseState;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间")
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
        return this.id;
    }

}
