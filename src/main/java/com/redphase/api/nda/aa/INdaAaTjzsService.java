package com.redphase.api.nda.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaTjzsDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>空气式超声波_统计噪声 业务处理接口类。
 */
public interface INdaAaTjzsService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaTjzsDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaTjzsDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaTjzsDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaTjzsDto> findDataIsList(DataAaTjzsDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaTjzsDto findDataById(DataAaTjzsDto dto) throws Exception;

    List<DataAaTjzsDto> findDataByIds(String ids) throws Exception;
}