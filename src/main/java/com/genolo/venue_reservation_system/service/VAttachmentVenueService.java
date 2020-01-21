package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.genolo.venue_reservation_system.dao.DictionariesMapper;
import com.genolo.venue_reservation_system.model.Dictionaries;
import com.genolo.venue_reservation_system.model.VAttachmentVenue;
import com.genolo.venue_reservation_system.dao.VAttachmentVenueMapper;
import com.genolo.venue_reservation_system.model.VVenueInfo;
import com.genolo.venue_reservation_system.model.Venue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * VIEW 服务类
 * @author wyhy
 * @since 2020-01-08
 */
@Service
public class VAttachmentVenueService extends BaseService<VAttachmentVenueMapper, VAttachmentVenue> {

    @Autowired
    DictionariesMapper dictionariesMapper;

    @Override
    public <E extends IPage<VAttachmentVenue>> E page(E page, Wrapper<VAttachmentVenue> queryWrapper) {
        QueryWrapper<VAttachmentVenue> wrapper=(QueryWrapper<VAttachmentVenue>)queryWrapper;
        VAttachmentVenue venue=queryWrapper.getEntity();
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
        List<VAttachmentVenue> venues=p.getRecords();
        for (VAttachmentVenue v:venues){
            List<String> projects=new ArrayList<String>();
            if (v.getVenueProject()!=null&&v.getVenueProject().size()>0){
                for (String project:v.getVenueProject()){
                    Dictionaries dictionaries=dictionariesMapper.selectById(project);
                    if (dictionaries!=null){
                        projects.add(dictionaries.getDictionariesName());
                    }
                }
                v.setVenueProject(projects);
            }
        }
        return p;
    }

}
