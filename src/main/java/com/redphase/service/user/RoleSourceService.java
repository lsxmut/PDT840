package com.redphase.service.user;

import com.redphase.api.user.IRoleSourceService;
import com.redphase.dao.master.user.IAuthPermDao;
import com.redphase.dao.master.user.IAuthRoleDao;
import com.redphase.dao.master.user.IUserDao;
import com.redphase.dto.user.AuthPermDto;
import com.redphase.dto.user.AuthRoleDto;
import com.redphase.dto.user.UserDto;
import com.redphase.entity.user.User;
import com.redphase.framework.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RoleSourceService extends BaseService implements IRoleSourceService {

    @Autowired
    private IAuthRoleDao authRoleDao;
    @Autowired
    private IAuthPermDao authPermDao;
    @Autowired
    private IUserDao userDao;

    @Override
    public List<AuthPermDto> getPermListByUserId(UserDto dto) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("userId", dto.getId());
            map.put("roleId", dto.getType());
            map.put("iissuperman", dto.getIissuperman());
            return copyTo(authPermDao.getPermListByUserId(map), AuthPermDto.class);
        } catch (Exception e) {
            log.error("角色权限信息列表>根据员工id,数据库处理异常!", e);
        }
        return null;
    }

    @Override
    public List<AuthRoleDto> getRoleListByUserId(UserDto dto) {
        try {
            Map<String, Object> map = new HashMap<>();
            map.put("staffId", dto.getId());
            map.put("roleId", dto.getType());
            map.put("iissuperman", dto.getIissuperman());
            return copyTo(authRoleDao.getRoleListByUserId(map), AuthRoleDto.class);
        } catch (Exception e) {
            log.error("角色信息列表>根据员工id,数据库处理异常!", e);
        }
        return null;
    }

    @Override
    public UserDto findUserByAccount(String account) {
        try {
            Map dto = new HashMap();
            dto.put("account", account);
            return copyTo(userDao.findUserByAccount(dto), UserDto.class);
        } catch (Exception e) {
            log.error("员工信息>根据员工登录名,数据库处理异常!", e);
        }
        return null;
    }

    @Override
    public Integer lastLogin(UserDto dto) {
        try {
            return userDao.lastLogin(copyTo(dto, User.class));
        } catch (Exception e) {
            log.error("更新最后登录时间!处理异常!", e);
        }
        return 0;
    }
}