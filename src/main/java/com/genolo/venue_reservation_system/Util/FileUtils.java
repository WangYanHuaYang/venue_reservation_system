package com.genolo.venue_reservation_system.Util;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.time.LocalDate;

/**
 * @Description: 文件工具类
 * @Param:
 * @return:
 * @Author: WJP
 * @Date: 2019/9/10 10:52
 */
public class FileUtils {

    public static String saveFile(MultipartFile file, String pathname) {
        try {
            File targetFile = new File(pathname);
//			if (targetFile.exists()) {
//				return pathname;
//			}

            if (!targetFile.getParentFile().exists()) {
                targetFile.getParentFile().mkdirs();
            }
            file.transferTo(targetFile);

            return pathname;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static boolean deleteFile(String pathname) {
        File file = new File(pathname);
        if (file.exists()) {
            boolean flag = file.delete();

            if (flag) {
                File[] files = file.getParentFile().listFiles();
                if (files == null || files.length == 0) {
                    file.getParentFile().delete();
                }
            }

            return flag;
        }

        return false;
    }

    public static String fileMd5(InputStream inputStream) {
        try {
            return DigestUtils.md5Hex(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getPath() {
        return "/" + LocalDate.now().toString().replace("-", "/") + "/";
    }

    /**
     * 将文本写入文件
     *
     * @param value
     * @param path
     */
    public static void saveTextFile(String value, String path) {
        FileWriter writer = null;
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            writer = new FileWriter(file);
            writer.write(value);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getText(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        try {
            return getText(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String getText(InputStream inputStream) {
        InputStreamReader isr = null;
        BufferedReader bufferedReader = null;
        try {
            isr = new InputStreamReader(inputStream, "utf-8");
            bufferedReader = new BufferedReader(isr);
            StringBuilder builder = new StringBuilder();
            String string;
            while ((string = bufferedReader.readLine()) != null) {
                string = string + "\n";
                builder.append(string);
            }

            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (isr != null) {
                try {
                    isr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    public static String getPrintSize(long size) {
// 如果字节数少于1024，则直接以B为单位，否则先除于1024，后3位因太少无意义
        if (size < 1024) {
            return String.valueOf(size) + "B";
        } else {
            size = size / 1024;
        }
// 如果原字节数除于1024之后，少于1024，则可以直接以KB作为单位
// 因为还没有到达要使用另一个单位的时候
// 接下去以此类推
        if (size < 1024) {
            return String.valueOf(size) + "KB";
        } else {
            size = size / 1024;
        }
        if (size < 1024) {
// 因为如果以MB为单位的话，要保留最后1位小数，
// 因此，把此数乘以100之后再取余
            size = size * 100;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "MB";
        } else {
// 否则如果要以GB为单位的，先除于1024再作同样的处理
            size = size * 100 / 1024;
            return String.valueOf((size / 100)) + "." + String.valueOf((size % 100)) + "GB";
        }
    }
}
