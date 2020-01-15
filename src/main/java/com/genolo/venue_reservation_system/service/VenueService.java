package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.enums.SqlLike;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.genolo.venue_reservation_system.model.Venue;
import com.genolo.venue_reservation_system.dao.VenueMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 场馆表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class VenueService extends BaseService<VenueMapper, Venue> {

    /**
    * @Description: 导入 Venue (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputVenues(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<Venue> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),Venue.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public <E extends IPage<Venue>> E page(E page, Wrapper<Venue> queryWrapper) {
        QueryWrapper<Venue> wrapper=(QueryWrapper<Venue>)queryWrapper;
        Venue venue=queryWrapper.getEntity();
        if (venue.getVenueProject()!=null&&venue.getVenueProject().size()>0){
            List<String> projects=venue.getVenueProject();
            StringBuilder val=new StringBuilder();
            for (String project:projects){
                val.append(project);
                val.append("%");
            }
            venue.setVenueProject(null);
            wrapper.like("venue_project",val.toString());
        }
        E p= super.page(page, wrapper);
        return p;
    }
}
