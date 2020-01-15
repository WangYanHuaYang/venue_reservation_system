package com.genolo.venue_reservation_system.model;

import com.baomidou.mybatisplus.annotation.*;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import java.time.LocalDateTime;
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
 * 团队人员表
 * </p>
 *
 * @author wyhy
 * @since 2020-01-14
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@ExcelTarget("team_user")
@TableName(autoResultMap = true)
@ApiModel(value="TeamUser对象", description="团队人员表")
public class TeamUser extends Model<TeamUser> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.ASSIGN_UUID)
    private String id;

    @ApiModelProperty(value = "成员名")
    @Excel(name = "成员名")
    @TableField(condition = SqlCondition.LIKE)
    private String membersName;

    @ApiModelProperty(value = "成员电话")
    @Excel(name = "成员电话")
    private String membersPhone;

    @ApiModelProperty(value = "成员邮箱")
    @Excel(name = "成员邮箱")
    private String membersEMail;

    @ApiModelProperty(value = "创建时间")
    @Excel(name = "创建时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "更新时间")
    @Excel(name = "更新时间")
    private LocalDateTime updateTime;

    @ApiModelProperty(value = "删除状态 0_删除 1_未删除")
    @Excel(name = "删除状态 0_删除 1_未删除")
    private Integer delState;

    @ApiModelProperty(value = "成员身份证号")
    @Excel(name = "成员身份证号")
    private String membersCardId;

    @ApiModelProperty(value = "团队id")
    @Excel(name = "团队id")
    private String teamId;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
