package com.genolo.venue_reservation_system.service;

import com.genolo.venue_reservation_system.model.Dictionaries;
import com.genolo.venue_reservation_system.dao.DictionariesMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * 字典表 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class DictionariesService extends BaseService<DictionariesMapper, Dictionaries> {

    /**
    * @Description: 导入 Dictionaries (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputDictionariess(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<Dictionaries> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),Dictionaries.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

}
