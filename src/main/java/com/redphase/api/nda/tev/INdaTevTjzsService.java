package com.redphase.api.nda.tev;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.tev.DataTevTjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>地电波_统计噪声 业务处理接口类。
 */
public interface INdaTevTjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataTevTjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataTevTjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataTevTjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataTevTjzsDto> findDataIsList(DataTevTjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataTevTjzsDto findDataById(DataTevTjzsDto dto) throws Exception;
}