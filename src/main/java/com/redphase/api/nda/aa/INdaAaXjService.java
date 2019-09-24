package com.redphase.api.nda.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaXjDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>空气式超声波_巡检 业务处理接口类。
 */
public interface INdaAaXjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaXjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaXjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaXjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaXjDto> findDataIsList(DataAaXjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaXjDto findDataById(DataAaXjDto dto) throws Exception;

    List<DataAaXjDto> findDataByIds(String ids) throws Exception;

    /**
     * <p>根据设备id查询最大巡检数据。
     */
    DataAaXjDto findDataMaxByMap(Map map) throws Exception;
}