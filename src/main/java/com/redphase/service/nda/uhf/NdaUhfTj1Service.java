package com.redphase.service.nda.uhf;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.uhf.INdaUhfTj1Service;
import com.redphase.dao.slave.nda.uhf.INdaUhfTj1Dao;
import com.redphase.dto.atlas.uhf.DataUhfTj1Dto;
import com.redphase.entity.atlas.uhf.DataUhfTj1;
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
 * <p>特高频_模式1_统计数据 业务处理实现类。
 */
@Service
@Slf4j
public class NdaUhfTj1Service extends BaseService implements INdaUhfTj1Service {
    @Autowired
    private INdaUhfTj1Dao dataUhfTj1Dao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataUhfTj1Dto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTj1 entity = copyTo(dto, DataUhfTj1.class);
            //判断数据是否存在
            if (dataUhfTj1Dao.isDataYN(entity) != 0) {
                //数据存在
                dataUhfTj1Dao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataUhfTj1Dao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataUhfTj1Dto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTj1 entity = copyTo(dto, DataUhfTj1.class);
            if (dataUhfTj1Dao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataUhfTj1Dto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTj1 entity = copyTo(dto, DataUhfTj1.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataUhfTj1Dao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataUhfTj1Dto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataUhfTj1Dto> findDataIsList(DataUhfTj1Dto dto) throws Exception {
        List<DataUhfTj1Dto> results = null;
        try {
            DataUhfTj1 entity = copyTo(dto, DataUhfTj1.class);
            results = copyTo(dataUhfTj1Dao.findDataIsList(entity), DataUhfTj1Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataUhfTj1Dto findDataById(DataUhfTj1Dto dto) throws Exception {
        DataUhfTj1Dto result = null;
        try {
            DataUhfTj1 entity = copyTo(dto, DataUhfTj1.class);
            result = copyTo(dataUhfTj1Dao.selectByPrimaryKey(entity), DataUhfTj1Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataUhfTj1Dto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataUhfTj1Dao.findDataByIds(ids), DataUhfTj1Dto.class);
    }


}