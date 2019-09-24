package com.redphase.api.nda.uhf;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.uhf.DataUhfTjlb1Dto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>特高频_模式1_统计录波 业务处理接口类。
 */
public interface INdaUhfTjlb1Service {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataUhfTjlb1Dto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataUhfTjlb1Dto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataUhfTjlb1Dto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataUhfTjlb1Dto> findDataIsList(DataUhfTjlb1Dto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataUhfTjlb1Dto findDataById(DataUhfTjlb1Dto dto) throws Exception;
}