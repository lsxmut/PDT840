package com.redphase.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 反射工具
 */
@Slf4j
public class ReflectUtil {
    /**
     * 获取obj对象fieldName的Field
     *
     * @param obj
     * @param fieldName
     * @return
     */
    public static Field getFieldByFieldName(Object obj, String fieldName) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getDeclaredField(fieldName);
            } catch (Exception e) {
                //log.error("反射取属性名异常!"+e.getMessage());
            }
        }
        return null;
    }

    /**
     * 获取obj对象methodName的Method
     *
     * @param obj
     * @param methodName
     * @return
     */
    public static Method getMethodByMethodName(Object obj, String methodName, Class<?>... parameterTypes) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                return superClass.getMethod(methodName, parameterTypes);
            } catch (Exception e) {
                //log.error("反射取属性名异常!"+e.getMessage());
            }
        }
        return null;
    }

    /**
     * 获取obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     */
    public static Object getValueByFieldName(Object obj, String fieldName) {
        Field field = getFieldByFieldName(obj, fieldName);
        Object value = null;
        try {
            if (field != null) {
                if (field.isAccessible()) {
                    value = field.get(obj);
                } else {
                    field.setAccessible(true);
                    value = field.get(obj);
                    field.setAccessible(false);
                }
            }
        } catch (Exception e) {
            //log.error("反射取属性值异常!"+e.getMessage());
        }
        return value;
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setValueByFieldName(Object obj, String fieldName, Object value) {
        for (Class<?> superClass = obj.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                if (field != null) {
                    if (field.isAccessible()) {
                        field.set(obj, value);
                    } else {
                        field.setAccessible(true);
                        field.set(obj, value);
                        field.setAccessible(false);
                    }
                }
            } catch (Exception e) {
                //log.error("反射设值异常!"+e.getMessage());
            }
        }
    }

    /**
     * 设置obj对象fieldName的属性值
     *
     * @param obj
     * @param fieldName
     * @param value
     */
    public static void setValueByFieldName2(Object obj, String fieldName, Object value) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            if (field.isAccessible()) {
                field.set(obj, value);
            } else {
                field.setAccessible(true);
                field.set(obj, value);
                field.setAccessible(false);
            }
        } catch (Exception e) {
            //log.error("反射设值异常!", e);
        }
    }
}
