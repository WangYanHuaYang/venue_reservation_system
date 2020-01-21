package com.genolo.venue_reservation_system.service;


import com.genolo.venue_reservation_system.Util.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文件服务类
 * @author wyhy
 * @since 2020-01-03
 */
@Service
public class FileService {

    @Value("${files.path}")
    private String filesPath;

    @Value("${log.path}")
    private String logPath;

    public Map save(MultipartFile file,String venueName,String schoolName) throws IOException {
        StringBuilder fullPath=new StringBuilder();
        StringBuilder pathname=new StringBuilder();
        String fileOrigName = file.getOriginalFilename();
        if (!fileOrigName.contains(".")) {
            throw new IllegalArgumentException("缺少后缀名");
        }
        HashMap map=new HashMap();
//        String md5 = FileUtils.fileMd5(file.getInputStream());
//        fileOrigName = fileOrigName.substring(fileOrigName.lastIndexOf("."));
        pathname.append("/");
        pathname.append(schoolName);
        pathname.append("/");
        pathname.append(venueName);
        pathname.append("/");
        pathname.append(fileOrigName);
        fullPath.append(filesPath);
        fullPath.append(pathname);
        FileUtils.saveFile(file, fullPath.toString());

        long size = file.getSize()/1024;
        String contentType = file.getContentType();
//        map.put("md5",md5);
        map.put("contentType",contentType);
        map.put("size",FileUtils.getPrintSize(size));
        map.put("fullPath",fullPath.toString());
        map.put("pathname",pathname.toString());
        map.put("contentType",contentType.startsWith("image/") ? 1 : 0);

        return map;

    }


    public void delete(String fullPath) {
            FileUtils.deleteFile(fullPath);
    }

    public List<Map<String,Object>> getLogDic(){
        return FileUtils.getFiles(logPath);
    }


}
