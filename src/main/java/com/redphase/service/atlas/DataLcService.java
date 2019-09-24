package com.redphase.service.atlas;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.IDataLcService;
import com.redphase.dao.master.atlas.IDataLcDao;
import com.redphase.dto.atlas.DataLcDto;
import com.redphase.entity.atlas.DataLc;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>负荷电流数据 业务处理实现类。
 */
@Service
@Slf4j
public class DataLcService extends AtlasService implements IDataLcService {
    @Autowired
    private IDataLcDao dataLcDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataLcDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataLc entity = copyTo(dto, DataLc.class);
            //判断数据是否存在
            if (dataLcDao.isDataYN(entity) != 0) {
                //数据存在
                dataLcDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataLcDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataLcDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataLc entity = copyTo(dto, DataLc.class);
            if (dataLcDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataLcDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataLc entity = copyTo(dto, DataLc.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataLcDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataLcDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataLcDto> findDataIsList(DataLcDto dto) throws Exception {
        List<DataLcDto> results = null;
        try {
            DataLc entity = copyTo(dto, DataLc.class);
            results = copyTo(dataLcDao.findDataIsList(entity), DataLcDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataLcDto findDataById(DataLcDto dto) throws Exception {
        DataLcDto result = null;
        try {
            DataLc entity = copyTo(dto, DataLc.class);
            result = copyTo(dataLcDao.selectByPrimaryKey(entity), DataLcDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public DataLcDto findDataMaxByMap(Map map) throws Exception {
        DataLcDto result = null;
        try {
            result = copyTo(dataLcDao.findDataMaxByMap(map), DataLcDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }
}