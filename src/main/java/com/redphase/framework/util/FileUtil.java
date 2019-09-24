package com.redphase.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class FileUtil {
    public static String getPath() throws UnsupportedEncodingException {
        URL url = FileUtil.class.getProtectionDomain().getCodeSource().getLocation();
        String path = null;
        path = URLDecoder.decode(url.getPath(), "utf-8");// 转化为utf-8编码
        if (path.endsWith(".jar")) {// 可执行jar包运行的结果里包含".jar"
            // 截取路径中的jar包名
            path = path.substring(0, path.lastIndexOf("/") + 1);
        }
        File file = new File(path);
        path = file.getAbsolutePath();//得到windows下的正确路径
        return path;
    }

    /**
     * 获取文件后缀
     */
    public static String getFileExt(String fileName) {
        int pos = fileName.lastIndexOf(".");
        if (pos > -1) {
            String temp = fileName.substring(pos + 1).toLowerCase();
            pos = temp.indexOf("?");
            if (pos > -1) {
                return temp.substring(0, pos);
            }
            return temp.trim().replaceAll("\"", "");
        }
        return "";
    }

    /**
     * 获取文件名
     */
    public static String getFileName(String filePath) {
        return new File(filePath).getName();
    }

    public static String readLineByLineJava8(String filePath) {
        StringBuilder contentBuilder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(filePath), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        } catch (Exception e) {
            log.error("文件读取异常!", e);
        }
        return contentBuilder.toString();
    }
}
