package com.redphase.framework;

import com.redphase.framework.annotation.FieldMapped;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 对象值复制：
 * ——注：支持字段属性名相同的值复制。当属性名不相同时，可使用 属性注解@FieldMapped映射
 */
@Slf4j
public class ObjectCopy {
    /**
     * 获取来源字段。并处理@FieldMapped 的字段映射（如属性名不同时）
     *
     * @param sourCls
     * @param toField
     * @return
     */
    private static Field getField(Class sourCls, Field toField) {
        Set<String> toNames = new HashSet<String>();
        toNames.add(toField.getName());
        //目标字段注解
        FieldMapped fMapped = toField.getAnnotation(FieldMapped.class);
        if (fMapped != null) {
            for (String cur_name : fMapped.names()) {
                if (!toNames.contains(cur_name)) {
                    toNames.add(cur_name);
                }
            }
        }

        do {
            for (Field from : sourCls.getDeclaredFields()) {
                //ru
                if (toNames.contains(from.getName())) {
                    return from;
                }
                //来源字段注解
                fMapped = from.getAnnotation(FieldMapped.class);
                if (fMapped != null) {
                    for (String cur_name : fMapped.names()) {
                        if (toNames.contains(cur_name)) {
                            return from;
                        }
                    }
                }
            }
            sourCls = sourCls.getSuperclass();
        } while (sourCls != Object.class);

        return null;
    }

    /**
     * 对象属性值复制。
     *
     * @param obj   值来源对象
     * @param toObj 值目标对象class
     * @param <T>
     * @return 目标对象
     * @throws Exception
     */
    public static <T> T copyTo(Object obj, Class<T> toObj) throws Exception {
        return copyTo(obj, toObj, false);
    }

    /**
     * 对象属性值复制。
     *
     * @param obj   值来源对象
     * @param toObj 值目标对象class
     * @param <T>
     * @return 目标对象
     * @throws Exception
     */
    public static <T> T copyTo(Object obj, Class<T> toObj, boolean isDesensitize) throws Exception {
        T toObjIns = (T) toObj.newInstance(); //创建目标对象实例

        //遍历目标所有属性
        Class toCls = toObj;
        Class sourCls;
        Field fromField = null;
        String fromType;
        //遍历源属性
        do {
            Field[] toFlds = toCls.getDeclaredFields(); //源属性集
            for (Field cur : toFlds) {
                cur.setAccessible(true);
                String name = cur.getName();
                String type = cur.getType().getTypeName();//得到此属性的类型

                sourCls = obj.getClass();
                fromField = getField(sourCls, cur);
                if (fromField == null) {
                    continue;
                }

                fromField.setAccessible(true);
                fromType = fromField.getType().getTypeName();

//                log.info("name={} from: type={}  to:type={}", name, fromType, type);
                if (type.equals(fromType)) {
                    cur.set(toObjIns, fromField.get(obj));
                } else {
                    //类型转换
                    try {
                        if (type.endsWith("String")) {
                            cur.set(toObjIns, fromField.get(obj));
                        } else if (type.endsWith("int") || type.endsWith("Integer")) {
                            if (fromType.endsWith("int") || fromType.endsWith("Integer")) {
                                cur.set(toObjIns, (Integer) fromField.get(obj));
                            } else {
                                cur.set(toObjIns, Integer.valueOf(String.valueOf(fromField.get(obj))));
                            }
                        } else if (type.endsWith("Date")) {
                            if (fromType.endsWith("Date")) {
                                cur.set(toObjIns, (Date) fromField.get(obj));
                            } else {
                                long d = 0;
                                if (fromType.endsWith("int") || fromType.endsWith("Integer") || type.endsWith("long") || type.endsWith("Long")) {
                                    d = (Long) fromField.get(obj);
                                } else {
                                    d = Long.valueOf(String.valueOf(fromField.get(obj)));
                                }
                                if (d == 0) {
                                    throw new Exception();
                                }
                                cur.set(toObjIns, new Date(d));
                            }
//                            cur.set(toObjIns, (Date) fromField.get(obj));
                        } else if (type.endsWith("long") || type.endsWith("Long")) {
                            if (fromType.endsWith("long") || fromType.endsWith("Long")) {
                                cur.set(toObjIns, (Long) fromField.get(obj));
                            } else {
                                cur.set(toObjIns, Long.valueOf(String.valueOf(fromField.get(obj))));
                            }
                        } else if (type.endsWith("short") || type.endsWith("Short")) {
                            if (fromType.endsWith("short") || fromType.endsWith("Short")) {
                                cur.set(toObjIns, (Short) fromField.get(obj));
                            } else {
                                cur.set(toObjIns, Short.valueOf(String.valueOf(fromField.get(obj))));
                            }
                        } else {
//                            log.error("类型转换失败！");
                            throw new Exception("类型转换失败！");
                        }
                    } catch (Exception e) {
                        log.error("类型转换失败:{}.{}[{}] convert to {}.{}[{}]", obj.getClass().getName(), name, fromType, toObj.getName(), name, type);
                        throw new IllegalArgumentException("类型转换失败:" + obj.getClass().getName() + "." + name + "[" + fromType + "] convert to "
                                + toObj.getName() + "." + name + "[" + type + "]");
                    }
                }
            }

            toCls = toCls.getSuperclass();
        } while (toCls != Object.class);
        return toObjIns;
    }

