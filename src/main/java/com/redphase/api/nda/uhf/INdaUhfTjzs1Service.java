package com.redphase.api.nda.uhf;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.uhf.DataUhfTjzs1Dto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>特高频_模式1_统计噪声 业务处理接口类。
 */
public interface INdaUhfTjzs1Service {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataUhfTjzs1Dto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataUhfTjzs1Dto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataUhfTjzs1Dto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataUhfTjzs1Dto> findDataIsList(DataUhfTjzs1Dto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataUhfTjzs1Dto findDataById(DataUhfTjzs1Dto dto) throws Exception;
}