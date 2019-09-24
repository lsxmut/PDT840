package com.redphase.service.nda.hfct;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.hfct.INdaHfctTjzsService;
import com.redphase.dao.slave.nda.hfct.INdaHfctTjzsDao;
import com.redphase.dto.atlas.hfct.DataHfctTjzsDto;
import com.redphase.entity.atlas.hfct.DataHfctTjzs;
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
 * <p>高频电流_统计噪声 业务处理实现类。
 */
@Service
@Slf4j
public class NdaHfctTjzsService extends BaseService implements INdaHfctTjzsService {
    @Autowired
    private INdaHfctTjzsDao dataHfctTjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataHfctTjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTjzs entity = copyTo(dto, DataHfctTjzs.class);
            //判断数据是否存在
            if (dataHfctTjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataHfctTjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataHfctTjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataHfctTjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTjzs entity = copyTo(dto, DataHfctTjzs.class);
            if (dataHfctTjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataHfctTjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTjzs entity = copyTo(dto, DataHfctTjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataHfctTjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataHfctTjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataHfctTjzsDto> findDataIsList(DataHfctTjzsDto dto) throws Exception {
        List<DataHfctTjzsDto> results = null;
        try {
            DataHfctTjzs entity = copyTo(dto, DataHfctTjzs.class);
            results = copyTo(dataHfctTjzsDao.findDataIsList(entity), DataHfctTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataHfctTjzsDto findDataById(DataHfctTjzsDto dto) throws Exception {
        DataHfctTjzsDto result = null;
        try {
            DataHfctTjzs entity = copyTo(dto, DataHfctTjzs.class);
            result = copyTo(dataHfctTjzsDao.selectByPrimaryKey(entity), DataHfctTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}