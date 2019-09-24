package com.redphase.aspect;

import com.redphase.dto.user.UserDto;
import com.redphase.framework.IDto;
import com.redphase.framework.util.JwtUtil;
import com.redphase.framework.util.ReflectUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 切点类
 */
@Aspect
@Component
@Slf4j
public class RfAccount2BeanAspect {
    @Autowired
    private JwtUtil jwtUtil;

    //切点
    @Pointcut("@annotation(com.redphase.framework.annotation.RfAccount2Bean)")
    public void account2BeanAspect() {
    }

    /**
     * 用于 将当前操作人员的信息写入实体对象
     */
    @Before("account2BeanAspect()")
    public void doBefore(JoinPoint joinPoint) {
        try {
            UserDto userDto = jwtUtil.getSubject();
            Object[] objArr = joinPoint.getArgs();
            if (objArr != null) {
                for (Object obj : objArr) {
                    if (obj instanceof IDto) {
                        if (log.isDebugEnabled()) {
                            log.debug("=====前置通知开始=====");
                            log.debug("请求方法:" + (joinPoint.getTarget().getClass().getName() + "." + joinPoint.getSignature().getName() + "()"));
                            log.debug("请求人:" + userDto.getName());
                        }
                        ReflectUtil.setValueByFieldName(obj, "createId", userDto.getId());//创建者id
//                        ReflectUtil.setValueByFieldName(obj, "account", dto.getAccount());//id
                        ReflectUtil.setValueByFieldName(obj, "updateId", userDto.getId());//修改者id
                        break;
                    }
                }
            }
            log.debug("=====前置通知结束=====");
        } catch (Exception e) {
            //记录本地异常日志
            log.error("异常信息:{}", e.getMessage());
        }
    }
}
