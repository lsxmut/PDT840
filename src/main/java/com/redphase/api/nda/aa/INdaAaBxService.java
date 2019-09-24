package com.redphase.api.nda.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaBxDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_波形图谱 业务处理接口类。
 */
public interface INdaAaBxService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaBxDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaBxDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaBxDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaBxDto> findDataIsList(DataAaBxDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaBxDto findDataById(DataAaBxDto dto) throws Exception;
}