package com.redphase.api.user;


import com.github.pagehelper.PageInfo;
import com.redphase.dto.user.UserDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>组织架构_员工   业务处理接口类。
 */
public interface IUserService {

    /**
     * <p>信息编辑。
     */
    Response saveOrUpdateData(UserDto dto) throws Exception;

    /**
     * <p>信息新增。
     */
    Response insert(UserDto dto) throws Exception;

    /**
     * <p>信息编辑。
     */
    String updateData(UserDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    String deleteData(UserDto dto) throws Exception;

    /**
     * <p>恢复逻辑删除的数据 单条。
     */
    String recoveryDataById(UserDto dto) throws Exception;

    /**
     * <p>逻辑删除 单条。
     */
    String deleteDataById(UserDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    PageInfo findDataIsPage(UserDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    List<UserDto> findDataIsList(UserDto dto);

    /**
     * <p>信息详情。
     */
    UserDto findDataById(UserDto dto);

    /**
     * <p>判断员工账号是否存在
     */
    String isAccountYN(String account);

    /**
     * <p>密码修改
     */
    String updatePwd(UserDto dto) throws Exception;

    /**
     * <p>密码重置
     */
    String resetPwd(UserDto dto) throws Exception;

}