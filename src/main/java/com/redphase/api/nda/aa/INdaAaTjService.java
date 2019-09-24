package com.redphase.api.nda.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaTjDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>空气式超声波_统计数据 业务处理接口类。
 */
public interface INdaAaTjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaTjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaTjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaTjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaTjDto> findDataIsList(DataAaTjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaTjDto findDataById(DataAaTjDto dto) throws Exception;

    List<DataAaTjDto> findDataByIds(String ids) throws Exception;
}