package com.redphase.service.nda;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.INdaDeviceService;
import com.redphase.dao.slave.nda.INdaDeviceDao;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.entity.atlas.DataDevice;
import com.redphase.framework.MultiTree;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>电力设备 业务处理实现类。
 */
@Service
@Slf4j
public class NdaDeviceService extends BaseService implements INdaDeviceService {
    @Autowired
    private INdaDeviceDao dataDeviceDao;

    /**
     * 加载台帐树
     */
    @Override
    public List<ZTreeNodeDto> loadAccountTree(Map param, String... attrIds) throws Exception {
        //查询所有台帐
        List<DataDeviceDto> deviceList = copyTo(dataDeviceDao.findListByMap(param), DataDeviceDto.class);
        if (deviceList == null) {
            return null;
        }
        MultiTree multiTree = new MultiTree("id", deviceList, attrIds);
        List<ZTreeNodeDto> treeNode = multiTree.buildTree();
        return treeNode;
    }

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataDeviceDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataDevice entity = copyTo(dto, DataDevice.class);
            //判断数据是否存在
            if (dataDeviceDao.isDataYN(entity) != 0) {
                //数据存在
                dataDeviceDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataDeviceDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataDeviceDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataDevice entity = copyTo(dto, DataDevice.class);
            if (dataDeviceDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataDeviceDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataDevice entity = copyTo(dto, DataDevice.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataDeviceDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataDeviceDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataDeviceDto> findDataIsList(DataDeviceDto dto) throws Exception {
        List<DataDeviceDto> results = null;
        try {
            DataDevice entity = copyTo(dto, DataDevice.class);
            results = copyTo(dataDeviceDao.findDataIsList(entity), DataDeviceDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public List<DataDeviceDto> findListByDevice(DataDeviceDto dto) throws Exception {
        synchronized (dto) {
            List<DataDeviceDto> results = null;
            try {
                DataDevice entity = copyTo(dto, DataDevice.class);
                results = copyTo(dataDeviceDao.findListByDevice(entity), DataDeviceDto.class);
            } catch (Exception e) {
                log.error("查询异常!", e);
                throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
            }
            return results;
        }
    }

    @Override
    public List<DataDeviceDto> findListByFullName(String fullName) throws Exception {
        if (StringUtils.isEmpty(fullName)) {
            return null;
        }
        String[] dirArr = fullName.split("_");
        return findListByDevice(new DataDeviceDto() {{
            //电业局
            setElectricBureau(dirArr[0]);
            //变电站
            setSubstation(dirArr[1]);
            //设备类型
            setDeviceType(AccountDto.DeviceType.getValueByText(dirArr[2]).toString());
            //电压等级
            setVoltageLevel(dirArr[3].replaceAll("([^\\d])", ""));
            //检测日期
            setDateDetection(dirArr[4]);
            //检测技术
            setTestTechnology(dirArr[5]);
        }});
    }

    @Override
    public DataDeviceDto findDataById(DataDeviceDto dto) throws Exception {
        DataDeviceDto result = null;
        try {
            DataDevice entity = copyTo(dto, DataDevice.class);
            result = copyTo(dataDeviceDao.selectByPrimaryKey(entity), DataDeviceDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataDeviceDto findDataByTestTechnology(DataDeviceDto dto) throws Exception {
        DataDeviceDto result = null;
        try {
            DataDevice entity = copyTo(dto, DataDevice.class);
            result = copyTo(dataDeviceDao.findDataByTestTechnology(entity), DataDeviceDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataDeviceDto getSaveDataDeviceDto(DataDeviceDto dataDeviceParameterDto) throws Exception {
        //查询任务单检测结果是否已存在
        DataDeviceDto dataDeviceDto = findDataByTestTechnology(dataDeviceParameterDto);
        if (dataDeviceDto == null) {
            Response<Long> result = saveOrUpdateData(dataDeviceParameterDto);
            dataDeviceDto = dataDeviceParameterDto;
            dataDeviceDto.setId(result.getData());
        }
        return dataDeviceDto;
    }

    @Override
    public DataDeviceDto findDataIsDevice(DataDeviceDto dto) throws Exception {
        DataDeviceDto result = null;
        try {
            DataDevice entity = copyTo(dto, DataDevice.class);
            result = copyTo(dataDeviceDao.findDataIsDevice(entity), DataDeviceDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<String> findSubstationIsList() throws Exception {
        List<String> result;
        try {
            result = dataDeviceDao.findSubstationIsList();
        } catch (Exception e) {
            log.error("信息[站点信息]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataDeviceDto> findDeviceNameIsList(DataDeviceDto dto) throws Exception {
        List<DataDeviceDto> result;
        try {
            result = copyTo(dataDeviceDao.findDeviceNameIsList(copyTo(dto, DataDevice.class)), DataDeviceDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<String> findTestTechnologyIsList(DataDeviceDto dto) throws Exception {
        List<String> result;
        try {
            result = dataDeviceDao.findTestTechnologyIsList(copyTo(dto, DataDevice.class));
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

}