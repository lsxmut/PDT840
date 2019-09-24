package com.redphase.api.atlas.ae;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.ae.DataAeTjlbDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>接触式超声波_统计录波 业务处理接口类。
 */
public interface IDataAeTjlbService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAeTjlbDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAeTjlbDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAeTjlbDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAeTjlbDto> findDataIsList(DataAeTjlbDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAeTjlbDto findDataById(DataAeTjlbDto dto) throws Exception;
}