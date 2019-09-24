package com.redphase.api.atlas.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeXjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_巡检噪声 业务处理接口类。
 */
public interface IDataAeXjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeXjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeXjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeXjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeXjzsDto> findDataIsList(DataAeXjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeXjzsDto findDataById(DataAeXjzsDto dto) throws Exception;
}