    /**
     * 对象复制：一个List列表
     *
     * @param from 来源列表
     * @param to   目标对象类
     * @param <T>
     * @return 目标对象列表
     * @throws Exception
     */
    public static <T> List<T> copyTo(List from, Class<T> to, boolean isDesensitize) {
        List<T> toList = new ArrayList<T>();
        for (Object cur : from) {
            try {
                toList.add(copyTo(cur, to, true));
            } catch (Exception e) {
                throw new IllegalArgumentException(e);
            }
        }

        return toList;
    }

    public static <T> List<T> copyTo(List from, Class<T> to) {
        return copyTo(from, to, false);
    }

    /**
     * 对象属性值复制
     *
     * @param map   来源MAap
     * @param toObj 目标对象类
     * @param <T>
     * @return 目标对象列表
     * @throws Exception
     */
    public static <T> T copyTo(Map map, Class<T> toObj) throws Exception {
        return copyTo(map, toObj, false);
    }

    /**
     * 对象属性值复制
     *
     * @param map   来源MAap
     * @param toObj 目标对象类
     * @param <T>
     * @return 目标对象列表
     * @throws Exception
     */
    public static <T> T copyTo(Map map, Class<T> toObj, boolean isDesensitize) throws Exception {
        T toObjIns = (T) toObj.newInstance(); //创建目标对象实例

        //遍历目标所有属性
        Class toCls = toObj;
//        Class sourCls;
//        Field fromField = null;
//        String fromType;
        //遍历源属性
        do {
            Field[] toFlds = toCls.getDeclaredFields(); //源属性集
            for (Field cur : toFlds) {
                cur.setAccessible(true);
                String mapKey = cur.getName();
                Object mapVal = map.get(mapKey);
                String type = cur.getType().getTypeName();//得到此属性的类型
                //类型转换
                try {
                    if (type.endsWith("String")) {
                        cur.set(toObjIns, String.valueOf(mapVal));
                    } else if (type.endsWith("int") || type.endsWith("Integer")) {
                        cur.set(toObjIns, Integer.valueOf(String.valueOf(mapVal)));
                    } else if (type.endsWith("Date")) {
                        if (mapVal instanceof Date) {
                            cur.set(toObjIns, new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse("" + mapVal));
                        }
                    } else if (type.endsWith("long") || type.endsWith("Long")) {
                        cur.set(toObjIns, Long.valueOf(String.valueOf(mapVal)));
                    } else if (type.endsWith("short") || type.endsWith("Short")) {
                        cur.set(toObjIns, Short.valueOf(String.valueOf(mapVal)));
                    } else {
                        //                            log.error("类型转换失败！");
                        throw new Exception("类型转换失败！");
                    }
                } catch (Exception e) {
                    log.error("类型转换失败:{}.{}[{}] convert to {}.{}[{}]", "map", mapKey, mapVal, toObj.getName(), mapKey, type);
                    throw new IllegalArgumentException("类型转换失败:" + "map" + "." + mapKey + "[" + mapVal + "] convert to " + toObj.getName() + "." + mapKey + "[" + type + "]");
                }
            }
            toCls = toCls.getSuperclass();
        } while (toCls != Object.class);
        return toObjIns;
    }
}
