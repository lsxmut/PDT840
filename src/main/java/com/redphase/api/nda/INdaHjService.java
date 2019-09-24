package com.redphase.api.nda;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>环境参数数据 业务处理接口类。
 */
public interface INdaHjService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataHjDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataHjDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataHjDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataHjDto> findDataIsList(DataHjDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataHjDto findDataById(DataHjDto dto) throws Exception;

    /**
     * 获取天气情况
     *
     * @param code
     * @return
     */
    String getWeather(Integer code);

    DataHjDto findDataByDevice(Map map) throws Exception;

    List<DataHjDto> findListByDevice(Map paramMap) throws Exception;
}