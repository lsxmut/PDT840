package com.redphase.api.nda.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeTjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_统计数据 业务处理接口类。
 */
public interface INdaAeTjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeTjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeTjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeTjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeTjDto> findDataIsList(DataAeTjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeTjDto findDataById(DataAeTjDto dto) throws Exception;

    List<DataAeTjDto> findDataByIds(String ids) throws Exception;
}