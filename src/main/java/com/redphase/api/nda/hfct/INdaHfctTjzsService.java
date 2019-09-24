package com.redphase.api.nda.hfct;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.hfct.DataHfctTjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>高频电流_统计噪声 业务处理接口类。
 */
public interface INdaHfctTjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataHfctTjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataHfctTjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataHfctTjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataHfctTjzsDto> findDataIsList(DataHfctTjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataHfctTjzsDto findDataById(DataHfctTjzsDto dto) throws Exception;
}