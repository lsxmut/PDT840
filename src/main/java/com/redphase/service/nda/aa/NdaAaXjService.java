package com.redphase.service.nda.aa;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.aa.INdaAaXjService;
import com.redphase.dao.slave.nda.aa.INdaAaXjDao;
import com.redphase.dto.atlas.aa.DataAaXjDto;
import com.redphase.entity.atlas.aa.DataAaXj;
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
import java.util.Map;

/**
 * <p>空气式超声波_巡检 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAaXjService extends BaseService implements INdaAaXjService {
    @Autowired
    private INdaAaXjDao dataAaXjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAaXjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaXj entity = copyTo(dto, DataAaXj.class);
            //判断数据是否存在
            if (dataAaXjDao.isDataYN(entity) != 0) {
                //数据存在
                dataAaXjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAaXjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAaXjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaXj entity = copyTo(dto, DataAaXj.class);
            if (dataAaXjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAaXjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaXj entity = copyTo(dto, DataAaXj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAaXjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAaXjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAaXjDto> findDataIsList(DataAaXjDto dto) throws Exception {
        List<DataAaXjDto> results = null;
        try {
            DataAaXj entity = copyTo(dto, DataAaXj.class);
            results = copyTo(dataAaXjDao.findDataIsList(entity), DataAaXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAaXjDto findDataById(DataAaXjDto dto) throws Exception {
        DataAaXjDto result = null;
        try {
            DataAaXj entity = copyTo(dto, DataAaXj.class);
            result = copyTo(dataAaXjDao.selectByPrimaryKey(entity), DataAaXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataAaXjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataAaXjDao.findDataByIds(ids), DataAaXjDto.class);
    }

    @Override
    public DataAaXjDto findDataMaxByMap(Map map) throws Exception {
        DataAaXjDto result = null;
        try {
            result = copyTo(dataAaXjDao.findDataMaxByMap(map), DataAaXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }
}