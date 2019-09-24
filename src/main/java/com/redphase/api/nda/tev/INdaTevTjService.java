package com.redphase.api.nda.tev;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.tev.DataTevTjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>地电波_统计数据 业务处理接口类。
 */
public interface INdaTevTjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataTevTjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataTevTjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataTevTjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataTevTjDto> findDataIsList(DataTevTjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataTevTjDto findDataById(DataTevTjDto dto) throws Exception;

    /**
     * 根据检测文件ID查询地电波_统计数据
     *
     * @param ids 检测文件ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataTevTjDto> findDataByIds(String ids) throws Exception;
}