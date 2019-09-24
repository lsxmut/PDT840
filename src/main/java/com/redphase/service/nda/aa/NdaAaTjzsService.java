package com.redphase.service.nda.aa;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.aa.INdaAaTjzsService;
import com.redphase.dao.slave.nda.aa.INdaAaTjzsDao;
import com.redphase.dto.atlas.aa.DataAaTjzsDto;
import com.redphase.entity.atlas.aa.DataAaTjzs;
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
 * <p>空气式超声波_统计噪声 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAaTjzsService extends BaseService implements INdaAaTjzsService {
    @Autowired
    private INdaAaTjzsDao dataAaTjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAaTjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaTjzs entity = copyTo(dto, DataAaTjzs.class);
            //判断数据是否存在
            if (dataAaTjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataAaTjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAaTjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAaTjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaTjzs entity = copyTo(dto, DataAaTjzs.class);
            if (dataAaTjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAaTjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaTjzs entity = copyTo(dto, DataAaTjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAaTjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAaTjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAaTjzsDto> findDataIsList(DataAaTjzsDto dto) throws Exception {
        List<DataAaTjzsDto> results = null;
        try {
            DataAaTjzs entity = copyTo(dto, DataAaTjzs.class);
            results = copyTo(dataAaTjzsDao.findDataIsList(entity), DataAaTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAaTjzsDto findDataById(DataAaTjzsDto dto) throws Exception {
        DataAaTjzsDto result = null;
        try {
            DataAaTjzs entity = copyTo(dto, DataAaTjzs.class);
            result = copyTo(dataAaTjzsDao.selectByPrimaryKey(entity), DataAaTjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataAaTjzsDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataAaTjzsDao.findDataByIds(ids), DataAaTjzsDto.class);
    }

}