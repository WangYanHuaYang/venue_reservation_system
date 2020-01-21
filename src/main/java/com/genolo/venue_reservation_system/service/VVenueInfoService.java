package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.genolo.venue_reservation_system.dao.DictionariesMapper;
import com.genolo.venue_reservation_system.model.Dictionaries;
import com.genolo.venue_reservation_system.model.VVenueInfo;
import com.genolo.venue_reservation_system.dao.VVenueInfoMapper;
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
 * @since 2020-01-14
 */
@Service
public class VVenueInfoService extends BaseService<VVenueInfoMapper, VVenueInfo> {

    @Autowired
    DictionariesMapper dictionariesMapper;

    /**
    * @Description: 导入 VVenueInfo (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-14
    */
    public boolean imputVVenueInfos(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<VVenueInfo> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),VVenueInfo.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

    @Override
    public <E extends IPage<VVenueInfo>> E page(E page, Wrapper<VVenueInfo> queryWrapper) {
        QueryWrapper<VVenueInfo> wrapper=(QueryWrapper<VVenueInfo>)queryWrapper;
        VVenueInfo venue=queryWrapper.getEntity();
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
        List<VVenueInfo> venues=p.getRecords();
        for (VVenueInfo v:venues){
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
