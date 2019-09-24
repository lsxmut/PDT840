package com.redphase.service.atlas.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.ae.IDataAeXjzsService;
import com.redphase.dao.master.atlas.ae.IDataAeXjzsDao;
import com.redphase.dto.atlas.ae.DataAeXjzsDto;
import com.redphase.entity.atlas.ae.DataAeXjzs;
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
 * <p>接触式超声波_巡检噪声 业务处理实现类。
 */
@Service
@Slf4j
public class DataAeXjzsService extends AtlasService implements IDataAeXjzsService {
    @Autowired
    private IDataAeXjzsDao dataAeXjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeXjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeXjzs entity = copyTo(dto, DataAeXjzs.class);
            //判断数据是否存在
            if (dataAeXjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeXjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeXjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeXjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeXjzs entity = copyTo(dto, DataAeXjzs.class);
            if (dataAeXjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeXjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeXjzs entity = copyTo(dto, DataAeXjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeXjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeXjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeXjzsDto> findDataIsList(DataAeXjzsDto dto) throws Exception {
        List<DataAeXjzsDto> results = null;
        try {
            DataAeXjzs entity = copyTo(dto, DataAeXjzs.class);
            results = copyTo(dataAeXjzsDao.findDataIsList(entity), DataAeXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeXjzsDto findDataById(DataAeXjzsDto dto) throws Exception {
        DataAeXjzsDto result = null;
        try {
            DataAeXjzs entity = copyTo(dto, DataAeXjzs.class);
            result = copyTo(dataAeXjzsDao.selectByPrimaryKey(entity), DataAeXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}