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

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

/**
 * <p>
 * 用户表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ExcelTarget("sys_user")
@TableName(autoResultMap = true)
@ApiModel(value="SysUser对象", description="用户表")
public class SysUser extends Model<SysUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "上级id")
    @Excel(name = "上级id")
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> parentsId;

    @ApiModelProperty(value = "用户名")
    @Excel(name = "用户名")
    @TableField(condition = SqlCondition.LIKE)
    private String userName;

    @ApiModelProperty(value = "密码")
    @Excel(name = "密码")
    private String password;

    @ApiModelProperty(value = "手机号")
    @Pattern(regexp = "^[1](([3][0-9])|([4][5-9])|([5][0-3,5-9])|([6][5,6])|([7][0-8])|([8][0-9])|([9][1,8,9]))[0-9]{8}$",message = "手机号码格式错误")
    @Excel(name = "手机号")
    private String phoneNumber;

    @ApiModelProperty(value = "邮箱")
    @Email
    @Excel(name = "邮箱")
    private String eMail;

    @ApiModelProperty(value = "机构名称")
    @Excel(name = "机构名称")
    @TableField(condition = SqlCondition.LIKE)
    private String organization;

    @ApiModelProperty(value = "角色列表")
    @Excel(name = "角色列表")
    @NotEmpty
    @TableField(typeHandler = FastjsonTypeHandler.class)
    private List<String> roles;

    @ApiModelProperty(value = "用户层级")
    @Excel(name = "用户层级")
    private Integer userLevel;

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
