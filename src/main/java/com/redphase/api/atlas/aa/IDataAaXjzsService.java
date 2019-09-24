package com.redphase.api.atlas.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaXjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>空气式超声波_巡检噪声 业务处理接口类。
 */
public interface IDataAaXjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaXjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaXjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaXjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaXjzsDto> findDataIsList(DataAaXjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaXjzsDto findDataById(DataAaXjzsDto dto) throws Exception;

    List<DataAaXjzsDto> findDataByIds(String ids) throws Exception;
}