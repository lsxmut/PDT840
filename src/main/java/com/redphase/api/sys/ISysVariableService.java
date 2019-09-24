package com.redphase.api.sys;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>系统_数据字典表 业务处理接口类。
 */
public interface ISysVariableService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(SysVariableDto dto) throws Exception;

    /**
     * <p>信息编辑。
     */
    public Response updateByCode(SysVariableDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(SysVariableDto dto) throws Exception;


    /**
     * <li>逻辑删除。
     */
    public String deleteDataById(SysVariableDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(SysVariableDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<SysVariableDto> findDataIsList(SysVariableDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<SysVariableDto> findChildDataIsList(SysVariableDto dto) throws Exception;

    /**
     * <p>信息树。
     */
    public List<SysVariableDto> findDataIsTree(SysVariableDto dto);

    /**
     * <p>信息详情。
     */
    public SysVariableDto findDataById(SysVariableDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public SysVariableDto findDataByCode(SysVariableDto dto) throws Exception;
}