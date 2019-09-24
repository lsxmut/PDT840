package com.redphase.service.nda.ae;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.ae.INdaAeTjService;
import com.redphase.dao.slave.nda.ae.INdaAeTjDao;
import com.redphase.dto.atlas.ae.DataAeTjDto;
import com.redphase.entity.atlas.ae.DataAeTj;
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
 * <p>接触式超声波_统计数据 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAeTjService extends BaseService implements INdaAeTjService {
    @Autowired
    private INdaAeTjDao dataAeTjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAeTjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTj entity = copyTo(dto, DataAeTj.class);
            //判断数据是否存在
            if (dataAeTjDao.isDataYN(entity) != 0) {
                //数据存在
                dataAeTjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAeTjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAeTjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTj entity = copyTo(dto, DataAeTj.class);
            if (dataAeTjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAeTjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAeTj entity = copyTo(dto, DataAeTj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAeTjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAeTjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAeTjDto> findDataIsList(DataAeTjDto dto) throws Exception {
        List<DataAeTjDto> results = null;
        try {
            DataAeTj entity = copyTo(dto, DataAeTj.class);
            results = copyTo(dataAeTjDao.findDataIsList(entity), DataAeTjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAeTjDto findDataById(DataAeTjDto dto) throws Exception {
        DataAeTjDto result = null;
        try {
            DataAeTj entity = copyTo(dto, DataAeTj.class);
            result = copyTo(dataAeTjDao.selectByPrimaryKey(entity), DataAeTjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataAeTjDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataAeTjDao.findDataByIds(ids), DataAeTjDto.class);
    }


}