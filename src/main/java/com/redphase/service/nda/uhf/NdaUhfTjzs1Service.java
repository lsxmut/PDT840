package com.redphase.service.nda.uhf;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.uhf.INdaUhfTjzs1Service;
import com.redphase.dao.slave.nda.uhf.INdaUhfTjzs1Dao;
import com.redphase.dto.atlas.uhf.DataUhfTjzs1Dto;
import com.redphase.entity.atlas.uhf.DataUhfTjzs1;
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
 * <p>特高频_模式1_统计噪声 业务处理实现类。
 */
@Service
@Slf4j
public class NdaUhfTjzs1Service extends BaseService implements INdaUhfTjzs1Service {
    @Autowired
    private INdaUhfTjzs1Dao dataUhfTjzs1Dao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataUhfTjzs1Dto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjzs1 entity = copyTo(dto, DataUhfTjzs1.class);
            //判断数据是否存在
            if (dataUhfTjzs1Dao.isDataYN(entity) != 0) {
                //数据存在
                dataUhfTjzs1Dao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataUhfTjzs1Dao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataUhfTjzs1Dto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjzs1 entity = copyTo(dto, DataUhfTjzs1.class);
            if (dataUhfTjzs1Dao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataUhfTjzs1Dto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjzs1 entity = copyTo(dto, DataUhfTjzs1.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataUhfTjzs1Dao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataUhfTjzs1Dto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataUhfTjzs1Dto> findDataIsList(DataUhfTjzs1Dto dto) throws Exception {
        List<DataUhfTjzs1Dto> results = null;
        try {
            DataUhfTjzs1 entity = copyTo(dto, DataUhfTjzs1.class);
            results = copyTo(dataUhfTjzs1Dao.findDataIsList(entity), DataUhfTjzs1Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataUhfTjzs1Dto findDataById(DataUhfTjzs1Dto dto) throws Exception {
        DataUhfTjzs1Dto result = null;
        try {
            DataUhfTjzs1 entity = copyTo(dto, DataUhfTjzs1.class);
            result = copyTo(dataUhfTjzs1Dao.selectByPrimaryKey(entity), DataUhfTjzs1Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}