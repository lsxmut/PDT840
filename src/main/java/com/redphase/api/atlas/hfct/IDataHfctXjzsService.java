package com.redphase.api.atlas.hfct;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.hfct.DataHfctXjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>高频电流_巡检噪声 业务处理接口类。
 */
public interface IDataHfctXjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataHfctXjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataHfctXjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataHfctXjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataHfctXjzsDto> findDataIsList(DataHfctXjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataHfctXjzsDto findDataById(DataHfctXjzsDto dto) throws Exception;
}