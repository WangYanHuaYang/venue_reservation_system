package com.genolo.venue_reservation_system.service;

import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.dao.AttachmentMapper;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 *  附件表	 服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class AttachmentService extends BaseService<AttachmentMapper, Attachment> {

    /**
    * @Description: 导入 Attachment (存在则刷新，不存在则新增)
    * @Param: [file]
    * @return: void
    * @Author: wyhy
    * @Date: 2020-01-03
    */
    public boolean imputAttachments(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state=false;
        try {
            List<Attachment> resultSet= ExcelImportUtil.importExcel(file.getInputStream(),Attachment.class,params);
            state=saveOrUpdateBatch(resultSet,resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }

}
