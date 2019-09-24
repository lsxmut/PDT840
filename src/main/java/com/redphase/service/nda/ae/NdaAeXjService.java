package com.redphase.service.nda.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.ae.INdaAeXjService;
import com.redphase.dao.slave.nda.ae.INdaAeXjDao;
import com.redphase.dto.atlas.ae.DataAeXjDto;
import com.redphase.entity.atlas.ae.DataAeXj;
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
 * <p>接触式超声波_巡检 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAeXjService extends BaseService implements INdaAeXjService {
    @Autowired
    private INdaAeXjDao dataAeXjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeXjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeXj entity = copyTo(dto, DataAeXj.class);
            //判断数据是否存在
            if (dataAeXjDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeXjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeXjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeXjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeXj entity = copyTo(dto, DataAeXj.class);
            if (dataAeXjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeXjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeXj entity = copyTo(dto, DataAeXj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeXjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeXjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeXjDto> findDataIsList(DataAeXjDto dto) throws Exception {
        List<DataAeXjDto> results = null;
        try {
            DataAeXj entity = copyTo(dto, DataAeXj.class);
            results = copyTo(dataAeXjDao.findDataIsList(entity), DataAeXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeXjDto findDataById(DataAeXjDto dto) throws Exception {
        DataAeXjDto result = null;
        try {
            DataAeXj entity = copyTo(dto, DataAeXj.class);
            result = copyTo(dataAeXjDao.selectByPrimaryKey(entity), DataAeXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataAeXjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataAeXjDao.findDataByIds(ids), DataAeXjDto.class);
    }


}