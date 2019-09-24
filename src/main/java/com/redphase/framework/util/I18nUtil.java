package com.redphase.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.util.Locale;
import java.util.ResourceBundle;

@Slf4j
public class I18nUtil {
    protected static ResourceBundle i18nBundle;

    static {
        //取得系统默认的国家/语言环境
        Locale myLocale = Locale.forLanguageTag("zh");
        //根据指定国家/语言环境加载资源文件
        i18nBundle = ResourceBundle.getBundle("com.redphase.i18n.message", myLocale);
        //打印从资源文件中取得的消息
        log.info(i18nBundle.getString("i18n.info"));
    }

    public static String get(String key) {
        return i18nBundle.getString(key);
    }

    public static void main(String[] args) {
        System.out.println(I18nUtil.get("i18n.info"));
        System.out.println(I18nUtil.get("i18n.info"));
        System.out.println(I18nUtil.get("i18n.info"));
        System.out.println(I18nUtil.get("i18n.info"));
    }
}
