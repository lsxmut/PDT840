package com.redphase.service.nda.aa;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.aa.INdaAaFxService;
import com.redphase.dao.slave.nda.aa.INdaAaFxDao;
import com.redphase.dto.atlas.aa.DataAaFxDto;
import com.redphase.entity.atlas.aa.DataAaFx;
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
public class NdaAaFxService extends BaseService implements INdaAaFxService {
    @Autowired
    private INdaAaFxDao dataAaFxDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAaFxDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaFx entity = copyTo(dto, DataAaFx.class);
            //判断数据是否存在
            if (dataAaFxDao.isDataYN(entity) != 0) {
                //数据存在
                dataAaFxDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAaFxDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAaFxDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaFx entity = copyTo(dto, DataAaFx.class);
            if (dataAaFxDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAaFxDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaFx entity = copyTo(dto, DataAaFx.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAaFxDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAaFxDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAaFxDto> findDataIsList(DataAaFxDto dto) throws Exception {
        List<DataAaFxDto> results = null;
        try {
            DataAaFx entity = copyTo(dto, DataAaFx.class);
            results = copyTo(dataAaFxDao.findDataIsList(entity), DataAaFxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAaFxDto findDataById(DataAaFxDto dto) throws Exception {
        DataAaFxDto result = null;
        try {
            DataAaFx entity = copyTo(dto, DataAaFx.class);
            result = copyTo(dataAaFxDao.selectByPrimaryKey(entity), DataAaFxDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}