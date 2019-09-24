package com.redphase.service.setup;

import com.redphase.api.setup.ISqlitBackupService;
import com.redphase.framework.annotation.ALogOperation;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.FileUtil;
import com.redphase.framework.util.StrUtil;
import com.redphase.framework.util.ValidatorUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Slf4j
public class SqlitBackupService extends BaseService implements ISqlitBackupService {
    private String dbFilePath;
    private String sqlite3EXEFilePath;

    public String getPath() throws UnsupportedEncodingException {
        return FileUtil.getPath();
    }

    /**
     * 构造函数初始化数据源
     */
    public SqlitBackupService() {
        try {
            dbFilePath = getPath() + "/config/data/PDT840.db";
            sqlite3EXEFilePath = getPath() + "/config/data/sqlite3.exe";
            dbFilePath = StrUtil.replaceAll(dbFilePath, "\\\\", "/");
            sqlite3EXEFilePath = StrUtil.replaceAll(sqlite3EXEFilePath, "\\\\", "/");
            if (!(new File(dbFilePath).exists())) {
                dbFilePath = StrUtil.replaceAll(dbFilePath, "/config/", "/");
            }
            if (!(new File(sqlite3EXEFilePath).exists())) {
                sqlite3EXEFilePath = StrUtil.replaceAll(sqlite3EXEFilePath, "/config/", "/");
            }
            if (dbFilePath.startsWith("/")) {
                dbFilePath = dbFilePath.substring(1);
            }
            if (sqlite3EXEFilePath.startsWith("/")) {
                sqlite3EXEFilePath = sqlite3EXEFilePath.substring(1);
            }
        } catch (Exception e) {
            log.error("构造函数初始化数据源", e);
        }
    }

    /**
     * 恢复sqlite数据库
     **/
    @ALogOperation(type = "数据库还原", desc = "系统设置")
    public void restore(String backupFile) throws Exception {
        if (ValidatorUtil.notEmpty(backupFile)) {
            backupFile = StrUtil.replaceAll(backupFile, "\\\\", "/");
        }
        String cmd = sqlite3EXEFilePath + " " + dbFilePath + " \".read " + backupFile + "\" ";
        log.debug("restore cmd:-->{}", cmd);
        InputStream in = null;
        InputStreamReader reader = null;
        BufferedReader br = null;
        try {
            ProcessBuilder builder = new ProcessBuilder(sqlite3EXEFilePath, dbFilePath, "\".read", backupFile, "\"");
            builder.redirectErrorStream(true);
            Process process = builder.start();// rt.exec(cmd);
            in = process.getInputStream();// 控制台的输出信息作为输入流
            reader = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
            String inStr;
            StringBuffer sb = new StringBuffer("");
            // 组合控制台输出信息字符串
            br = new BufferedReader(reader);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            if (log.isDebugEnabled()) {
                log.debug("数据库还原:" + sb.toString());
            }

            try {
                process.waitFor();
            } catch (Exception e) {
                log.error("InterruptedException Exception encountered", e);
            }
            if (process.exitValue() != 0) {
                process.destroy();
            }
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
            }
            try {
                if (reader != null) reader.close();
            } catch (Exception e) {
            }
            try {
                if (br != null) br.close();
            } catch (Exception e) {
            }
        }
    }

    /**
     * 备份sqlite数据库
     */
    @ALogOperation(type = "数据库备份", desc = "系统设置")
    public void backup(String backupFile) throws Exception {
        Runtime rt = Runtime.getRuntime();
        String cmd = sqlite3EXEFilePath + " " + dbFilePath + " .dump";
        log.debug("backup cmd:-->{}", cmd);
        Process process = rt.exec(cmd);
        InputStream in = null;
        InputStreamReader reader = null;
        FileOutputStream fout = null;
        OutputStreamWriter writer = null;
        BufferedReader br = null;
        try {
            in = process.getInputStream();// 控制台的输出信息作为输入流
            reader = new InputStreamReader(in, "utf-8");
            // 设置输出流编码为utf-8。这里必须是utf-8，否则从流中读入的是乱码
            String inStr;
            StringBuffer sb = new StringBuffer("");
            // 组合控制台输出信息字符串
            br = new BufferedReader(reader);
            while ((inStr = br.readLine()) != null) {
                sb.append(inStr + "\r\n");
            }
            if (log.isDebugEnabled()) {
                log.debug("备份sql:" + sb.toString());
            }
            // 要用来做导入用的sql目标文件：
            fout = new FileOutputStream(backupFile);
            writer = new OutputStreamWriter(fout, "utf-8");
            writer.write(sb.toString());
            writer.flush();

            try {
                process.waitFor();
            } catch (Exception e) {
                log.error("InterruptedException Exception encountered", e);
            }
            if (process.exitValue() != 0) {
                process.destroy();
            }
        } finally {
            try {
                if (in != null) in.close();
            } catch (Exception e) {
            }
            try {
                if (reader != null) reader.close();
            } catch (Exception e) {
            }
            try {
                if (br != null) br.close();
            } catch (Exception e) {
            }
            try {
                if (writer != null) writer.close();
            } catch (Exception e) {
            }
            try {
                if (fout != null) fout.close();
            } catch (Exception e) {
            }
        }
    }
}