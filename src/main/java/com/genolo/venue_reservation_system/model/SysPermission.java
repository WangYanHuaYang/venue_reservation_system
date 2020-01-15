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

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

/**
 * <p>
 * 权限表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName(autoResultMap = true)
@ExcelTarget("sys_permission")
@ApiModel(value="SysPermission对象", description="权限表")
public class SysPermission extends Model<SysPermission> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "上级权限id")
    @Excel(name = "上级权限id")
    @NotBlank(message = "上级权限id不能为空")
    private String parentId;

    @ApiModelProperty(value = "权限名")
    @Excel(name = "权限名")
    @NotBlank(message = "权限名不能为空")
    @TableField(condition = SqlCondition.LIKE)
    private String permissionName;

    @ApiModelProperty(value = "权限链接")
    @Excel(name = "权限链接")
    private String permissionUrl;

    @ApiModelProperty(value = "权限图标样式")
    @Excel(name = "权限图标样式")
    private String permissionCss;

    @ApiModelProperty(value = "权限类型  1_菜单 2_按钮")
    @Excel(name = "权限类型",replace = {"1_菜单","2_按钮"})
    private Integer permissionType;

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

    @ApiModelProperty(value = "排序")
    @Min(value = 1,message = "排序不能为空")
    private Integer sort;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
