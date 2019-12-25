package com.genolo.venue_reservation_system.Util;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

/**
 * 工具类
 *
 * @author wjp
 *
 */
public class Utils {

    /**
     * 时间判断
     * */

    public static int isTime(Date starlTime,Date endTime){
        if(new Date().getTime() < starlTime.getTime()){
            return 1;
        }else if(new Date().getTime()>endTime.getTime()){
            return 2;
        }
        return 0;
    }


    /**
     * 判空
     *
     * @param obj
     * @return
     */
    public static boolean isNull(Object obj) {
        if (obj != null) {
            if (obj instanceof String) {
                if (!"".equals(obj.toString().trim())) {
                    return false;
                }
            } else {
                return false;
            }
            return true;
        }
        return true;
    }

    /**
     * 得到文件夹大小
     *
     * @param f
     *            文件
     * @return
     */
    public static long getFileSize(File f) {
        long fileSize = 0;
        if (f != null) {
            if (f.isFile()) {
                fileSize += f.length();
            }
            if (f.isDirectory()) {
                for (File f1 : f.listFiles()) {
                    fileSize += getFileSize(f1);
                }
            }
        }
        return fileSize;
    }

    /**
     * 设置文件名称唯一
     *
     * @param fileName
     * @return
     */
    public static String toFileNameOnly(String fileName) {
        return getUUID() + fileName.substring(fileName.lastIndexOf("."), fileName.length());
    }

    /**
     * 得到唯一字符串
     *
     * @return
     */
    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    /**
     * 判断路径是否为文件目录,是则返回true
     *
     * @param filePath
     * @return
     */
    public static boolean isFileDirectory(String filePath) {
        File file = new File(filePath);
        return file.isDirectory();
    }

