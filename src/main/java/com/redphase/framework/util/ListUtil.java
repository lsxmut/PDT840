package com.redphase.framework.util;

import java.util.List;

/**
 * list工具类
 */
public class ListUtil {

    /**
     * 判断list是否为空
     *
     * @param list
     * @return
     */
    public static boolean isNotEmpty(List list) {
        return list != null && list.size() > 0;
    }
}
