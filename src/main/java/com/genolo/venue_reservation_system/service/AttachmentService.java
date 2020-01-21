package com.genolo.venue_reservation_system.service;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.genolo.venue_reservation_system.Util.FileUtils;
import com.genolo.venue_reservation_system.model.Attachment;
import com.genolo.venue_reservation_system.dao.AttachmentMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import org.springframework.web.multipart.MultipartFile;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 附件表	 服务类
 *
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class AttachmentService extends BaseService<AttachmentMapper, Attachment> {

    @Value("${files.path}")
    private String filesPath;

    /**
     * @Description: 导入 Attachment (存在则刷新，不存在则新增)
     * @Param: [file]
     * @return: void
     * @Author: wyhy
     * @Date: 2020-01-03
     */
    public boolean imputAttachments(MultipartFile file) {
        ImportParams params = new ImportParams();
        boolean state = false;
        try {
            List<Attachment> resultSet = ExcelImportUtil.importExcel(file.getInputStream(), Attachment.class, params);
            state = saveOrUpdateBatch(resultSet, resultSet.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return state;
    }


    public Attachment save(MultipartFile file, Attachment attachment, String venueName, String schoolName,String fileName) throws IOException {
        StringBuilder fullPath = new StringBuilder();
        StringBuilder pathname = new StringBuilder();
        String fileOrigName = file.getOriginalFilename();
        String suffixNmae=fileOrigName.substring(fileOrigName.lastIndexOf("."));
        if (!fileOrigName.contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        pathname.append("/");
        pathname.append(schoolName);
        pathname.append("/");
        pathname.append(venueName);
        pathname.append("/");
        pathname.append(fileName);
        pathname.append(suffixNmae);
        fullPath.append(filesPath);
        fullPath.append(pathname);

        long size = file.getSize();
        attachment.setCreateTime(LocalDateTime.now());
        attachment.setUpdateTime(LocalDateTime.now());
        attachment.setAttachmentSize(FileUtils.getPrintSize(size));
        attachment.setReserve1(fullPath.toString());
        attachment.setAttachmentUrl(pathname.toString());
        String path=FileUtils.saveFile(file, fullPath.toString());
        if (!StringUtils.isBlank(path)){
            getBaseMapper().insert(attachment);
            return attachment;
        }else {
            return null;
        }
    }

}
