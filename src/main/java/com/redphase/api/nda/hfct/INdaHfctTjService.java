package com.redphase.api.nda.hfct;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.hfct.DataHfctTjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>高频电流_统计数据 业务处理接口类。
 */
public interface INdaHfctTjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataHfctTjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataHfctTjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataHfctTjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataHfctTjDto> findDataIsList(DataHfctTjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataHfctTjDto findDataById(DataHfctTjDto dto) throws Exception;

    List<DataHfctTjDto> findDataByIds(String ids) throws Exception;
}