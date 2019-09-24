package com.redphase.api.nda.uhf;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.uhf.DataUhfTjlb2Dto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>特高频_模式2_统计录波 业务处理接口类。
 */
public interface INdaUhfTjlb2Service {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataUhfTjlb2Dto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataUhfTjlb2Dto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataUhfTjlb2Dto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataUhfTjlb2Dto> findDataIsList(DataUhfTjlb2Dto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataUhfTjlb2Dto findDataById(DataUhfTjlb2Dto dto) throws Exception;
}