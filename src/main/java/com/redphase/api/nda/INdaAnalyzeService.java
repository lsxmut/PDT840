package com.redphase.api.nda;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>检测文件 业务处理接口类。
 */
public interface INdaAnalyzeService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataAnalyzeDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataAnalyzeDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataAnalyzeDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataAnalyzeDto> findDataIsList(DataAnalyzeDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataAnalyzeDto findDataById(DataAnalyzeDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public DataAnalyzeDto findDataByDataSource(DataAnalyzeDto dto) throws Exception;

    public DataAnalyzeDto getSaveDataAnalyzeDto(DataAnalyzeDto dataAnalyzeParameterDto, DataDeviceDto dataDeviceDto, DataDeviceSiteDto dataDeviceSiteDto) throws Exception;

    /**
     * 查询多个设备的所有检测文件
     *
     * @param ids 检测设备ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataAnalyzeDto> findDataByDataDeviceIds(String ids) throws Exception;

    List<DataAnalyzeDto> thendSearch(Map map) throws Exception;
    List<DataAnalyzeDto> findListByDevice(Map map) throws Exception;
}