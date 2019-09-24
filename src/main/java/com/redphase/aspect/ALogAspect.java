package com.redphase.aspect;

import com.alibaba.fastjson.JSON;
import com.redphase.api.user.ILogOperationService;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.IDto;
import com.redphase.framework.annotation.ALogOperation;
import com.redphase.framework.util.JwtUtil;
import com.redphase.framework.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * 切点类
 */
@Aspect
@Component
@Slf4j
public class ALogAspect {
    @Autowired
    private JwtUtil jwtUtil;
    //注入Service用于把日志保存数据库
    @Autowired
    private ILogOperationService orgLogService;

    //操作日志切点
    @Pointcut("@annotation(com.redphase.framework.annotation.ALogOperation)")
    public void alogOperationAspect() {
    }

    /**
     * 用于记录员工的操作
     */
    @After("alogOperationAspect()")
    public void doAfter(JoinPoint joinPoint) throws Exception {
        try {
            //读取session中的员工
            UserDto userDto = jwtUtil.getSubject();
            if (userDto.getIissuperman() == 1) {
                log.debug("i is superman..");
                return;
            }
            //请求的IP
            String[] logArr = getMethodDesc(joinPoint);
            if (log.isDebugEnabled()) {
                log.debug("=====前置通知开始=====");
                log.debug("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
                log.debug("方法描述:" + logArr[1]);
                log.debug("请求人:" + userDto.getName());
            }
            Object object = new Object();
            Object[] objArr = joinPoint.getArgs();
            if (objArr != null) {
                for (Object obj : objArr) {
                    if (obj instanceof IDto) {
                        log.debug(JSON.toJSONString(obj));
                        object = obj;
                        break;
                    }
                }
            }
            String newFlag;
            try {
                String id = "" + ReflectUtil.getValueByFieldName(object, "id");
                newFlag = "" + ReflectUtil.getValueByFieldName(object, "newFlag");
                if ("0".equals(id) || "1".equals(newFlag)) {
                    logArr[0] = "新增";
                }
            } catch (Exception e) {
            }
            Object finalObject = object;
            orgLogService.log(new HashMap() {{
                put("type", logArr[0]);// 操作类型(a增d删u改q查)
                put("memo", logArr[1]);// 描述
                put("detailInfo", JSON.toJSONString(finalObject));// 具体
                put("createId", userDto.getId());// 操作人id
                put("createName", userDto.getName());// 操作人姓名
            }});
            log.debug("=====前置通知结束=====");
        } catch (Exception e) {
            //记录本地异常日志
            log.error("员工的操作日志异常:", e);
        }
    }

    /**
     * 记录操作异常日志
     *
     * @param joinPoint
     * @param e
     */
    @AfterThrowing(pointcut = "alogOperationAspect()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Throwable e) {
        try {
            //获取员工请求方法的参数并序列化为JSON格式字符串
            String params = "";
            if (joinPoint.getArgs() != null && joinPoint.getArgs().length > 0) {
                for (int i = 0; i < joinPoint.getArgs().length; i++) {
                    params += JSON.toJSONString(joinPoint.getArgs()[i]) + ";";
                }
            }
            //读取session中的员工
            UserDto userDto = jwtUtil.getSubject();
            if (userDto.getIissuperman() == 1) {
                log.debug("i is superman..");
                return;
            }
//            log.info("=={}",jwtUtil.getSubject());
            //获取请求ip
            String[] logArr = getMethodDesc(joinPoint);
            if (log.isDebugEnabled()) {
                log.debug("=====异常通知开始=====");
                log.debug("异常代码:" + e.getClass().getName());
                log.debug("异常信息:" + e.getMessage());
                log.debug("异常方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
                log.debug("方法描述:" + logArr[1]);
                log.debug("请求人:" + userDto.getName());
                log.debug("请求参数:" + params);
                log.debug("=====异常通知结束=====");
            }
            orgLogService.log(new HashMap() {{
                put("type", logArr[0]);// 操作类型(a增d删u改q查)
                put("memo", logArr[1]);// 描述
                put("detailInfo", e.getMessage());// 具体
                put("createId", userDto.getId());// 操作人id
                put("createName", userDto.getName());// 操作人姓名
            }});
            log.error("异常方法:{},异常代码:{},异常信息:{},参数:{}", joinPoint.getTarget().getClass().getName() + joinPoint.getSignature().getName(), e.getClass().getName(), e.getMessage(), params);
        } catch (Exception ex) {
            log.error("员工的操作日志异常:", ex);
        }
    }

    /**
     * 获取注解中对方法的描述信息
     */
    public static String[] getMethodDesc(JoinPoint joinPoint) throws Exception {
        String targetName = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        Object[] arguments = joinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        String[] logArr = new String[2];
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    logArr[0] = method.getAnnotation(ALogOperation.class).type();
                    logArr[1] = method.getAnnotation(ALogOperation.class).desc();
                    break;
                }
            }
        }
        return logArr;
    }
}
