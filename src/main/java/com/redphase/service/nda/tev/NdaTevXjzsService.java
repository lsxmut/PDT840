package com.redphase.service.nda.tev;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.tev.INdaTevXjzsService;
import com.redphase.dao.slave.nda.tev.INdaTevXjzsDao;
import com.redphase.dto.atlas.tev.DataTevXjzsDto;
import com.redphase.entity.atlas.tev.DataTevXjzs;
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
 * <p>地电波_巡检噪声 业务处理实现类。
 */
@Service
@Slf4j
public class NdaTevXjzsService extends BaseService implements INdaTevXjzsService {
    @Autowired
    private INdaTevXjzsDao dataTevXjzsDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataTevXjzsDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXjzs entity = copyTo(dto, DataTevXjzs.class);
            //判断数据是否存在
            if (dataTevXjzsDao.isDataYN(entity) != 0) {
                //数据存在
                dataTevXjzsDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataTevXjzsDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataTevXjzsDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXjzs entity = copyTo(dto, DataTevXjzs.class);
            if (dataTevXjzsDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataTevXjzsDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXjzs entity = copyTo(dto, DataTevXjzs.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataTevXjzsDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataTevXjzsDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataTevXjzsDto> findDataIsList(DataTevXjzsDto dto) throws Exception {
        List<DataTevXjzsDto> results = null;
        try {
            DataTevXjzs entity = copyTo(dto, DataTevXjzs.class);
            results = copyTo(dataTevXjzsDao.findDataIsList(entity), DataTevXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataTevXjzsDto findDataById(DataTevXjzsDto dto) throws Exception {
        DataTevXjzsDto result = null;
        try {
            DataTevXjzs entity = copyTo(dto, DataTevXjzs.class);
            result = copyTo(dataTevXjzsDao.selectByPrimaryKey(entity), DataTevXjzsDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<DataTevXjzsDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataTevXjzsDao.findDataByIds(ids), DataTevXjzsDto.class);
    }
}