package com.redphase.service.nda.hfct;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.hfct.INdaHfctXjService;
import com.redphase.dao.slave.nda.hfct.INdaHfctXjDao;
import com.redphase.dto.atlas.hfct.DataHfctXjDto;
import com.redphase.entity.atlas.hfct.DataHfctXj;
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
 * <p>高频电流_巡检 业务处理实现类。
 */
@Service
@Slf4j
public class NdaHfctXjService extends BaseService implements INdaHfctXjService {
    @Autowired
    private INdaHfctXjDao dataHfctXjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataHfctXjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctXj entity = copyTo(dto, DataHfctXj.class);
            //判断数据是否存在
            if (dataHfctXjDao.isDataYN(entity) != 0) {
                //数据存在
                dataHfctXjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataHfctXjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataHfctXjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctXj entity = copyTo(dto, DataHfctXj.class);
            if (dataHfctXjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataHfctXjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctXj entity = copyTo(dto, DataHfctXj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataHfctXjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataHfctXjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataHfctXjDto> findDataIsList(DataHfctXjDto dto) throws Exception {
        List<DataHfctXjDto> results = null;
        try {
            DataHfctXj entity = copyTo(dto, DataHfctXj.class);
            results = copyTo(dataHfctXjDao.findDataIsList(entity), DataHfctXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataHfctXjDto findDataById(DataHfctXjDto dto) throws Exception {
        DataHfctXjDto result = null;
        try {
            DataHfctXj entity = copyTo(dto, DataHfctXj.class);
            result = copyTo(dataHfctXjDao.selectByPrimaryKey(entity), DataHfctXjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataHfctXjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataHfctXjDao.findDataByIds(ids), DataHfctXjDto.class);
    }


}