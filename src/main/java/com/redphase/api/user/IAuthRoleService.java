package com.redphase.api.user;


import com.github.pagehelper.PageInfo;
import com.redphase.dto.user.AuthPermDto;
import com.redphase.dto.user.AuthRoleDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>权限_角色信息   业务处理接口类。
 */
public interface IAuthRoleService {
    /**
     * <p>信息编辑。
     */
    Response saveOrUpdateData(AuthRoleDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    String deleteData(AuthRoleDto dto) throws Exception;

    /**
     * <p>逻辑删除。
     */
    String deleteDataById(AuthRoleDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    PageInfo findDataIsPage(AuthRoleDto dto);

    /**
     * <p>信息列表。
     */
    List<AuthRoleDto> findDataIsList(AuthRoleDto dto);

    /**
     * <p>信息详情。
     */
    AuthRoleDto findDataById(AuthRoleDto dto);

    /**
     * <p>获取当前角色已有(功能/权限)
     */
    List<AuthPermDto> findPermIsList(AuthRoleDto dto);
}