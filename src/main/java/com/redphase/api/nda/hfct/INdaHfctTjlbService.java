package com.redphase.api.nda.hfct;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.hfct.DataHfctTjlbDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>高频电流_统计录波 业务处理接口类。
 */
public interface INdaHfctTjlbService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataHfctTjlbDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataHfctTjlbDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataHfctTjlbDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataHfctTjlbDto> findDataIsList(DataHfctTjlbDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataHfctTjlbDto findDataById(DataHfctTjlbDto dto) throws Exception;
}