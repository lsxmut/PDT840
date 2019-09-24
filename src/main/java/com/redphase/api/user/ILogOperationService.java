package com.redphase.api.user;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.user.LogOperationDto;

import java.util.Map;

/**
 * <p>系统_管理员操作日志   业务处理接口类。
 */
public interface ILogOperationService {
    /**
     * 管理员操作日志记录。
     */
    void log(Map<String, Object> map);

    /**
     * <p>信息列表 分页。
     */
    PageInfo findDataIsPage(LogOperationDto dto);

    /**
     * <p>信息详情。
     */
    LogOperationDto findDataById(LogOperationDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    String deleteData(LogOperationDto dto) throws Exception;
}