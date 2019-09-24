package com.redphase.service.nda.uhf;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.uhf.INdaUhfTjlb2Service;
import com.redphase.dao.slave.nda.uhf.INdaUhfTjlb2Dao;
import com.redphase.dto.atlas.uhf.DataUhfTjlb2Dto;
import com.redphase.entity.atlas.uhf.DataUhfTjlb2;
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
 * <p>特高频_模式2_统计录波 业务处理实现类。
 */
@Service
@Slf4j
public class NdaUhfTjlb2Service extends BaseService implements INdaUhfTjlb2Service {
    @Autowired
    private INdaUhfTjlb2Dao dataUhfTjlb2Dao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataUhfTjlb2Dto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjlb2 entity = copyTo(dto, DataUhfTjlb2.class);
            //判断数据是否存在
            if (dataUhfTjlb2Dao.isDataYN(entity) != 0) {
                //数据存在
                dataUhfTjlb2Dao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataUhfTjlb2Dao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataUhfTjlb2Dto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjlb2 entity = copyTo(dto, DataUhfTjlb2.class);
            if (dataUhfTjlb2Dao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataUhfTjlb2Dto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjlb2 entity = copyTo(dto, DataUhfTjlb2.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataUhfTjlb2Dao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataUhfTjlb2Dto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataUhfTjlb2Dto> findDataIsList(DataUhfTjlb2Dto dto) throws Exception {
        List<DataUhfTjlb2Dto> results = null;
        try {
            DataUhfTjlb2 entity = copyTo(dto, DataUhfTjlb2.class);
            results = copyTo(dataUhfTjlb2Dao.findDataIsList(entity), DataUhfTjlb2Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataUhfTjlb2Dto findDataById(DataUhfTjlb2Dto dto) throws Exception {
        DataUhfTjlb2Dto result = null;
        try {
            DataUhfTjlb2 entity = copyTo(dto, DataUhfTjlb2.class);
            result = copyTo(dataUhfTjlb2Dao.selectByPrimaryKey(entity), DataUhfTjlb2Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}