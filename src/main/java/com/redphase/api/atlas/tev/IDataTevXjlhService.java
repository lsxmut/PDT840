package com.redphase.api.atlas.tev;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.tev.DataTevXjlhDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>地电波_联合巡检 业务处理接口类。
 */
public interface IDataTevXjlhService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataTevXjlhDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataTevXjlhDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataTevXjlhDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataTevXjlhDto> findDataIsList(DataTevXjlhDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataTevXjlhDto findDataById(DataTevXjlhDto dto) throws Exception;

    /**
     * 根据检测文件ID查询地电波_巡检数据
     *
     * @param ids 检测文件ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataTevXjlhDto> findDataByIds(String ids) throws Exception;
}