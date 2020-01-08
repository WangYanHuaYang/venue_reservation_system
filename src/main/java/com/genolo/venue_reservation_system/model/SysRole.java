package com.genolo.venue_reservation_system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
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
 * 角色表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ExcelTarget("sys_role")
@TableName(autoResultMap = true)
@ApiModel(value="SysRole对象", description="角色表")
public class SysRole extends Model<SysRole> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "角色名")
    @Excel(name = "角色名")
    @TableField(condition = SqlCondition.LIKE)
    private String roleName;

    @ApiModelProperty(value = "角色描述")
    @Excel(name = "角色描述")
    private String roleDescription;

    @ApiModelProperty(value = "权限树")
    @Excel(name = "权限树")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> permissions;

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


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