    /**
     * 判断文件是否存在，存在返回true
     *
     * @param filePath
     * @return
     */
    public static boolean isFileExists(String filePath) {
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * 删除文件
     *
     * @param filePath
     *            文件路径以及文件名称
     */
    public static void fileDelete(String filePath) {
        File file = new File(filePath);
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            }
        }
    }

    /**
     * 删除某个文件夹下的所有文件夹和文件
     *
     * @param delpath
     *            String
     * @throws FileNotFoundException
     * @throws IOException
     * @return boolean
     */
    public static boolean deletefile(String delpath) throws Exception {
        try {

            File file = new File(delpath);
            // 当且仅当此抽象路径名表示的文件存在且 是一个目录时，返回 true
            if (!file.isDirectory()) {
                file.delete();
            } else if (file.isDirectory()) {
                String[] filelist = file.list();
                for (int i = 0; i < filelist.length; i++) {
                    File delfile = new File(delpath + "\\" + filelist[i]);
                    if (!delfile.isDirectory()) {
                        delfile.delete();
                    } else if (delfile.isDirectory()) {
                        deletefile(delpath + "\\" + filelist[i]);
                    }
                }
                file.delete();
            }

        } catch (Exception e) {
            System.out.println("deletefile() Exception:" + e.getMessage());
        }
        return true;
    }

    /**
     * 创建目录
     *
     * @param filePath
     * @return
     */
    public static boolean fileMkdirs(String filePath) {
        File file = new File(filePath);
        System.out.println(1);
        if (file.canRead() && file.canWrite()) {
            return file.mkdirs();
        }
        return false;
    }

    /**
     * 验证文件可读可写
     *
     * @param filePath
     * @return
     */
    public static boolean FileCanWrite(String filePath) {
        File file = new File(filePath);
        return file.canRead() && file.canWrite();
    }

    /**
     * 得到所有class属性包括父类
     *
     * @param class1
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Field[] getClassField(Class class1) {
        Field[] fields = class1.getDeclaredFields();
        Field[] fields2 = new Field[0];
        Class superclass = class1.getSuperclass();
        if (superclass != null) {// 简单的递归一下
            fields2 = getClassField(superclass);
        }
        Field[] fields3 = new Field[fields.length + fields2.length];
        System.arraycopy(fields, 0, fields3, 0, fields.length);
        System.arraycopy(fields2, 0, fields3, fields.length, fields2.length);
        return fields3;
    }

    /**
     * 文件上传
     *
     * @param inputfile
     *            需要上传文件
     * @param uploadURL
     *            服务器保存的地址
     * @param uploadFileName
     *            保存的文件名称
     * @return
     * @throws IOException
     */
    public static boolean upload(File inputfile, String uploadURL, String uploadFileName) throws IOException {
        FileInputStream fileInputStream = null;
        FileOutputStream fileOutputStream = null;
        try {
            // 判断目录是否已经存在
            File fileDirectory = new File(uploadURL);
            // 如果不存在则创建
            if (!fileDirectory.exists()) {
                fileDirectory.mkdirs();
            }
            fileInputStream = new FileInputStream(inputfile);
            fileOutputStream = new FileOutputStream(new File(uploadURL + File.separator + uploadFileName));
            int tempdata = 1024 * 8;
            byte[] b = new byte[tempdata];
            for (int i = 0; i < inputfile.length() / tempdata + 1; i++) {
                if (i == inputfile.length() / tempdata) {
                    tempdata = (int) (inputfile.length() - i * tempdata);
                    b = new byte[tempdata];
                }
                if (fileInputStream.read(b, 0, tempdata) != -1) {
                    fileOutputStream.write(b);
                }
                tempdata = 1024 * 8;
            }
        } catch (IOException e) {
            throw e;
        } finally {
            if (fileInputStream != null) {
                fileInputStream.close();
            }
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        }
        return true;
    }

    /**
     * 下载
     *
     * @param inputfile
     * @param outputStream
     * @return
     * @throws IOException
     */
    public static boolean download(File inputfile, OutputStream outputStream) throws IOException {
        if (inputfile != null && inputfile.exists()) {
            FileInputStream fileInputStream = null;
            try {
                fileInputStream = new FileInputStream(inputfile);
                int tempdata = 1024 * 8;
                byte[] b = new byte[tempdata];
                for (int i = 0; i < inputfile.length() / tempdata + 1; i++) {
                    if (i == inputfile.length() / tempdata) {
                        tempdata = (int) (inputfile.length() - i * tempdata);
                        b = new byte[tempdata];
                    }
                    if (fileInputStream.read(b, 0, tempdata) != -1) {
                        outputStream.write(b);
                    }
                    tempdata = 1024 * 8;
                }
            } catch (IOException e) {
                throw e;
            } finally {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
            }
        }
        return true;
    }

    /**
     * 下载文件到本地
     *
     * @param urlString
     *            被下载的文件地址
     * @param filename
     *            本地文件名
     * @throws Exception
     *             各种异常
     */
    public static void download(String urlString, String filename) throws Exception {
        InputStream is = null;
        GZIPInputStream gis = null;
        OutputStream os = null;
        try {
            URL url = new URL(urlString);
            URLConnection con = url.openConnection();
            is = con.getInputStream();
            String code = con.getHeaderField("Content-Encoding");
            if ((null != code) && code.equals("gzip")) {
                gis = new GZIPInputStream(is);
                byte[] bs = new byte[1024];
                int len;
                os = new FileOutputStream(filename);
                while ((len = gis.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
                gis.close();
                os.close();
                is.close();
            } else {
                // 1K的数据缓冲
                byte[] bs = new byte[1024];
                // 读取到的数据长度
                int len;
                // 输出的文件流
                os = new FileOutputStream(filename);
                // 开始读取
                while ((len = is.read(bs)) != -1) {
                    os.write(bs, 0, len);
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (is != null) {
                is.close();
            }
            if (gis != null) {
                gis.close();
            }
            if (os != null) {
                os.close();
            }
        }
    }

    /**
     * MD5加密
     *
     * @param
     * @return
     */
    public static String MD5(String str) {
        MessageDigest messageDigest = null;

        try {
            messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.reset();

            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }

        return md5StrBuff.toString();
    }
    /**
     * 根据消息资源文件名称获得消息资源文件对象
     *
     * @param propertiesName
     * @return
     */
    public static ResourceBundle getProperties(String propertiesName) {
        ResourceBundle rb = null;
        try {
            rb = ResourceBundle.getBundle(propertiesName);
        } catch (Exception e) {
            System.out.println("找不到指定的消息资源文件！");
        }
        return rb;
    }

    /**
     * 取随机产生的认证码(6个字符)
     *
     * @return
     */
    public static String createRandString() {
        // 生成随机类
        Random random = new Random();
        String sRand = "";
        String[] bytes = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
        for (int i = 0; i < 5; i++) {
            String rand = bytes[random.nextInt(36)];
            sRand += rand;
        }
        return sRand;
    }

    public static BufferedImage checkImg(String sRand) {
        // 在内存中创建图象
        int width = 80, height = 20;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // 获取图形上下文
        Graphics g = image.getGraphics();
        // 生成随机类
        Random random = new Random();
        // 设定背景色
        g.setColor(getRandColor(200, 250));
        g.fillRect(0, 0, width, height);
        // 设定字体
        g.setFont(new Font("Arial Unicode MS", Font.PLAIN, 16));
        // 画边框
        // g.setColor(new Color());
        // g.drawRect(0,0,width-1,height-1);
        // 随机产生155条干扰线，使图象中的认证码不易被其它程序探测到
        g.setColor(getRandColor(150, 200));
        for (int i = 0; i < 100; i++) {
            int x = random.nextInt(width);
            int y = random.nextInt(height);
            int xl = random.nextInt(12);
            int yl = random.nextInt(12);
            g.drawLine(x, y, x + xl, y + yl);
        }
        String[] ss = sRand.split("");
        // 取随机产生的认证码(4位数字)
        for (int i = 1; i < ss.length; i++) {
            // 将认证码显示到图象中
            g.setColor(new Color(20 + random.nextInt(110), 20 + random.nextInt(110), 20 + random.nextInt(110)));
            // 调用函数出来的颜色相同，可能是因为种子太接近，所以只能直接生成

            g.drawString(ss[i], 14 * (i - 1) + 6, 16);
        }
        return image;
    }

    /**
     * 给定范围获得随机颜色
     *
     * @param fc
     * @param bc
     * @return
     */
    private static Color getRandColor(int fc, int bc) {
        Random random = new Random();
        if (fc > 255)
            fc = 255;
        if (bc > 255)
            bc = 255;
        int r = fc + random.nextInt(bc - fc);
        int g = fc + random.nextInt(bc - fc);
        int b = fc + random.nextInt(bc - fc);
        return new Color(r, g, b);
    }

    /**
     * 过滤特殊字符
     *
     * @return String 过滤特殊符号后字符串
     *
     **/
    public static String toCharFilter(String str) {
        if (null != str && !"".equals(str)) {
            str = str.replaceAll("/", "//");
            str = str.replaceAll("_", "/_");
            str = str.replaceAll("%", "/%");
            return str;
        }
        return null;
    }


    /**
     * 模拟HTTP请求
     *
     * @param urlStr
     *            url地址
     * @param content
     *            参数,例如name=xxx&pwd=xxx
     * @param post
     *            请求类型,POST/GET
     * @return
     * @throws IOException
     */
    public static String getHttpResult(String urlStr, String content, String post) throws IOException {
        URL url = null;
        HttpURLConnection connection = null;
        DataOutputStream out = null;
        StringBuffer buffer = null;
        BufferedReader reader = null;
        try {
            url = new URL(urlStr);
            connection = (HttpURLConnection) url.openConnection();// 新建连接实例
            connection.setDoOutput(true);// 是否打开输出流 true|false
            connection.setDoInput(true);// 是否打开输入流true|false
            connection.setRequestMethod(post);// 提交方法POST|GET
            connection.setUseCaches(false);// 是否缓存true|false
            connection.connect();// 打开连接端口
            out = new DataOutputStream(connection.getOutputStream());// 打开输出流往对端服务器写数据
            out.writeBytes(content);// 写数据,也就是提交你的表单 name=xxx&pwd=xxx
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));// 往对端写完数据
            // //
            // 对端服务器返回数据
            // ,以BufferedReader流来读取
            buffer = new StringBuffer();
            String line = "";
            while ((line = reader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (!isNull(out)) {
                out.flush();// 刷新
                out.close();// 关闭输出流
            }
            if (!isNull(reader)) {
                reader.close();
            }
            if (!isNull(connection)) {
                connection.disconnect();// 关闭连接
            }
        }
        return buffer.toString();
    }
    /**
     *
     * 基本功能：过滤所有以"<"开头以">"结尾的标签
     * <p>
     *
     * @param str
     * @return String
     */
    public static String filterHtml(String str) {
        String regxpForHtml = "<([^>]*)>"; // 过滤所有以<开头以>结尾的标签
        Pattern pattern = Pattern.compile(regxpForHtml);
        Matcher matcher = pattern.matcher(str);
        StringBuffer sb = new StringBuffer();
        boolean result1 = matcher.find();
        while (result1) {
            matcher.appendReplacement(sb, "");
            result1 = matcher.find();
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    /**
     * 判断是否全为数字类型
     *
     * @param str
     * @return
     */
    public static boolean isNumeric2(String str) {
        for (int i = str.length(); --i >= 0;) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 获取Timestamp格式的当前时间
     *
     * @return
     */
    public static Timestamp getNowTime() {
        return new Timestamp(new Date().getTime());
    }

    public static Map<Object, Object> getPropertiesElement(String path) {
        File pf = new File(path);
        FileInputStream inpf = null;
        try {
            inpf = new FileInputStream(pf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 生成properties对象
        Properties p = new Properties();
        try {
            p.load(inpf);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p;
    }

    /**
     * 压缩图片方法
     *
     * @param oldFile
     *            将要压缩的图片
     * @param width
     *            压缩宽
     * @param height
     *            压缩高
     * @param quality
     *            压缩清晰度 <b>建议为1.0</b>
     * @param smallIcon
     *            压缩图片后,添加的扩展名（在图片后缀名前添加）
     * @param percentage
     *            是否等比压缩 若true宽高比率将将自动调整
     * @author slzs
     * @return 如果处理正确返回压缩后的文件名 null则参数可能有误
     * @throws IOException
     */
//    public static String doCompress(String oldFile, int width, int height, float quality, String smallIcon, boolean percentage) throws IOException {
//        if (oldFile != null && width > 0 && height > 0) {
//            Image srcFile = null;
//            String newImage = null;
//            FileOutputStream out = null;
//            try {
//                File file = new File(oldFile);
//                // 文件不存在
//                if (!file.exists()) {
//                    return null;
//                }
//                /* 读取图片信息 */
//                srcFile = ImageIO.read(file);
//                int new_w = width;
//                int new_h = height;
//                if (percentage) {
//                    // 为等比缩放计算输出的图片宽度及高度
//                    double rate1 = ((double) srcFile.getWidth(null)) / (double) width + 0.1;
//                    double rate2 = ((double) srcFile.getHeight(null)) / (double) height + 0.1;
//                    double rate = rate1 > rate2 ? rate1 : rate2;
//                    new_w = (int) (((double) srcFile.getWidth(null)) / rate);
//                    new_h = (int) (((double) srcFile.getHeight(null)) / rate);
//                }
//                /* 宽高设定 */
//                BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_INT_RGB);
//                tag.getGraphics().drawImage(srcFile, 0, 0, new_w, new_h, null);
//
//                /* 压缩后的文件名 */
//                String filePrex = oldFile.substring(0, oldFile.lastIndexOf('.'));
//                newImage = filePrex + smallIcon + oldFile.substring(filePrex.length());
//
//                /* 压缩之后临时存放位置 */
//                out = new FileOutputStream(newImage);
//
//                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//                JPEGEncodeParam jep = JPEGCodec.getDefaultJPEGEncodeParam(tag);
//
//                /* 压缩质量 */
//                jep.setQuality(quality, true);
//                encoder.encode(tag, jep);
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                srcFile.flush();
//                if (out != null) {
//                    out.flush();
//                    out.close();
//                }
//            }
//            return newImage;
//        } else {
//            return null;
//        }
//    }

    /**
     * 移动文件
     *
     * @throws IOException
     */
    public static void copyFile(String oldPath, String newPath) throws IOException {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            input = new FileInputStream(oldPath);// 可替换为任何路径何和文件名
            output = new FileOutputStream(newPath);// 可替换为任何路径何和文件名
            int in = input.read();
            while (in != -1) {
                output.write(in);
                in = input.read();
            }
        } catch (IOException e) {
            System.out.println(e.toString());
        } finally {
            input.close();
            output.flush();
            output.close();
        }
    }

    /**
     *
     * @param s
     * @return 获得图片
     */
    public static List<String> getImg(String s) {
        String regex;
        List<String> list = new ArrayList<String>();
        regex = "src=\"(.*?)\"";
        Pattern pa = Pattern.compile(regex, Pattern.DOTALL);
        Matcher ma = pa.matcher(s);
        while (ma.find()) {
            list.add(ma.group());
        }
        return list;
    }

    /**
     * 根据传入的URL
     *
     * @param url
     * @return
     */
    public static String[] shortUrl(String url) {
        // 可以自定义生成 MD5 加密字符传前的混合 KEY
        String key = "temobi";
        // 要使用生成 URL 的字符
        String[] chars = new String[] { "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
                "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"

        };
        // 对传入网址进行 MD5 加密
        String sMD5EncryptResult = Utils.MD5(key + url);
        String hex = sMD5EncryptResult;
        String[] resUrl = new String[4];
        for (int i = 0; i < 4; i++) {
            // 把加密字符按照 8 位一组 16 进制与 0x3FFFFFFF 进行位与运算
            String sTempSubString = hex.substring(i * 8, i * 8 + 8);
            // 这里需要使用 long 型来转换，因为 Inteper .parseInt() 只能处理 31 位 , 首位为符号位 , 如果不用
            // long ，则会越界
            long lHexLong = 0x3FFFFFFF & Long.parseLong(sTempSubString, 16);
            String outChars = "";
            for (int j = 0; j < 6; j++) {
                // 把得到的值与 0x0000003D 进行位与运算，取得字符数组 chars 索引
                long index = 0x0000003D & lHexLong;
                // 把取得的字符相加
                outChars += chars[(int) index];
                // 每次循环按位右移 5 位
                lHexLong = lHexLong >> 5;
            }
            // 把字符串存入对应索引的输出数组
            resUrl[i] = outChars;
        }
        return resUrl;
    }

    /**
     * 复制整个文件夹内容
     *
     * @param oldPath
     *            String 原文件路径 如：c:/fqf
     * @param newPath
     *            String 复制后路径 如：f:/fqf/ff
     * @return boolean
     * @throws IOException
     */
    public static void copyFolder(String oldPath, String newPath) throws IOException {
        FileInputStream input = null;
        FileOutputStream output = null;
        try {
            (new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
            File a = new File(oldPath);
            String[] file = a.list();
            File temp = null;
            for (int i = 0; i < file.length; i++) {
                if (oldPath.endsWith(File.separator)) {
                    temp = new File(oldPath + file[i]);
                } else {
                    temp = new File(oldPath + File.separator + file[i]);
                }

                if (temp.isFile()) {
                    input = new FileInputStream(temp);
                    output = new FileOutputStream(newPath + File.separator + (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ((len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                }
                if (temp.isDirectory()) {// 如果是子文件夹
                    copyFolder(oldPath + File.separator + file[i], newPath + File.separator + file[i]);
                }
            }
        } catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错 ");
            e.printStackTrace();
        } finally {
            if (!Utils.isNull(output)) {
                output.flush();
                output.close();
            }
            if (!Utils.isNull(input)) {
                input.close();
            }
        }
    }

    /**
     * 验证输入的字符串是否是邮箱
     *
     * @param
     * @return
     */
    public static boolean checkEmail(String email) {
        Pattern pattern = Pattern.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }

    /**
     * 正则表达式：验证身份证
     */
    public static boolean verForm(String num) {
        String reg = "^\\d{17}[0-9Xx]$";
        if (!num.matches(reg)) {
            return false;
        }
        return true;
    }

    /**
     * 正则表达式：验证手机号
     */
    public static boolean Phone(String num) {
        String reg = "^[1]([3-9])[0-9]{9}$";
        if (!num.matches(reg)) {
            return false;
        }
        return true;
    }

    /**
     * 正则表达式 Height 报名序号
     * */
    public static boolean  Height(String  Height) {
        String reg = "^[A-Za-z0-9]{0,8}$";
        if (!Height.matches(reg)) {
            return false;
        }
        return true;
    }

    /**
     * 正则表达式：验证考生号
     */
    public static boolean eamineeId(String eamineeId) {
        String reg = "^1822\\d{10}$";
        if (!eamineeId.matches(reg)) {
            return false;
        }
        return true;
    }
    /**
     * 正则表达式：验证邮政编码
     * */
    public static boolean postalCode(String postalCode) {
        String reg = "[1-9]\\d{5}";
        if (!postalCode.matches(reg)) {
            return false;
        }
        return true;
    }

    /**
     * 验证输入的字符攒是否是手机号
     *
     * @param phone
     * @return
     */
    public static boolean checkPhone(String phone) {
        Pattern pattern = Pattern.compile("/^[1]([3-9])[0-9]{9}$/");
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) {
            return true;
        }
        return false;
    }
    /**
     * 获取短码getUUID
     * @author wjp
     * @return
     */
    public static String getsUUID() {
        String[] new_uuid= shortUrl(getUUID());
        String shortuuid=new_uuid[0];
        return  shortuuid;
    }

    //随机生成5位随机数
    public final static String get5Radom(){
        String newString=null;

        //得到0.0到1.0之间的数字,并扩大100000倍
        double doubleP=Math.random()*100000;

        //如果数据等于100000,则减少1
        if(doubleP>=100000){
            doubleP=99999;
        }

        //然后把这个数字转化为不包含小数点的整数
        int tempString=(int)Math.ceil(doubleP);

        //转化为字符串
        newString=""+tempString;

        //把得到的数增加为固定长度,为5位
        while(newString.length()<5){
            newString="0"+newString;
        }

        return newString;
    }

    public final static String get6Radom(){
        String newString=null;

        //得到0.0到1.0之间的数字,并扩大100000倍
        double doubleP=Math.random()*1000000;

        //如果数据等于100000,则减少1
        if(doubleP>=1000000){
            doubleP=999999;
        }

        //然后把这个数字转化为不包含小数点的整数
        int tempString=(int)Math.ceil(doubleP);

        //转化为字符串
        newString=""+tempString;

        //把得到的数增加为固定长度,为5位
        while(newString.length()<6){
            newString="0"+newString;
        }

        return newString;
    }
//    public static void main(String[] args) {
        /*Utils u = new Utils();*/
        //Set<Integer> set = new HashSet<Integer>();
	/*Random random = new Random();

	int aaa=	random.nextInt(99999);
		String bbb=Integer.toString(aaa);

String aaa="22038119991103522x";
	Utils u = new Utils();
	System.out.println(u.verForm(aaa));
	*/
//	Utils u = new Utils();
//	u.shijian();

//    }
    public static int LoginTime(){
        SimpleDateFormat sdf=new SimpleDateFormat("HH");
        int a= Integer.parseInt(sdf.format(new Date()));
		/*if(a<6){
			System.out.println("未到时间");
			return 1;
		}else if(a>19){
			System.out.println("已过时间");
			return 2;
		}else{
			System.out.println("通过");*/
        return 3;
        //}
    }


    //身份证 获取性别
    public static int cardGender(String cardId){
        String id17 = cardId.substring(16, 17);
        if (Integer.parseInt(id17) % 2 != 0) {
            return 1;
        } else {
            return 2;
        }
    }

    //身份证 获取出生日期
    public static String cardBirthday(String cardId){
        String birthday = cardId.substring(6, 14);
        StringBuffer s = new StringBuffer(birthday);
        s.insert(4, "-");
        s.insert(7, "-");
        //Date birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(birthday);
        return s.toString();
    }

    /**
     * 判断对象或对象数组中每一个对象是否为空: 对象为null，字符序列长度为0，集合类、Map为empty
     *
     * @param obj
     * @return
     */
    public static boolean isNullOrEmpty(Object obj) {
        if (obj == null)
            return true;

        if (obj instanceof CharSequence)
            return ((CharSequence) obj).length() == 0;

        if (obj instanceof Collection)
            return ((Collection) obj).isEmpty();

        if (obj instanceof Map)
            return ((Map) obj).isEmpty();

        if (obj instanceof Object[]) {
            Object[] object = (Object[]) obj;
            if (object.length == 0) {
                return true;
            }
            boolean empty = true;
            for (int i = 0; i < object.length; i++) {
                if (!isNullOrEmpty(object[i])) {
                    empty = false;
                    break;
                }
            }
            return empty;
        }
        return false;
    }


    /**
     * 生成0001格式
     * */
    public static String examine(Integer max){
        DecimalFormat decimalFormat = new DecimalFormat("0000");
        return decimalFormat.format(max);
    }

}
