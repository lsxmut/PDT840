package com.redphase.api.atlas;

import java.io.File;

/**
 * <p>统计分析+图谱重绘 业务处理接口类。
 */
public interface IAtlasService {

    public void copyFile(File fromFile, File toFile) throws Exception;
}