package com.redphase.api.atlas.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeBxDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>接触式超声波_波形图谱 业务处理接口类。
 */
public interface IDataAeBxService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeBxDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeBxDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeBxDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeBxDto> findDataIsList(DataAeBxDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeBxDto findDataById(DataAeBxDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    DataAeBxDto findDataByDevice(Map map) throws Exception;
}