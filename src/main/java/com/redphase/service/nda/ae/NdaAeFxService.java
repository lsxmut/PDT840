package com.redphase.service.nda.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.ae.INdaAeFxService;
import com.redphase.dao.slave.nda.ae.INdaAeFxDao;
import com.redphase.dto.atlas.ae.DataAeFxDto;
import com.redphase.entity.atlas.ae.DataAeFx;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>接触式超声波_飞行图谱 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAeFxService extends BaseService implements INdaAeFxService {
    @Autowired
    private INdaAeFxDao dataAeFxDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeFxDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeFx entity = copyTo(dto, DataAeFx.class);
            //判断数据是否存在
            if (dataAeFxDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeFxDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeFxDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeFxDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeFx entity = copyTo(dto, DataAeFx.class);
            if (dataAeFxDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeFxDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeFx entity = copyTo(dto, DataAeFx.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeFxDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeFxDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeFxDto> findDataIsList(DataAeFxDto dto) throws Exception {
        List<DataAeFxDto> results = null;
        try {
            DataAeFx entity = copyTo(dto, DataAeFx.class);
            results = copyTo(dataAeFxDao.findDataIsList(entity), DataAeFxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeFxDto findDataById(DataAeFxDto dto) throws Exception {
        DataAeFxDto result = null;
        try {
            DataAeFx entity = copyTo(dto, DataAeFx.class);
            result = copyTo(dataAeFxDao.selectByPrimaryKey(entity), DataAeFxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}