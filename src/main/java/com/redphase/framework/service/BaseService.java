package com.redphase.framework.service;

import com.redphase.framework.ObjectCopy;
import com.redphase.framework.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.Observable;

/**
 * <p> service 基类 放置service通用资源或公共方法
 */
public abstract class BaseService extends Observable {
    /**
     * 线程池
     */
    @Autowired
    protected ThreadPoolTaskExecutor taskExecutor;
    @Autowired
    protected JwtUtil jwtUtil;

    protected Integer PN(Integer pageNum) {
        if (pageNum == null || pageNum == 0) {
            return 1;
        } else {
            return pageNum;
        }
    }

    protected Integer PS(Integer pageSize) {
        if (pageSize == null || pageSize == 0 || pageSize > 50) {//防止被攻击 限制每页数据最大值
            return 15;
        } else {
            return pageSize;
        }
    }

    public <T> T copyTo(Object obj, Class<T> toObj) throws Exception {
        if (obj == null) {
            return null;
        }
        return ObjectCopy.copyTo(obj, toObj);
    }

    public <T> List<T> copyTo(List from, Class<T> to) {
        if (from == null) {
            return null;
        }
        return ObjectCopy.copyTo(from, to, true);
    }
}
