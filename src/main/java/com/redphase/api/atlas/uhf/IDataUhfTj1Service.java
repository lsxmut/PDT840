package com.redphase.api.atlas.uhf;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.uhf.DataUhfTj1Dto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>特高频_模式1_统计数据 业务处理接口类。
 */
public interface IDataUhfTj1Service {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataUhfTj1Dto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataUhfTj1Dto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataUhfTj1Dto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataUhfTj1Dto> findDataIsList(DataUhfTj1Dto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataUhfTj1Dto findDataById(DataUhfTj1Dto dto) throws Exception;

    List<DataUhfTj1Dto> findDataByIds(String ids) throws Exception;
}