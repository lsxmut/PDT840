package com.redphase.api.atlas.tev;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.tev.DataTevTjlbDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>地电波_统计录波 业务处理接口类。
 */
public interface IDataTevTjlbService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataTevTjlbDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataTevTjlbDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataTevTjlbDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataTevTjlbDto> findDataIsList(DataTevTjlbDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataTevTjlbDto findDataById(DataTevTjlbDto dto) throws Exception;
}