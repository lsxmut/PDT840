package com.redphase.service.nda;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.INdaAnalyzeService;
import com.redphase.dao.slave.nda.INdaAnalyzeDao;
import com.redphase.dto.atlas.DataAnalyzeDto;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.entity.atlas.DataAnalyze;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>检测文件 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAnalyzeService extends BaseService implements INdaAnalyzeService {
    @Autowired
    private INdaAnalyzeDao dataAnalyzeDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAnalyzeDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAnalyze entity = copyTo(dto, DataAnalyze.class);
            //判断数据是否存在
            if (dataAnalyzeDao.isDataYN(entity) != 0) {
                //数据存在
                dataAnalyzeDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAnalyzeDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAnalyzeDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAnalyze entity = copyTo(dto, DataAnalyze.class);
            if (dataAnalyzeDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAnalyzeDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAnalyze entity = copyTo(dto, DataAnalyze.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAnalyzeDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAnalyzeDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAnalyzeDto> findDataIsList(DataAnalyzeDto dto) throws Exception {
        List<DataAnalyzeDto> results = null;
        try {
            DataAnalyze entity = copyTo(dto, DataAnalyze.class);
            results = copyTo(dataAnalyzeDao.findDataIsList(entity), DataAnalyzeDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAnalyzeDto findDataById(DataAnalyzeDto dto) throws Exception {
        DataAnalyzeDto result = null;
        try {
            DataAnalyze entity = copyTo(dto, DataAnalyze.class);
            result = copyTo(dataAnalyzeDao.selectByPrimaryKey(entity), DataAnalyzeDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataAnalyzeDto findDataByDataSource(DataAnalyzeDto dto) throws Exception {
        DataAnalyzeDto result = null;
        try {
            DataAnalyze entity = copyTo(dto, DataAnalyze.class);
            result = copyTo(dataAnalyzeDao.findDataByDataSource(entity), DataAnalyzeDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataAnalyzeDto getSaveDataAnalyzeDto(DataAnalyzeDto dataAnalyzeParameterDto, DataDeviceDto dataDeviceDto, DataDeviceSiteDto dataDeviceSiteDto) throws Exception {
        DataAnalyzeDto dataAnalyzeDto = findDataByDataSource(dataAnalyzeParameterDto);
        if (dataAnalyzeDto == null) {
            Response<Long> result = saveOrUpdateData(dataAnalyzeParameterDto);
            dataAnalyzeDto = dataAnalyzeParameterDto;
            dataAnalyzeDto.setId(result.getData());
        }
        return dataAnalyzeDto;
    }

    @Override
    public List<DataAnalyzeDto> findDataByDataDeviceIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataAnalyzeDao.findDataByDataDeviceIds(ids), DataAnalyzeDto.class);
    }

    public List<DataAnalyzeDto> thendSearch(Map map) throws Exception {
        List<DataAnalyzeDto> results = null;
        try {
            results = copyTo(dataAnalyzeDao.thendSearch(map), DataAnalyzeDto.class);
            for (DataAnalyzeDto analyzeDto : results) {
                String fileUrl = analyzeDto.getFileUrl();
                if (!StringUtils.isEmpty(fileUrl)) {
                    analyzeDto.setFileName(fileUrl.substring(fileUrl.lastIndexOf("\\") + 1, fileUrl.length()));
                }
            }
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    public List<DataAnalyzeDto> findListByDevice(Map map) throws Exception {
        List<DataAnalyzeDto> results = null;
        try {
            results = copyTo(dataAnalyzeDao.findListByDevice(map), DataAnalyzeDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }
}