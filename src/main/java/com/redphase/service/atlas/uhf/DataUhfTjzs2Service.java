package com.redphase.service.atlas.uhf;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.uhf.IDataUhfTjzs2Service;
import com.redphase.dao.master.atlas.uhf.IDataUhfTjzs2Dao;
import com.redphase.dto.atlas.uhf.DataUhfTjzs2Dto;
import com.redphase.entity.atlas.uhf.DataUhfTjzs2;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.service.atlas.AtlasService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>特高频_模式2_统计噪声 业务处理实现类。
 */
@Service
@Slf4j
public class DataUhfTjzs2Service extends AtlasService implements IDataUhfTjzs2Service {
    @Autowired
    private IDataUhfTjzs2Dao dataUhfTjzs2Dao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataUhfTjzs2Dto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjzs2 entity = copyTo(dto, DataUhfTjzs2.class);
            //判断数据是否存在
            if (dataUhfTjzs2Dao.isDataYN(entity) != 0) {
                //数据存在
                dataUhfTjzs2Dao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataUhfTjzs2Dao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataUhfTjzs2Dto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjzs2 entity = copyTo(dto, DataUhfTjzs2.class);
            if (dataUhfTjzs2Dao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataUhfTjzs2Dto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTjzs2 entity = copyTo(dto, DataUhfTjzs2.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataUhfTjzs2Dao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataUhfTjzs2Dto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataUhfTjzs2Dto> findDataIsList(DataUhfTjzs2Dto dto) throws Exception {
        List<DataUhfTjzs2Dto> results = null;
        try {
            DataUhfTjzs2 entity = copyTo(dto, DataUhfTjzs2.class);
            results = copyTo(dataUhfTjzs2Dao.findDataIsList(entity), DataUhfTjzs2Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataUhfTjzs2Dto findDataById(DataUhfTjzs2Dto dto) throws Exception {
        DataUhfTjzs2Dto result = null;
        try {
            DataUhfTjzs2 entity = copyTo(dto, DataUhfTjzs2.class);
            result = copyTo(dataUhfTjzs2Dao.selectByPrimaryKey(entity), DataUhfTjzs2Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}