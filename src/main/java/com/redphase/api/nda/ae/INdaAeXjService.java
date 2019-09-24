package com.redphase.api.nda.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeXjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_巡检 业务处理接口类。
 */
public interface INdaAeXjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeXjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeXjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeXjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeXjDto> findDataIsList(DataAeXjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeXjDto findDataById(DataAeXjDto dto) throws Exception;

    List<DataAeXjDto> findDataByIds(String ids) throws Exception;
}