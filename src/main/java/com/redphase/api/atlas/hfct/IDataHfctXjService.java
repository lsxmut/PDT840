package com.redphase.api.atlas.hfct;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.hfct.DataHfctXjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>高频电流_巡检 业务处理接口类。
 */
public interface IDataHfctXjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataHfctXjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataHfctXjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataHfctXjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataHfctXjDto> findDataIsList(DataHfctXjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataHfctXjDto findDataById(DataHfctXjDto dto) throws Exception;

    List<DataHfctXjDto> findDataByIds(String ids) throws Exception;
}