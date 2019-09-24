package com.redphase.api.setup;

import com.redphase.dto.sys.SysVariableDto;

import java.io.File;
import java.util.List;

/**
 * <p>系统_数据字典表 业务处理接口类。
 */
public interface ISetupService {
    /**
     * <p>>路径设置。
     */
    public void savePathVariableList(List<SysVariableDto> dtos) throws Exception;

    /**
     * <p>>网络设置。
     */
    public void saveSocketVariableList(List<SysVariableDto> dtos) throws Exception;

    /**
     * <p>>过期时间设置。
     */
    public void saveTimeoutVariableList(List<SysVariableDto> dtos) throws Exception;

    /**
     * <p>>阈值设置。
     */
    public void saveThresholdVariableList(List<SysVariableDto> dtos) throws Exception;

    /**
     * <p>>编辑保存。
     */
    public void saveVariableList(List<SysVariableDto> dtos) throws Exception;

    /**
     * <p>根据pcode获取列表。
     */
    public List<SysVariableDto> getVariableListByPCode(SysVariableDto dto) throws Exception;

    /**
     * <p>根据code获取。
     */
    public SysVariableDto getVariableByCode(SysVariableDto dto) throws Exception;

    public void copyFile(File fromFile, File toFile) throws Exception;
}