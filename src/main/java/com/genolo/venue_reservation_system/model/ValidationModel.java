package com.genolo.venue_reservation_system.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @program: sbm_demo
 * @description: Validation模板
 * @author: WYHY
 * @create: 2018-09-21 15:48
 **/
@Data
@ExcelTarget("时间")
public class ValidationModel {

    @NotBlank(message = "用户名不能为空！")
    @Size(min = 6,max = 16,message = "用户名长度必须在6-16位之间！")
    private String name;

    @Excel(name = "密码")
    @NotBlank(message = "密码不能为空！")
    @Size(min = 6,max = 16,message = "密码长度必须在6-16位之间！")
    private String password;

}
