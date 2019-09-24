package com.redphase.service.nda.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.ae.INdaAeTjzsService;
import com.redphase.dao.slave.nda.ae.INdaAeTjzsDao;
import com.redphase.dto.atlas.ae.DataAeTjzsDto;
import com.redphase.entity.atlas.ae.DataAeTjzs;
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
 * <p>接触式超声波_统计噪声 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAeTjzsService extends BaseService implements INdaAeTjzsService {
    @Autowired
    private INdaAeTjzsDao dataAeTjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeTjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTjzs entity = copyTo(dto, DataAeTjzs.class);
            //判断数据是否存在
            if (dataAeTjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeTjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeTjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeTjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTjzs entity = copyTo(dto, DataAeTjzs.class);
            if (dataAeTjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeTjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTjzs entity = copyTo(dto, DataAeTjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeTjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeTjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeTjzsDto> findDataIsList(DataAeTjzsDto dto) throws Exception {
        List<DataAeTjzsDto> results = null;
        try {
            DataAeTjzs entity = copyTo(dto, DataAeTjzs.class);
            results = copyTo(dataAeTjzsDao.findDataIsList(entity), DataAeTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeTjzsDto findDataById(DataAeTjzsDto dto) throws Exception {
        DataAeTjzsDto result = null;
        try {
            DataAeTjzs entity = copyTo(dto, DataAeTjzs.class);
            result = copyTo(dataAeTjzsDao.selectByPrimaryKey(entity), DataAeTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}