package com.redphase.framework.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解 记录管理员操作日志
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ALogOperation {
    String type() default "";

    String desc() default "";
}
