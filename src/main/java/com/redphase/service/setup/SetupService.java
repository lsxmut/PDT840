package com.redphase.service.setup;

import com.redphase.api.setup.ISetupService;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.framework.annotation.ALogOperation;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.CommonConstant;
import com.redphase.service.sys.SysVariableService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.List;

/**
 * 系统字典设置
 */
@Service
@Slf4j
public class SetupService extends BaseService implements ISetupService {
    @Autowired
    private SysVariableService variableService;

    @Override
    @ALogOperation(type = "路径设置", desc = "系统设置")
    public void savePathVariableList(List<SysVariableDto> dtos) throws Exception {
        saveVariableList(dtos);
    }

    @Override
    @ALogOperation(type = "通信设置", desc = "系统设置")
    public void saveSocketVariableList(List<SysVariableDto> dtos) throws Exception {
        saveVariableList(dtos);
    }

    @Override
    @ALogOperation(type = "超时设置", desc = "系统设置")
    public void saveTimeoutVariableList(List<SysVariableDto> dtos) throws Exception {
        saveVariableList(dtos);
    }

    @Override
    @ALogOperation(type = "阈值设置", desc = "系统设置")
    public void saveThresholdVariableList(List<SysVariableDto> dtos) throws Exception {
        saveVariableList(dtos);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public void saveVariableList(List<SysVariableDto> dtos) throws Exception {
        for (SysVariableDto dto : dtos) {
            variableService.updateByCode(dto);
        }
    }

    @Override
    public List<SysVariableDto> getVariableListByPCode(SysVariableDto dto) throws Exception {
        return variableService.findChildDataIsList(dto);
    }

    @Override
    public SysVariableDto getVariableByCode(SysVariableDto dto) throws Exception {
        return variableService.findDataByCode(dto);
    }

    public void copyFile(File fromFile, File toFile) throws Exception {
        FileInputStream ins = new FileInputStream(fromFile);
        FileOutputStream out = new FileOutputStream(toFile);
        byte[] b = new byte[1024];
        int n = 0;
        while ((n = ins.read(b)) != -1) {
            out.write(b, 0, n);
        }
        ins.close();
        out.close();
    }
}
