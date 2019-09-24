package com.redphase.service.atlas;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.IDataDeviceSiteService;
import com.redphase.dao.master.atlas.IDataDeviceSiteDao;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.entity.atlas.DataDeviceSite;
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

/**
 * <p>检查点 业务处理实现类。
 */
@Service
@Slf4j
public class DataDeviceSiteService extends BaseService implements IDataDeviceSiteService {
    @Autowired
    private IDataDeviceSiteDao dataDeviceSiteDao;

    @Override
    ////@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataDeviceSiteDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataDeviceSite entity = copyTo(dto, DataDeviceSite.class);
            //判断数据是否存在
            if (dataDeviceSiteDao.isDataYN(entity) != 0) {
                //数据存在
                dataDeviceSiteDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataDeviceSiteDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataDeviceSiteDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataDeviceSite entity = copyTo(dto, DataDeviceSite.class);
            if (dataDeviceSiteDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataDeviceSiteDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataDeviceSite entity = copyTo(dto, DataDeviceSite.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataDeviceSiteDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataDeviceSiteDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataDeviceSiteDto> findDataIsList(DataDeviceSiteDto dto) throws Exception {
        List<DataDeviceSiteDto> results = null;
        try {
            DataDeviceSite entity = copyTo(dto, DataDeviceSite.class);
            results = copyTo(dataDeviceSiteDao.findDataIsList(entity), DataDeviceSiteDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataDeviceSiteDto findDataById(DataDeviceSiteDto dto) throws Exception {
        DataDeviceSiteDto result = null;
        try {
            DataDeviceSite entity = copyTo(dto, DataDeviceSite.class);
            result = copyTo(dataDeviceSiteDao.selectByPrimaryKey(entity), DataDeviceSiteDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataDeviceSiteDto findDataByDeviceAndSite(DataDeviceSiteDto dto) throws Exception {
        DataDeviceSiteDto result = null;
        try {
            DataDeviceSite entity = copyTo(dto, DataDeviceSite.class);
            result = copyTo(dataDeviceSiteDao.findDataByDeviceAndSite(entity), DataDeviceSiteDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataDeviceSiteDto getSaveDataDeviceSiteDto(DataDeviceSiteDto dataDeviceSiteParameterDto) throws Exception {
        DataDeviceSiteDto dataDeviceSiteDto = findDataByDeviceAndSite(dataDeviceSiteParameterDto);
        if (dataDeviceSiteDto == null) {
            Response<Long> result = saveOrUpdateData(dataDeviceSiteParameterDto);
            dataDeviceSiteDto = dataDeviceSiteParameterDto;
            dataDeviceSiteDto.setId(result.getData());
        }
        return dataDeviceSiteDto;
    }

    public List<String> findSiteNameIsList(DataDeviceSiteDto dto) throws Exception {
        List<String> result;
        try {
            result = dataDeviceSiteDao.findSiteNameIsList(copyTo(dto, DataDeviceSite.class));
        } catch (Exception e) {
            log.error("信息[站点信息]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataDeviceSiteDto> findDataByDataDeviceIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataDeviceSiteDao.findDataByDataDeviceIds(ids), DataDeviceSiteDto.class);
    }
}