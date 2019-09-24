/*
 * 角色资源   业务处理接口类
 *
 * VERSION      DATE          BY              REASON
 * -------- ----------- --------------- ------------------------------------------
 * 1.00     2015.06.29      wuxiaogang         程序.发布
 * -------- ----------- --------------- ------------------------------------------
 * Copyright 2015 baseos System. - All Rights Reserved.
 *
 */
package com.redphase.api.user;


import com.redphase.dto.user.AuthPermDto;
import com.redphase.dto.user.AuthRoleDto;
import com.redphase.dto.user.UserDto;

import java.util.List;

/**
 * <p>角色资源   业务处理接口类。
 */
public interface IRoleSourceService {
    /**
     * <p>角色权限信息列表>根据员工id。
     */
    List<AuthPermDto> getPermListByUserId(UserDto user);

    public List<AuthRoleDto> getRoleListByUserId(UserDto dto);

    /**
     * <p>员工信息。
     */
    UserDto findUserByAccount(String account);

    /**
     * 更新员工登陆信息
     */
    Integer lastLogin(UserDto user);
}