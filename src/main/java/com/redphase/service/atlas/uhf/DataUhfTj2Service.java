package com.redphase.service.atlas.uhf;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.uhf.IDataUhfTj2Service;
import com.redphase.dao.master.atlas.uhf.IDataUhfTj2Dao;
import com.redphase.dto.atlas.uhf.DataUhfTj2Dto;
import com.redphase.entity.atlas.uhf.DataUhfTj2;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.service.atlas.AtlasService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>特高频_模式2_统计数据 业务处理实现类。
 */
@Service
@Slf4j
public class DataUhfTj2Service extends AtlasService implements IDataUhfTj2Service {
    @Autowired
    private IDataUhfTj2Dao dataUhfTj2Dao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataUhfTj2Dto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTj2 entity = copyTo(dto, DataUhfTj2.class);
            //判断数据是否存在
            if (dataUhfTj2Dao.isDataYN(entity) != 0) {
                //数据存在
                dataUhfTj2Dao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataUhfTj2Dao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataUhfTj2Dto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTj2 entity = copyTo(dto, DataUhfTj2.class);
            if (dataUhfTj2Dao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataUhfTj2Dto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataUhfTj2 entity = copyTo(dto, DataUhfTj2.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataUhfTj2Dao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataUhfTj2Dto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataUhfTj2Dto> findDataIsList(DataUhfTj2Dto dto) throws Exception {
        List<DataUhfTj2Dto> results = null;
        try {
            DataUhfTj2 entity = copyTo(dto, DataUhfTj2.class);
            results = copyTo(dataUhfTj2Dao.findDataIsList(entity), DataUhfTj2Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataUhfTj2Dto findDataById(DataUhfTj2Dto dto) throws Exception {
        DataUhfTj2Dto result = null;
        try {
            DataUhfTj2 entity = copyTo(dto, DataUhfTj2.class);
            result = copyTo(dataUhfTj2Dao.selectByPrimaryKey(entity), DataUhfTj2Dto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataUhfTj2Dto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataUhfTj2Dao.findDataByIds(ids), DataUhfTj2Dto.class);
    }


}