package com.redphase.framework.util;


/**
 * <p>系统程序用常量</p>
 *
 * @author wuxiaogang
 */
public class CommonConstant {
    /**
     * 数据库事务默认超时时间
     */
    public static final int DB_DEFAULT_TIMEOUT = 300;
    /**
     * 路径分隔符
     */
    public static final String PATH_SEPARATOR = "/";
    /**
     * 系统默认编码
     */
    public static final String DEFAULT_ENCODE = "UTF-8";
    /**
     * 空字符串
     */
    public static final String EMPTY_STRING = "";
    /**
     * 空格
     */
    public static final String BLANK_STRING = " ";
    /**
     * 异常信息
     */
    public static final String LOG_ERROR_TITLE = "异常信息";

    public static final String JWT_HEADER_TOKEN_KEY = "Authorization";
    public static final String JWT_ID = "jwt";
    public static final String JWT_SECRET_KEY = "43455454gjixiuowrmkhdiuhs#^&(klefk!";
    public static final long JWT_TTL = 2 * 60 * 60 * 1000;  //jwt过期时间 毫秒
    public static final long JWT_TTL_REFRESH = 2 * 55 * 60 * 1000;  //jwt刷新 毫秒

    public static final int PAGEROW_DEFAULT_COUNT = 15;
    public static final String SYS_TIMEOUT_KEY = "SYS_TIMEOUT_KEY";
    /**
     * 任务单数据文件目录名称
     */
    public static final String ATLAS_DATA_DIR_NAME = "检查数据";
    public static final String ATLAS_DATA_SKILL_FULL_NAME = "ATLAS_DATA_SKILL_FULL_NAME";
    public static final String ATLAS_DATA_SKILL_DEVICE_MAIN = "ATLAS_DATA_SKILL_DEVICE_MAIN";
    public static final String ATLAS_DATA_SKILL_DEVICE_SUB = "ATLAS_DATA_SKILL_DEVICE_SUB";
    public static final String ATLAS_DATA_SKILL_DEVICE_SITE = "ATLAS_DATA_SKILL_DEVICE_SITE";

    public static final String FEIGN_ERROR_SYMBOL_STRING = "#Symbol#";
    public static final int BIG_DATA_LENGTH = 0;

    public static final String NDA_DATA_SKILL_FULL_NAME = "NDA_DATA_SKILL_FULL_NAME";
    public static final String NDA_DATA_SKILL_DEVICE_MAIN = "NDA_DATA_SKILL_DEVICE_MAIN";
    public static final String NDA_DATA_SKILL_DEVICE_SUB = "NDA_DATA_SKILL_DEVICE_SUB";
    public static final String NDA_DATA_SKILL_DEVICE_SITE = "NDA_DATA_SKILL_DEVICE_SITE";
}
