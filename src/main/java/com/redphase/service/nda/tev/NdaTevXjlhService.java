package com.redphase.service.nda.tev;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.tev.INdaTevXjlhService;
import com.redphase.dao.slave.nda.tev.INdaTevXjlhDao;
import com.redphase.dto.atlas.tev.DataTevXjlhDto;
import com.redphase.entity.atlas.tev.DataTevXjlh;
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
 * <p>地电波_联合巡检 业务处理实现类。
 */
@Service
@Slf4j
public class NdaTevXjlhService extends BaseService implements INdaTevXjlhService {
    @Autowired
    private INdaTevXjlhDao dataTevXjhlDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataTevXjlhDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXjlh entity = copyTo(dto, DataTevXjlh.class);
            //判断数据是否存在
            if (dataTevXjhlDao.isDataYN(entity) != 0) {
                //数据存在
                dataTevXjhlDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataTevXjhlDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataTevXjlhDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXjlh entity = copyTo(dto, DataTevXjlh.class);
            if (dataTevXjhlDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataTevXjlhDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataTevXjlh entity = copyTo(dto, DataTevXjlh.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataTevXjhlDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataTevXjlhDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataTevXjlhDto> findDataIsList(DataTevXjlhDto dto) throws Exception {
        List<DataTevXjlhDto> results = null;
        try {
            DataTevXjlh entity = copyTo(dto, DataTevXjlh.class);
            results = copyTo(dataTevXjhlDao.findDataIsList(entity), DataTevXjlhDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataTevXjlhDto findDataById(DataTevXjlhDto dto) throws Exception {
        DataTevXjlhDto result = null;
        try {
            DataTevXjlh entity = copyTo(dto, DataTevXjlh.class);
            result = copyTo(dataTevXjhlDao.selectByPrimaryKey(entity), DataTevXjlhDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public List<DataTevXjlhDto> findDataByIds(String ids) throws Exception {
        if (StringUtils.isEmpty(ids)) {
            return new ArrayList<>();
        }
        return copyTo(dataTevXjhlDao.findDataByIds(ids), DataTevXjlhDto.class);
    }
}