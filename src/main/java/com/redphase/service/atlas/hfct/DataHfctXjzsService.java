package com.redphase.service.atlas.hfct;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.hfct.IDataHfctXjzsService;
import com.redphase.dao.master.atlas.hfct.IDataHfctXjzsDao;
import com.redphase.dto.atlas.hfct.DataHfctXjzsDto;
import com.redphase.entity.atlas.hfct.DataHfctXjzs;
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
 * <p>高频电流_巡检噪声 业务处理实现类。
 */
@Service
@Slf4j
public class DataHfctXjzsService extends AtlasService implements IDataHfctXjzsService {
    @Autowired
    private IDataHfctXjzsDao dataHfctXjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataHfctXjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctXjzs entity = copyTo(dto, DataHfctXjzs.class);
            //判断数据是否存在
            if (dataHfctXjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataHfctXjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataHfctXjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataHfctXjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctXjzs entity = copyTo(dto, DataHfctXjzs.class);
            if (dataHfctXjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataHfctXjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctXjzs entity = copyTo(dto, DataHfctXjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataHfctXjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataHfctXjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataHfctXjzsDto> findDataIsList(DataHfctXjzsDto dto) throws Exception {
        List<DataHfctXjzsDto> results = null;
        try {
            DataHfctXjzs entity = copyTo(dto, DataHfctXjzs.class);
            results = copyTo(dataHfctXjzsDao.findDataIsList(entity), DataHfctXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataHfctXjzsDto findDataById(DataHfctXjzsDto dto) throws Exception {
        DataHfctXjzsDto result = null;
        try {
            DataHfctXjzs entity = copyTo(dto, DataHfctXjzs.class);
            result = copyTo(dataHfctXjzsDao.selectByPrimaryKey(entity), DataHfctXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}