package com.redphase.framework.util;

import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

@Slf4j
public class ByteConverterUtil {

    //byte 数组与 long 的相互转换
    public static byte[] long2bytes(long x) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.putLong(0, x);
        return buffer.array();
    }

    public static long byte2long(byte[] bytes) {
        ByteBuffer buffer = ByteBuffer.allocate(8);
        buffer.put(bytes, 0, bytes.length);
        buffer.flip();//need flip
        return buffer.getLong();
    }

    public static int byte2int(byte[] b) {
        if (b.length < 4) {
            byte[] bytes = {0, 0, 0, 0};
            System.arraycopy(b, 0, bytes, bytes.length - b.length, b.length);
            b = bytes;
        }
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    /**
     * int转byte数组
     */
    public static byte[] int2byte(int num) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) ((num >> 24) & 0xff);
        bytes[1] = (byte) ((num >> 16) & 0xff);
        bytes[2] = (byte) ((num >> 8) & 0xff);
        bytes[3] = (byte) (num & 0xff);
        return bytes;
    }

    public static float byte2float(byte[] b) {
        int accum = 0;
//        accum = accum | (b[0] & 0xff) << 0;
//        accum = accum | (b[1] & 0xff) << 8;
//        accum = accum | (b[2] & 0xff) << 16;
//        accum = accum | (b[3] & 0xff) << 24;
        accum = accum | (b[0] & 0xff) << 24;
        accum = accum | (b[1] & 0xff) << 16;
        accum = accum | (b[2] & 0xff) << 8;
        accum = accum | (b[3] & 0xff) << 0;
        return Float.intBitsToFloat(accum);
    }

    public static byte[] float2byte(float f) {
        // 把float转换为byte[]
        int fbit = Float.floatToIntBits(f);

        byte[] b = new byte[4];
        for (int i = 0; i < 4; i++) {
            b[i] = (byte) (fbit >> (24 - i * 8));
        }
        return b;
//        // 翻转数组
//        int len = b.length;
//        // 建立一个与源数组元素类型相同的数组
//        byte[] dest = new byte[len];
//        // 为了防止修改源数组，将源数组拷贝一份副本
//        System.arraycopy(b, 0, dest, 0, len);
//        byte temp;
//        // 将顺位第i个与倒数第i个交换
//        for (int i = 0; i < len / 2; ++i) {
//            temp = dest[i];
//            dest[i] = dest[len - i - 1];
//            dest[len - i - 1] = temp;
//        }
//        return dest;
    }

    public static byte[] char2byte(char c) {
        byte[] b = new byte[2];
        b[0] = (byte) ((c & 0xFF00) >> 8);    //0xff00=1111 1111 0000 0000
        b[1] = (byte) (c & 0xFF);   //0xff,=1111 1111
        return b;
    }

    public static char byte2char(byte[] b) {
        char c = 0;
        if (b.length == 1) {
            c = (char) (b[0] & 0xFF);
        } else {
            c = (char) (((b[0] & 0xFF) << 8) | (b[1] & 0xFF));
        }
        return c;
    }

    public static byte[] short2byte(short s) {
        byte[] b = new byte[2];
        for (int i = 0; i < 2; i++) {
            int offset = 16 - (i + 1) * 8; //因为byte占4个字节，所以要计算偏移量
            b[i] = (byte) ((s >> offset) & 0xff); //把16位分为2个8位进行分别存储
        }
        return b;
    }

    public static int byte2short(byte[] b) {
        short shortValue = 0;
        for (int i = 0; i < b.length; i++) {
            shortValue += (b[i] & 0x00FF) << (8 * (1 - i));
        }
        return shortValue & 0x0FFFF;
    }

    public static byte[] string2byte(String str) {
        if (str == null) {
            return null;
        }
        byte[] byteArray = str.getBytes();
        return byteArray;
    }

    public static String byte2string(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        String str = new String(bytes);
        if (ValidatorUtil.notEmpty(str)) {
            str = str.trim().replaceAll("[\u0000-\u001f\b]", "");
        }
        return str;
    }

    public static byte[] unicode2byte(String s) {
        int len = s.length();
        byte abyte[] = new byte[len << 1];
        int j = 0;
        for (int i = 0; i < len; i++) {
            char c = s.charAt(i);
            abyte[j++] = (byte) (c >> 8);
            abyte[j++] = (byte) (c & 0xff);
        }
        return abyte;
    }

    public static String byte2unicode(byte[] src) {
        return byte2unicode(src, 0, src.length);
    }

    public static String byte2unicode(byte[] src, int start, int len) {
        if (start + len > src.length || len % 2 != 0) {
            throw new IllegalArgumentException();
        }
        StringBuffer sb = new StringBuffer();
        char ch;
        for (int i = start; i < start + (len / 2); i++) {
            ch = (char) ((src[start + (i - start) * 2] << 8) | (src[start + (i - start) * 2 + 1] & 0xff));
            sb.append(ch);
        }

        if (ValidatorUtil.notEmpty(sb.toString())) {
            return sb.toString().trim().replaceAll("[\u0000-\u001f\b]", "");
        }
        return sb.toString().trim();
    }

    public static byte[] double2Bytes(double d) {
        long value = Double.doubleToRawLongBits(d);
        byte[] byteRet = new byte[8];
        for (int i = 0; i < 8; i++) {
            byteRet[i] = (byte) ((value >> 8 * i) & 0xff);
        }
        return byteRet;
    }

    public static double bytes2Double(byte[] arr) {
        long value = 0;
        for (int i = 0; i < 8; i++) {
            value |= ((long) (arr[i] & 0xff)) << (8 * i);
        }
        return Double.longBitsToDouble(value);
    }

    public static void main(String[] args) throws IOException {
//        System.out.println(byte2unicode(new byte[]{127, 22, 120, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}));
//        System.out.println(byte2string(new byte[]{0,82,0,101,0,100,0,80,0,104,0,97,0,115,0,101,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0}));
//        System.out.println(byte2string(new byte[]{0,0,-56,65,-16,0,0,65,-16,0,0,1,66,48,0,0,66,112,0,0,65,-96,0,0,65,-56,0,0,65,-16,0,0}));
//        System.out.println(byte2unicode(new byte[]{0,0,-56,65,-16,0,0,65,-16,0,0,1,66,48,0,0,66,112,0,0,65,-96,0,0,65,-56,0,0,65,-16,0,0}));
        File dataFile = new File(Thread.currentThread().getContextClassLoader().getResource("DBN60/AE/2class/ae_2class_A50_61.bin").getPath());
        FileInputStream inputStream = new FileInputStream(dataFile);
        byte[] dataBuffer = new byte[(int) dataFile.length()];
        if (inputStream.read(dataBuffer) > 0) {// == dataRowBuffer.length
            for (int i = 0; i < dataBuffer.length / 8; i += 8) {
                byte[] destBytes = new byte[8];
                System.arraycopy(dataBuffer, i, destBytes, 0, destBytes.length);
                System.out.println(bytes2Double(destBytes));
            }
        }
    }
}
