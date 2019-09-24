package com.redphase.api.nda.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaFxDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_飞行图谱 业务处理接口类。
 */
public interface INdaAaFxService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaFxDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaFxDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaFxDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaFxDto> findDataIsList(DataAaFxDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaFxDto findDataById(DataAaFxDto dto) throws Exception;
}