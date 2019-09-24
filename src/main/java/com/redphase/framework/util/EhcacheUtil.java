package com.redphase.framework.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

@Component
public class EhcacheUtil {
    @Autowired
    private CacheManager cacheManager;
    private final String cacheName = "defaultCache";

    /**
     * 设置缓存对象
     *
     * @param key
     * @param object
     */
    public void setCache(String key, Object object) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.put(key, object);
    }

    /**
     * 从缓存中取出对象
     *
     * @param key
     * @return
     */
    public Object getCache(String key) {
        Object object = null;
        synchronized (EhcacheUtil.class) {
            Cache cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                Cache.ValueWrapper valueWrapper = cache.get(key);
                if (valueWrapper != null) {
                    object = valueWrapper.get();
                }
            }
        }
        return object;
    }

    /**
     * 设置缓存对象
     *
     * @param cacheManager
     * @param key
     * @param object
     */
    public void setCache(CacheManager cacheManager, String key, Object object) {
        Cache cache = cacheManager.getCache(cacheName);
        cache.putIfAbsent(key, object);
    }

    /**
     * 从缓存中取出对象
     *
     * @param cacheManager
     * @param key
     * @return
     */
    public Object getCache(CacheManager cacheManager, String key) {
        Object object = null;
        Cache cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            Cache.ValueWrapper valueWrapper = cache.get(key);
            if (valueWrapper != null) {
                object = valueWrapper.get();
            }
        }
        return object;
    }

}
