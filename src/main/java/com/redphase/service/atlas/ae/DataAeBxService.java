package com.redphase.service.atlas.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.ae.IDataAeBxService;
import com.redphase.dao.master.atlas.ae.IDataAeBxDao;
import com.redphase.dto.atlas.ae.DataAeBxDto;
import com.redphase.entity.atlas.ae.DataAeBx;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.service.atlas.AtlasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * <p>接触式超声波_波形图谱 业务处理实现类。
 */
@Service
@Slf4j
public class DataAeBxService extends AtlasService implements IDataAeBxService {
    @Autowired
    private IDataAeBxDao dataAeBxDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeBxDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeBx entity = copyTo(dto, DataAeBx.class);
            //判断数据是否存在
            if (dataAeBxDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeBxDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeBxDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeBxDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeBx entity = copyTo(dto, DataAeBx.class);
            if (dataAeBxDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeBxDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeBx entity = copyTo(dto, DataAeBx.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeBxDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeBxDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeBxDto> findDataIsList(DataAeBxDto dto) throws Exception {
        List<DataAeBxDto> results = null;
        try {
            DataAeBx entity = copyTo(dto, DataAeBx.class);
            results = copyTo(dataAeBxDao.findDataIsList(entity), DataAeBxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeBxDto findDataById(DataAeBxDto dto) throws Exception {
        DataAeBxDto result = null;
        try {
            DataAeBx entity = copyTo(dto, DataAeBx.class);
            result = copyTo(dataAeBxDao.selectByPrimaryKey(entity), DataAeBxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    public DataAeBxDto findDataByDevice(Map paramMap) throws Exception {
        DataAeBxDto results = null;
        try {
            results = copyTo(dataAeBxDao.findDataByDevice(paramMap), DataAeBxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

}