package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.model.Dictionaries;
import com.genolo.venue_reservation_system.service.DictionariesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.springframework.beans.factory.annotation.Autowired;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * <p>
 * 字典表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"字典表接口"})
@RestController
@RequestMapping("/dictionaries")
public class DictionariesController {

    @Autowired
    DictionariesService baseService;

    /**
     * @Description: 新增 字典表
     * @Param: [dictionaries]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 Dictionaries")
    @RequestMapping(value = "/saveDictionaries", method = RequestMethod.PUT)
    private Msg saveDictionaries(@RequestBody Dictionaries dictionaries) {
        dictionaries.setCreateTime(LocalDateTime.now());
        dictionaries.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.save(dictionaries);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 Dictionaries (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 Dictionaries (非逻辑删除)")
    @RequestMapping(value = "/deleteDictionaries", method = RequestMethod.DELETE)
    private Msg deleteDictionaries(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 Dictionaries
     * @Param: [dictionaries]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 Dictionaries")
    @RequestMapping(value = "/updateDictionaries", method = RequestMethod.POST)
    private Msg updateDictionaries(@RequestBody Dictionaries dictionaries) {
        dictionaries.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.updateById(dictionaries);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 Dictionaries
     * @Param: [dictionaries]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 Dictionaries")
    @RequestMapping(value = "/selectDictionariess", method = RequestMethod.POST)
    private Msg selectDictionariess(@RequestBody Dictionaries dictionaries, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<Dictionaries> page = new Page<Dictionaries>(pageNum, pageSize);
        QueryWrapper<Dictionaries> wrapper = new QueryWrapper<Dictionaries>().setEntity(dictionaries);
        wrapper.orderBy(true, false, "update_time");
        IPage<Dictionaries> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 Dictionaries
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 Dictionaries")
    @RequestMapping(value = "/selectDictionariesById", method = RequestMethod.POST)
    private Msg selectDictionariesById(@RequestParam(value = "id", defaultValue = "no") String id) {
        Dictionaries state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 Dictionaries
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 Dictionaries")
    @RequestMapping(value = "/importDictionaries", method = RequestMethod.POST)
    private Msg importDictionaries(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputDictionariess(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 Dictionaries
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 Dictionaries")
    @RequestMapping(value = "/exportDictionaries", method = RequestMethod.POST)
    private void exportDictionaries(@RequestBody Dictionaries dictionaries, HttpServletResponse response) {
        List<Dictionaries> resultSet = baseService.list(new QueryWrapper<Dictionaries>().setEntity(dictionaries));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, dictionaries.toString(), "1", Dictionaries.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
