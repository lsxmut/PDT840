package com.redphase.service.atlas.aa;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.atlas.aa.IDataAaXjzsService;
import com.redphase.dao.master.atlas.aa.IDataAaXjzsDao;
import com.redphase.dto.atlas.aa.DataAaXjzsDto;
import com.redphase.entity.atlas.aa.DataAaXjzs;
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
 * <p>空气式超声波_巡检噪声 业务处理实现类。
 */
@Service
@Slf4j
public class DataAaXjzsService extends AtlasService implements IDataAaXjzsService {
    @Autowired
    private IDataAaXjzsDao dataAaXjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAaXjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaXjzs entity = copyTo(dto, DataAaXjzs.class);
            //判断数据是否存在
            if (dataAaXjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataAaXjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAaXjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAaXjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaXjzs entity = copyTo(dto, DataAaXjzs.class);
            if (dataAaXjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAaXjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaXjzs entity = copyTo(dto, DataAaXjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAaXjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAaXjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAaXjzsDto> findDataIsList(DataAaXjzsDto dto) throws Exception {
        List<DataAaXjzsDto> results = null;
        try {
            DataAaXjzs entity = copyTo(dto, DataAaXjzs.class);
            results = copyTo(dataAaXjzsDao.findDataIsList(entity), DataAaXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAaXjzsDto findDataById(DataAaXjzsDto dto) throws Exception {
        DataAaXjzsDto result = null;
        try {
            DataAaXjzs entity = copyTo(dto, DataAaXjzs.class);
            result = copyTo(dataAaXjzsDao.selectByPrimaryKey(entity), DataAaXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataAaXjzsDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataAaXjzsDao.findDataByIds(ids), DataAaXjzsDto.class);
    }

}