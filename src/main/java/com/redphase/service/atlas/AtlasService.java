package com.redphase.service.atlas;

import com.redphase.api.atlas.IAtlasService;
import com.redphase.framework.service.BaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * 统计分析+图谱重绘
 */
@Service
@Slf4j
public class AtlasService extends BaseService implements IAtlasService {
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
