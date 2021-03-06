package com.redphase.api.nda.uhf;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.uhf.DataUhfTj2Dto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>特高频_模式2_统计数据 业务处理接口类。
 */
public interface INdaUhfTj2Service {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataUhfTj2Dto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataUhfTj2Dto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataUhfTj2Dto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataUhfTj2Dto> findDataIsList(DataUhfTj2Dto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataUhfTj2Dto findDataById(DataUhfTj2Dto dto) throws Exception;

    List<DataUhfTj2Dto> findDataByIds(String ids) throws Exception;
}