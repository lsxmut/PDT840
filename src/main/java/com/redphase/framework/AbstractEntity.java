package com.redphase.framework;

import java.io.Serializable;

public class AbstractEntity implements Serializable {
    protected static long serialVersionUID = 1L;

    /**
     * 对象值拷贝
     *
     * @param to  目标对象类
     * @param <T>
     * @return 目标对象
     * @throws Exception
     */
    public <T> T copyTo(Class<T> to) throws Exception {
        T obj = ObjectCopy.copyTo(this, to);
        return obj;
    }

}
