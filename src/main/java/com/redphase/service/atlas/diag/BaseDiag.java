package com.redphase.service.atlas.diag;

import com.redphase.framework.util.ByteConverterUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Component
public abstract class BaseDiag {
    @Value("${path.DBN60}")
    private String Dbn60Path;

    protected double[] fopen(String url) {
        double[] doubleBytes = null;
        FileInputStream inputStream = null;
        try {
            File dataFile = new File(Dbn60Path + url);
//            File dataFile = new File(Thread.currentThread().getContextClassLoader().getResource( url).getPath());
//            File dataFile = new File(FileUtil.getPath() + "/" + url);
            System.out.println(dataFile.getPath());
            inputStream = new FileInputStream(dataFile);
//            System.out.println( dataFile.length());
            doubleBytes = new double[(int) dataFile.length() / 8];
            byte[] destBytes = new byte[8];
            int i = 0;
            while (inputStream.read(destBytes) > 0) {
                doubleBytes[i++] = ByteConverterUtil.bytes2Double(destBytes);
            }
        } catch (Exception e) {
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                }
            }
        }
        return doubleBytes;
    }

    protected void fread(double[] srcData, double[] dstData, int sampleLen) {
        fread(srcData, 0, dstData, 0, sampleLen);
    }

    protected void fread(double[] srcData, int srcPos, double[] dstData, int destPos, int sampleLen) {
        System.arraycopy(srcData, srcPos, dstData, destPos, sampleLen);
    }

    protected double[][] read_data_erjin(double[] fileBytes, double[][] a, double[] b, int sample, int sample_length) {
        int srcPost = 0;
        for (int i = 0; i < sample; i++) {
            fread(fileBytes, srcPost, a[i], 0, sample_length);
            srcPost += sample_length;
            fread(fileBytes, srcPost, b, i, 1);
            srcPost++;
        }
        return a;
    }

    protected double[] matrixx(double[][] a, double[] b, double[] c, int row, int col) {
        int col_a = col;
        int row_a = row;
        for (int m = 0; m < row_a; m++) {
            c[m] = 0;
            for (int n = 0; n < col_a; n++) {
                c[m] += a[m][n] * b[n];
            }
        }
        return c;
    }

    protected double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    protected int max_i_(double[] x, int l) {
        double max_v = x[0];
        int max_i = 0;
        for (int i = 0; i < l; i++) {
            if (x[i] > max_v) {
                max_v = x[i];
                max_i = i;
            }
        }
        return max_i;
    }
}
