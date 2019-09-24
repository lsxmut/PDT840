package com.redphase.service.nda.aa;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.aa.INdaAaTjlbService;
import com.redphase.dao.slave.nda.aa.INdaAaTjlbDao;
import com.redphase.dto.atlas.aa.DataAaTjlbDto;
import com.redphase.entity.atlas.aa.DataAaTjlb;
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
 * <p>空气式超声波_统计录波 业务处理实现类。
 */
@Service
@Slf4j
public class NdaAaTjlbService extends BaseService implements INdaAaTjlbService {
    @Autowired
    private INdaAaTjlbDao dataAaTjlbDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataAaTjlbDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaTjlb entity = copyTo(dto, DataAaTjlb.class);
            //判断数据是否存在
            if (dataAaTjlbDao.isDataYN(entity) != 0) {
                //数据存在
                dataAaTjlbDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataAaTjlbDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataAaTjlbDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaTjlb entity = copyTo(dto, DataAaTjlb.class);
            if (dataAaTjlbDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataAaTjlbDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataAaTjlb entity = copyTo(dto, DataAaTjlb.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataAaTjlbDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataAaTjlbDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataAaTjlbDto> findDataIsList(DataAaTjlbDto dto) throws Exception {
        List<DataAaTjlbDto> results = null;
        try {
            DataAaTjlb entity = copyTo(dto, DataAaTjlb.class);
            results = copyTo(dataAaTjlbDao.findDataIsList(entity), DataAaTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataAaTjlbDto findDataById(DataAaTjlbDto dto) throws Exception {
        DataAaTjlbDto result = null;
        try {
            DataAaTjlb entity = copyTo(dto, DataAaTjlb.class);
            result = copyTo(dataAaTjlbDao.selectByPrimaryKey(entity), DataAaTjlbDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


}