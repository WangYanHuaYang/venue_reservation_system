package com.genolo.venue_reservation_system.controller;

import com.genolo.venue_reservation_system.Util.FileUtil;
import com.genolo.venue_reservation_system.Util.Msg;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.model.News;
import com.genolo.venue_reservation_system.service.NewsService;
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
 * 新闻公告表 前端控制器
 * </p>
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Api(tags = {"新闻公告表接口"})
@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    NewsService baseService;

    /**
     * @Description: 新增 新闻公告表
     * @Param: [news]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("新增 News")
    @RequestMapping(value = "/saveNews", method = RequestMethod.PUT)
    private Msg saveNews(@RequestBody News news) {
        news.setCreateTime(LocalDateTime.now());
        news.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.save(news);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 删除 News (非逻辑删除)
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("删除 News (非逻辑删除)")
    @RequestMapping(value = "/deleteNews", method = RequestMethod.DELETE)
    private Msg deleteNews(@RequestParam(value = "id", defaultValue = "no") String id) {
        boolean state = baseService.removeById(id);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 修改 News
     * @Param: [news]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("修改 News")
    @RequestMapping(value = "/updateNews", method = RequestMethod.POST)
    private Msg updateNews(@RequestBody News news) {
        news.setUpdateTime(LocalDateTime.now());
        boolean state = baseService.updateById(news);
        if (state) {
            return Msg.SUCCESS();
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 多条件查询 News
     * @Param: [news]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("多条件查询 News")
    @RequestMapping(value = "/selectNewss", method = RequestMethod.POST)
    private Msg selectNewss(@RequestBody News news, @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum, @RequestParam(value = "pageSize", defaultValue = "1") Integer pageSize) {
        Page<News> page = new Page<News>(pageNum, pageSize);
        QueryWrapper<News> wrapper = new QueryWrapper<News>().setEntity(news);
        wrapper.orderBy(true, false, "update_time");
        IPage<News> state = baseService.page(page, wrapper);
        if (state.getSize() > 0) {
            return Msg.SUCCESS().add("resultSet", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 根据id查询 News
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("根据id查询 News")
    @RequestMapping(value = "/selectNewsById", method = RequestMethod.POST)
    private Msg selectNewsById(@RequestParam(value = "id", defaultValue = "no") String id) {
        News state = baseService.getById(id);
        if (state != null) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导入 News
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导入 News")
    @RequestMapping(value = "/importNews", method = RequestMethod.POST)
    private Msg importNews(@RequestParam(value = "file") MultipartFile file) {
        boolean state = baseService.imputNewss(file);
        if (state) {
            return Msg.SUCCESS().add("result", state);
        } else {
            return Msg.FAIL();
        }
    }

    /**
     * @Description: 导出 News
     * @Param: [id]
     * @Author: wyhy
     * @Date: 2018/9/30
     */
    @ApiOperation("导出 News")
    @RequestMapping(value = "/exportNews", method = RequestMethod.POST)
    private void exportNews(@RequestBody News news, HttpServletResponse response) {
        List<News> resultSet = baseService.list(new QueryWrapper<News>().setEntity(news));
        DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        FileUtil.exportExcel(resultSet, news.toString(), "1", News.class, LocalDateTime.now().format(df) + ".xlsx", response);
    }

}
