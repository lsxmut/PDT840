package com.redphase.api.atlas;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>检查点 业务处理接口类。
 */
public interface IDataDeviceSiteService {
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataDeviceSiteDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataDeviceSiteDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataDeviceSiteDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataDeviceSiteDto> findDataIsList(DataDeviceSiteDto dto) throws Exception;


    /**
     * <p>信息详情。
     */
    public DataDeviceSiteDto findDataById(DataDeviceSiteDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public DataDeviceSiteDto findDataByDeviceAndSite(DataDeviceSiteDto dto) throws Exception;

    public DataDeviceSiteDto getSaveDataDeviceSiteDto(DataDeviceSiteDto dataDeviceSiteParameterDto) throws Exception;

    /**
     * 查询多个设备的所有检测位置
     *
     * @param ids 检测设备ID，逗号分隔
     * @return
     * @throws Exception
     */
    List<DataDeviceSiteDto> findDataByDataDeviceIds(String ids) throws Exception;

    List<String> findSiteNameIsList(DataDeviceSiteDto dto) throws Exception;
}