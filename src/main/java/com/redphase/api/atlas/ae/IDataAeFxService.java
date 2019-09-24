package com.redphase.api.atlas.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeFxDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_飞行图谱 业务处理接口类。
 */
public interface IDataAeFxService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeFxDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeFxDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeFxDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeFxDto> findDataIsList(DataAeFxDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeFxDto findDataById(DataAeFxDto dto) throws Exception;
}