package com.redphase.api.nda.aa;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.aa.DataAaTjlbDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>空气式超声波_统计录波 业务处理接口类。
 */
public interface INdaAaTjlbService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAaTjlbDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAaTjlbDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAaTjlbDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAaTjlbDto> findDataIsList(DataAaTjlbDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAaTjlbDto findDataById(DataAaTjlbDto dto) throws Exception;
}