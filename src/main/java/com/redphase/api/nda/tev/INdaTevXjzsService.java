package com.redphase.api.nda.tev;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.tev.DataTevXjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>地电波_巡检噪声 业务处理接口类。
 */
public interface INdaTevXjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataTevXjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataTevXjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataTevXjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataTevXjzsDto> findDataIsList(DataTevXjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataTevXjzsDto findDataById(DataTevXjzsDto dto) throws Exception;

    /**
     * 根据检测文件ID查询地电波_巡检噪声数据
     *
     * @param ids 检测文件ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataTevXjzsDto> findDataByIds(String ids) throws Exception;
}