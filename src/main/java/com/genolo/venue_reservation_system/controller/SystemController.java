package com.genolo.venue_reservation_system.controller;

import com.baomidou.mybatisplus.core.exceptions.MybatisPlusException;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.Util.MybatisAutoGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @program: sbm_demo
 * @description: 模板接口
 * @author: WYHY
 * @create: 2018-09-21 14:30
 **/
@Controller
@RequestMapping(value = "/system")
@Api(tags ={"系统管理接口"})
public class SystemController {

    @Autowired
    MybatisAutoGenerator generator;

    @RequestMapping(value = "/modelValidation",method = RequestMethod.GET)
    @ResponseBody
    public String modelValidation(){
        return "test";
    }

    @ApiOperation(value = "从表生成接口")
    @ResponseBody
    @RequestMapping(value = "/maketable",method = RequestMethod.GET)
    public Msg maketable(@RequestParam("tablename")String tablename){
        try {
            generator.makeTable(tablename);
            return Msg.SUCCESS();
        } catch (MybatisPlusException e) {
            e.printStackTrace();
            return Msg.FAIL();
        }
    }

}
