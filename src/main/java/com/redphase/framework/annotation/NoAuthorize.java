package com.redphase.framework.annotation;

import java.lang.annotation.*;

/**
 * 无需登录
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NoAuthorize {
}
