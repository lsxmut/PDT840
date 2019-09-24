package com.redphase.api.user;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.user.AuthPermDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>权限_权限信息   业务处理接口类。
 */
public interface IAuthPermService {

    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(AuthPermDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(AuthPermDto dto) throws Exception;

    /**
     * <p>恢复逻辑删除的数据。
     */
    public String recoveryDataById(AuthPermDto dto) throws Exception;

    /**
     * <p>逻辑删除。
     */
    public String deleteDataById(AuthPermDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(AuthPermDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<AuthPermDto> findDataIsList(AuthPermDto dto);

    /**
     * <p>信息详情。
     */
    public AuthPermDto findDataById(AuthPermDto dto);

    /**
     * <p>信息树。
     */
    public List<AuthPermDto> findDataIsTree(AuthPermDto dto);

    /**
     * <p>根据角色id获取对应的权限信息列表。
     */
    public List<AuthPermDto> findPermDataIsListByRoleId(Map dto);
}