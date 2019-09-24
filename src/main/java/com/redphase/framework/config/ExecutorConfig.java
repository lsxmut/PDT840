/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2016 abel533@gmail.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.redphase.framework.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@Slf4j
@Order(-998)
public class ExecutorConfig {

    @ConfigurationProperties(prefix = "spring.thread-pool")
    @Data
    public static class ThreadPoolProperties {
        int corePoolSize = 15;
        int keepAliveSeconds = 300;
        int maxPoolSize = 20;
        int queueCapacity = 1024;
    }

    @Autowired
    private ThreadPoolProperties threadPoolProperties;

    /**
     * 加载属性文件数据
     */
    @Bean
    public ThreadPoolProperties threadPoolProperties() {
        return new ThreadPoolProperties();
    }

    /***
     *  线程池
     */
    @Bean
    public ThreadPoolTaskExecutor getTaskExecutor() {
        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//		线程池维护线程的最少数量
        taskExecutor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
//		线程池维护线程所允许的空闲时间
        taskExecutor.setKeepAliveSeconds(threadPoolProperties.getKeepAliveSeconds());
//		线程池维护线程的最大数量
        taskExecutor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
//		线程池所使用的缓冲队列 (卡不死机)
        taskExecutor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        return taskExecutor;
    }
}
