package com.redphase.api.user;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.user.LogLoginDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>组织架构_员工登录日志 业务处理接口类。
 */
public interface ILogLoginService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(LogLoginDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(LogLoginDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<LogLoginDto> findDataIsList(LogLoginDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public LogLoginDto findDataById(LogLoginDto dto) throws Exception;
}