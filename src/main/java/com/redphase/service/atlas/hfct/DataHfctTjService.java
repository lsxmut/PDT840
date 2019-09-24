package com.redphase.service.atlas.hfct;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.hfct.IDataHfctTjService;
import com.redphase.dao.master.atlas.hfct.IDataHfctTjDao;
import com.redphase.dto.atlas.hfct.DataHfctTjDto;
import com.redphase.entity.atlas.hfct.DataHfctTj;
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
 * <p>高频电流_统计数据 业务处理实现类。
 */
@Service
@Slf4j
public class DataHfctTjService extends AtlasService implements IDataHfctTjService {
    @Autowired
    private IDataHfctTjDao dataHfctTjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataHfctTjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTj entity = copyTo(dto, DataHfctTj.class);
            //判断数据是否存在
            if (dataHfctTjDao.isDataYN(entity) != 0) {
                //数据存在
                dataHfctTjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataHfctTjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataHfctTjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTj entity = copyTo(dto, DataHfctTj.class);
            if (dataHfctTjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataHfctTjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHfctTj entity = copyTo(dto, DataHfctTj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataHfctTjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataHfctTjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataHfctTjDto> findDataIsList(DataHfctTjDto dto) throws Exception {
        List<DataHfctTjDto> results = null;
        try {
            DataHfctTj entity = copyTo(dto, DataHfctTj.class);
            results = copyTo(dataHfctTjDao.findDataIsList(entity), DataHfctTjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataHfctTjDto findDataById(DataHfctTjDto dto) throws Exception {
        DataHfctTjDto result = null;
        try {
            DataHfctTj entity = copyTo(dto, DataHfctTj.class);
            result = copyTo(dataHfctTjDao.selectByPrimaryKey(entity), DataHfctTjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataHfctTjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataHfctTjDao.findDataByIds(ids), DataHfctTjDto.class);
    }


}