package com.redphase.api.atlas;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.framework.Response;

import java.util.List;
import java.util.Map;

/**
 * <p>电力设备 业务处理接口类。
 */
public interface IDataDeviceService {
    /**
     * <p>台账树加载
     */
    public List<ZTreeNodeDto> loadAccountTree(Map param, String... attrIds) throws Exception;
    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(DataDeviceDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(DataDeviceDto dto) throws Exception;


    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(DataDeviceDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataDeviceDto> findDataIsList(DataDeviceDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<DataDeviceDto> findListByDevice(DataDeviceDto dto) throws Exception;

    /**
     * 根据全名查询设备
     *
     * @param fullName
     * @return
     * @throws Exception
     */
    List<DataDeviceDto> findListByFullName(String fullName) throws Exception;

    /**
     * <p>信息详情。
     */
    public DataDeviceDto findDataById(DataDeviceDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public DataDeviceDto findDataByTestTechnology(DataDeviceDto dto) throws Exception;

    public DataDeviceDto getSaveDataDeviceDto(DataDeviceDto dataDeviceParameterDto) throws Exception;

    public DataDeviceDto findDataIsDevice(DataDeviceDto dto) throws Exception;

    List<String> findSubstationIsList() throws Exception;

    List<DataDeviceDto> findDeviceNameIsList(DataDeviceDto dto) throws Exception;

    List<String> findTestTechnologyIsList(DataDeviceDto dto) throws Exception;
}