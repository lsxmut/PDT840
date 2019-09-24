package com.redphase.framework.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解 写入账户信息到实体bean中
 */

@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RfAccount2Bean {
    String value() default "";
}
