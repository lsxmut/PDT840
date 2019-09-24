package com.redphase.api.nda.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeTjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_统计噪声 业务处理接口类。
 */
public interface INdaAeTjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeTjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeTjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeTjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeTjzsDto> findDataIsList(DataAeTjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeTjzsDto findDataById(DataAeTjzsDto dto) throws Exception;
}