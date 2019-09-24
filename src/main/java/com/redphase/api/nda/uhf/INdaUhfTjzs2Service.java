package com.redphase.api.nda.uhf;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.uhf.DataUhfTjzs2Dto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>特高频_模式2_统计噪声 业务处理接口类。
 */
public interface INdaUhfTjzs2Service {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataUhfTjzs2Dto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataUhfTjzs2Dto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataUhfTjzs2Dto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataUhfTjzs2Dto> findDataIsList(DataUhfTjzs2Dto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataUhfTjzs2Dto findDataById(DataUhfTjzs2Dto dto) throws Exception;
}