package com.redphase.api.atlas;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.DataLcDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>负荷电流数据 业务处理接口类。
 */
public interface IDataLcService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataLcDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataLcDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataLcDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataLcDto> findDataIsList(DataLcDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataLcDto findDataById(DataLcDto dto) throws Exception;

    /**
     * <p>根据设备id查询最大负荷电流。
     */
    public DataLcDto findDataMaxByMap(Map map) throws Exception;
}