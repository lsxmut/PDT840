package com.redphase.api.nda.tev;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.tev.DataTevXjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>地电波_巡检 业务处理接口类。
 */
public interface INdaTevXjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataTevXjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataTevXjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataTevXjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataTevXjDto> findDataIsList(DataTevXjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataTevXjDto findDataById(DataTevXjDto dto) throws Exception;

    /**
     * 根据检测文件ID查询地电波_巡检数据
     *
     * @param ids 检测文件ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataTevXjDto> findDataByIds(String ids) throws Exception;
}