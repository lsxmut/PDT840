package com.redphase.service.nda;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.nda.INdaHjService;
import com.redphase.dao.slave.nda.INdaHjDao;
import com.redphase.dto.atlas.DataHjDto;
import com.redphase.entity.atlas.DataHj;
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
import java.util.Map;

/**
 * <p>环境参数数据 业务处理实现类。
 */
@Service
@Slf4j
public class NdaHjService extends BaseService implements INdaHjService {
    @Autowired
    private INdaHjDao dataHjDao;

    @Override
    //@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(DataHjDto dto) throws Exception {
        Response result = new Response(0, "success");
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHj entity = copyTo(dto, DataHj.class);
            //判断数据是否存在
            if (dataHjDao.isDataYN(entity) != 0) {
                //数据存在
                dataHjDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                dataHjDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(DataHjDto dto) throws Exception {
        String result = "success";
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHj entity = copyTo(dto, DataHj.class);
            if (dataHjDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }


    @Override
    public PageInfo findDataIsPage(DataHjDto dto) throws Exception {
        PageInfo pageInfo = null;
        try {
            if (dto == null) {
                throw new RuntimeException(I18nUtil.get("error.paramError"));
            }
            DataHj entity = copyTo(dto, DataHj.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = dataHjDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), DataHjDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<DataHjDto> findDataIsList(DataHjDto dto) throws Exception {
        List<DataHjDto> results = null;
        try {
            DataHj entity = copyTo(dto, DataHj.class);
            results = copyTo(dataHjDao.findDataIsList(entity), DataHjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public DataHjDto findDataById(DataHjDto dto) throws Exception {
        DataHjDto result = null;
        try {
            DataHj entity = copyTo(dto, DataHj.class);
            result = copyTo(dataHjDao.selectByPrimaryKey(entity), DataHjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String getWeather(Integer code) {
        switch (code) {
            case 0x01:
                return "晴";
            case 0x02:
                return "阴";
            case 0x03:
                return "雨";
            case 0x04:
                return "雪";
            case 0x05:
                return "雾";
            case 0x06:
                return "雷雨";
            case 0x07:
                return "多云";
            default:
                return "未记录";
        }
    }


    public DataHjDto findDataByDevice(Map paramMap) throws Exception {
        DataHjDto results = null;
        try {
            results = copyTo(dataHjDao.findDataByDevice(paramMap), DataHjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    public List<DataHjDto> findListByDevice(Map paramMap) throws Exception {
        List<DataHjDto> results = null;
        try {
            results = copyTo(dataHjDao.findListByDevice(paramMap), DataHjDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

}