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
 *  附件表	
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ExcelTarget("attachment")
@ApiModel(value="Attachment对象", description=" 附件表	")
public class Attachment extends Model<Attachment> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "附件名称")
    @Excel(name = "附件名称")
    private String attachmentName;

    @ApiModelProperty(value = "附件链接")
    @Excel(name = "附件链接")
    private String attachmentUrl;

    @ApiModelProperty(value = "附件所属id")
    @Excel(name = "附件所属id")
    private String attachmentBelongId;

    @ApiModelProperty(value = "上传人id")
    @Excel(name = "上传人id")
    private String uploadUserId;

    @ApiModelProperty(value = "附件大小 单位：K")
    @Excel(name = "附件大小 单位：K")
    private Double attachmentSize;

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
