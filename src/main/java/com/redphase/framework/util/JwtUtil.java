package com.redphase.framework.util;

import com.Application;
import com.redphase.dto.user.UserDto;
import com.redphase.view.AlertView;
import com.redphase.view.LoginView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
public class JwtUtil {
    @Autowired
    protected AlertView ialert;
    @Autowired
    private EhcacheUtil ehcacheUtil;

    public UserDto getSubject() {
        Object obj = ehcacheUtil.getCache(CommonConstant.JWT_HEADER_TOKEN_KEY);
        if (obj == null) {
            Application.getStage().setScene(null);
            Application.showView(LoginView.class);
            Application.getStage().setMaximized(true);
            Application.getStage().centerOnScreen();
            ialert.error("重新登录!");
            throw new RuntimeException("获取用户失败!");
        }
        if (log.isDebugEnabled()) {
            //log.debug("当前用户..->{}", obj);
        }
        return (UserDto) obj;
    }

    public boolean isPermitted(String authStr) {
        if (getSubject() == null) {
            throw new RuntimeException("用户未登录!");
        }
        Set<String> authorizationInfoPerms = getSubject().getAuthorizationInfoPerms();
        //log.debug("鉴定:{},权集:{}", authStr, authorizationInfoPerms);
        return authorizationInfoPerms != null && authorizationInfoPerms.contains(authStr);
    }
}
