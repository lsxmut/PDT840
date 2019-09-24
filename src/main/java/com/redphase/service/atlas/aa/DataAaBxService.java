package com.redphase.service.atlas.aa;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.aa.IDataAaBxService;
import com.redphase.dao.master.atlas.aa.IDataAaBxDao;
import com.redphase.dto.atlas.aa.DataAaBxDto;
import com.redphase.entity.atlas.aa.DataAaBx;
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

/**
 * <p>接触式超声波_波形图谱 业务处理实现类。
 */
@Service
@Slf4j
public class DataAaBxService extends AtlasService implements IDataAaBxService {
    @Autowired
    private IDataAaBxDao dataAaBxDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAaBxDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaBx entity = copyTo(dto, DataAaBx.class);
            //判断数据是否存在
            if (dataAaBxDao.isDataYN(entity) != 0) {
                //数据存在
                dataAaBxDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAaBxDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAaBxDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaBx entity = copyTo(dto, DataAaBx.class);
            if (dataAaBxDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAaBxDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaBx entity = copyTo(dto, DataAaBx.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAaBxDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAaBxDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAaBxDto> findDataIsList(DataAaBxDto dto) throws Exception {
        List<DataAaBxDto> results = null;
        try {
            DataAaBx entity = copyTo(dto, DataAaBx.class);
            results = copyTo(dataAaBxDao.findDataIsList(entity), DataAaBxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAaBxDto findDataById(DataAaBxDto dto) throws Exception {
        DataAaBxDto result = null;
        try {
            DataAaBx entity = copyTo(dto, DataAaBx.class);
            result = copyTo(dataAaBxDao.selectByPrimaryKey(entity), DataAaBxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}