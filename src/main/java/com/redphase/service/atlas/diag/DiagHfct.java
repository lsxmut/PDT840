package com.redphase.service.atlas.diag;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class DiagHfct extends BaseDiag {
    public String getResult(double[] p) {
        String result = "数据源出错";
        try {
            //二分类权重
            double[] er_juzhen_0 = fopen("/HFCT/2class/hfct_2class_A50_61.bin");
            double[] er_juzhen_1 = fopen("/HFCT/2class/hfct_2class_B40_51.bin");
            double[] er_juzhen_2 = fopen("/HFCT/2class/hfct_2class_C20_41.bin");
            double[] er_juzhen_3 = fopen("/HFCT/2class/hfct_2class_D3_21.bin");
            double[] er_minn = fopen("/HFCT/2class/hfct_2class_min.bin");
            double[] er_maxx = fopen("/HFCT/2class/hfct_2class_max.bin");

            //五分类权重
            double[] juzhen_0 = fopen("/HFCT/5class/hfct_x_5class_A50_61.bin");
            double[] juzhen_1 = fopen("/HFCT/5class/hfct_x_5class_B50_51.bin");
            double[] juzhen_2 = fopen("/HFCT/5class/hfct_x_5class_C5_51.bin");
            double[] minn = fopen("/HFCT/5class/hfct_x_5class_min.bin");
            double[] maxx = fopen("/HFCT/5class/hfct_x_5class_max.bin");
            double[] data_ = new double[DefineConstants.sample_len];
            double[] data_er = new double[DefineConstants.sample_len];
            for (int i = 0; i < DefineConstants.sample_len; i++) {
                data_[i] = p[i];
                data_er[i] = p[i];
            }
            int pred_label = -1;
            int pred_label_er = -1;

            // 二分类样本归一化
            // 获取样本最大值和最小值，再归一化
            //获取归一化最小值
            double[] er_min_ = new double[DefineConstants.sample_len];
            fread(er_minn, er_min_, DefineConstants.sample_len);

            //获取归一化最大值
            double[] er_max_ = new double[DefineConstants.sample_len];
            fread(er_maxx, er_max_, DefineConstants.sample_len);
            // 获取归一化最大值

            //1个样本归一化
            for (int num = 0; num < DefineConstants.sample_len; num++) {
                if (data_er[num] < er_min_[num]) {
                    data_er[num] = 0;
                } else if (data_er[num] > er_max_[num]) {
                    data_er[num] = 1.0;
                } else {
                    data_er[num] = (data_er[num] - er_min_[num]) / (er_max_[num] - er_min_[num]);
                }

            }
            //1个样本归一化

            // 获取样本最大值和最小值，再归一化
            // 二分类样本归一化

            //二分类代码
            double[][] a_er = new double[DefineConstants.erc_size_1_1][];
            for (int i = 0; i < DefineConstants.erc_size_1_1; i++) {
                a_er[i] = new double[DefineConstants.erc_size_1_2];
            }

            double[][] b_er = new double[DefineConstants.erc_size_2_1][];
            for (int i = 0; i < DefineConstants.erc_size_2_1; i++) {
                b_er[i] = new double[DefineConstants.erc_size_2_2];
            }

            double[][] c_er = new double[DefineConstants.erc_size_3_1][];
            for (int i = 0; i < DefineConstants.erc_size_3_1; i++) {
                c_er[i] = new double[DefineConstants.erc_size_3_2];
            }

            double[][] d_er = new double[DefineConstants.erc_size_4_1][];
            for (int i = 0; i < DefineConstants.erc_size_4_1; i++) {
                d_er[i] = new double[DefineConstants.erc_size_4_2];
            }

            // 四个矩阵参数 ，需要读

            // 四个激活值
            double[] er_b_1 = new double[DefineConstants.erc_size_1_1];  //200

            double[] er_c_1 = new double[DefineConstants.erc_size_2_1];  //250

            double[] er_d_1 = new double[DefineConstants.erc_size_3_1];  //4

            double[] er_f_1 = new double[DefineConstants.erc_size_4_1];  //4

            // 四个激活值

            //四个偏置
            double[] er_bias_1 = new double[DefineConstants.erc_size_1_1];
            double[] er_bias_2 = new double[DefineConstants.erc_size_2_1];
            double[] er_bias_3 = new double[DefineConstants.erc_size_3_1];
            double[] er_bias_4 = new double[DefineConstants.erc_size_4_1];


            read_data_erjin(er_juzhen_0, a_er, er_bias_1, DefineConstants.erc_size_1_1, DefineConstants.erc_size_1_2);

            read_data_erjin(er_juzhen_1, b_er, er_bias_2, DefineConstants.erc_size_2_1, DefineConstants.erc_size_2_2);

            read_data_erjin(er_juzhen_2, c_er, er_bias_3, DefineConstants.erc_size_3_1, DefineConstants.erc_size_3_2);

            read_data_erjin(er_juzhen_3, d_er, er_bias_4, DefineConstants.erc_size_4_1, DefineConstants.erc_size_4_2);

            //二分类进行预测

            for (int num = 0; num < 1; num++) {
                matrixx(a_er, data_er, er_b_1, DefineConstants.erc_size_1_1, DefineConstants.erc_size_1_2);  // b_1 是200维
                for (int i = 0; i < DefineConstants.erc_size_1_1; i++) {
                    er_b_1[i] = er_b_1[i] + er_bias_1[i];
                    er_b_1[i] = sigmoid(er_b_1[i]);
                }

                matrixx(b_er, er_b_1, er_c_1, DefineConstants.erc_size_2_1, DefineConstants.erc_size_2_2);  // c_1 是250维

                for (int i = 0; i < DefineConstants.erc_size_2_1; i++) {
                    er_c_1[i] = er_c_1[i] + er_bias_2[i];
                    er_c_1[i] = sigmoid(er_c_1[i]);
                }


                matrixx(c_er, er_c_1, er_d_1, DefineConstants.erc_size_3_1, DefineConstants.erc_size_3_2);  // d_1 是4维

                for (int i = 0; i < DefineConstants.erc_size_3_1; i++) {
                    er_d_1[i] = er_d_1[i] + er_bias_3[i];
                    er_d_1[i] = sigmoid(er_d_1[i]);
                }

                matrixx(d_er, er_d_1, er_f_1, DefineConstants.erc_size_4_1, DefineConstants.erc_size_4_2);  // d_1 是4维

                for (int i = 0; i < DefineConstants.erc_size_4_1; i++) {
                    er_f_1[i] = er_f_1[i] + er_bias_4[i];
                    er_f_1[i] = sigmoid(er_f_1[i]);
                }

                pred_label_er = max_i_(er_f_1, 2);  // 2个类别


            }
            //二分类进行预测

            //二分类代码


            //五分类代码
            // 获取样本最大值和最小值，再归一化
            //获取归一化最小值
            double[] min_ = new double[DefineConstants.sample_len];
            fread(minn, min_, DefineConstants.sample_len);

            //获取归一化最大值
            double[] max_ = new double[DefineConstants.sample_len];
            fread(maxx, max_, DefineConstants.sample_len);

            //1个样本归一化


            for (int num = 0; num < DefineConstants.sample_len; num++) {
                if (data_[num] < min_[num]) {
                    data_[num] = 0;
                } else if (data_[num] > max_[num]) {
                    data_[num] = 1.0;
                } else {
                    data_[num] = (data_[num] - min_[num]) / (max_[num] - min_[num]);
                }

            }
            //1个样本归一化
            // 获取样本最大值和最小值，再归一化


            double[][] a = new double[DefineConstants.SIZE_1_1][];
            for (int i = 0; i < DefineConstants.SIZE_1_1; i++) {
                a[i] = new double[DefineConstants.SIZE_1_2];
            }

            double[][] b = new double[DefineConstants.SIZE_2_1][];
            for (int i = 0; i < DefineConstants.SIZE_2_1; i++) {
                b[i] = new double[DefineConstants.SIZE_2_2];
            }

            double[][] c = new double[DefineConstants.SIZE_3_1][];
            for (int i = 0; i < DefineConstants.SIZE_3_1; i++) {
                c[i] = new double[DefineConstants.SIZE_3_2];  // 三个矩阵参数 ，需要读
            }


            double[] b_1 = new double[DefineConstants.SIZE_1_1];  //200

            double[] c_1 = new double[DefineConstants.SIZE_2_1];  //250

            double[] d_1 = new double[DefineConstants.SIZE_3_1];  //4
            // 三个激活值

            double[] bias_1 = new double[DefineConstants.SIZE_1_1];
            double[] bias_2 = new double[DefineConstants.SIZE_2_1];
            double[] bias_3 = new double[DefineConstants.SIZE_3_1];


            read_data_erjin(juzhen_0, a, bias_1, DefineConstants.SIZE_1_1, DefineConstants.SIZE_1_2);
            read_data_erjin(juzhen_1, b, bias_2, DefineConstants.SIZE_2_1, DefineConstants.SIZE_2_2);
            read_data_erjin(juzhen_2, c, bias_3, DefineConstants.SIZE_3_1, DefineConstants.SIZE_3_2);

            //五分类代码


            //测试单个样本
            if (pred_label_er == 1) {
                //System.out.print("尖端放电概率：");
                //System.out.print(0);
                resultIntArr[1] = 0;
                //System.out.print("\n");
                //System.out.print("颗粒放电概率：");
                //System.out.print(0);
                resultIntArr[5] = 0;
                //System.out.print("\n");
                //System.out.print("气隙放电概率：");
                //System.out.print(0);
                resultIntArr[4] = 0;
                //System.out.print("\n");
                //System.out.print("悬浮放电概率：");
                //System.out.print(0);
                resultIntArr[2] = 0;
                //System.out.print("\n");
                //System.out.print("沿面放电概率：");
                //System.out.print(0);
                resultIntArr[3] = 0;
                //System.out.print("\n");
                //System.out.print("干扰概率：");
                //System.out.print(0);
                resultIntArr[6] = 0;
                //System.out.print("\n");
                //System.out.print("其他概率：");
                //System.out.print(0);
                resultIntArr[7] = 0;
                //System.out.print("\n");
                //System.out.print("正常概率：");
                //System.out.print(100);
                resultIntArr[0] = 100;
                //System.out.print("\n");
                //System.out.print("最终预测类别为：");
                //System.out.print("正常");
                //System.out.print("\n");
                result = "正常";
            } else {
                for (int num = 0; num < 1; num++) {
                    double sum_;
                    matrixx(a, data_, b_1, DefineConstants.SIZE_1_1, DefineConstants.SIZE_1_2);  // b_1 是200维
                    for (int i = 0; i < DefineConstants.SIZE_1_1; i++) {
                        b_1[i] = b_1[i] + bias_1[i];
                        b_1[i] = sigmoid(b_1[i]);
                    }

                    matrixx(b, b_1, c_1, DefineConstants.SIZE_2_1, DefineConstants.SIZE_2_2);  // c_1 是250维

                    for (int i = 0; i < DefineConstants.SIZE_2_1; i++) {
                        c_1[i] = c_1[i] + bias_2[i];
                        c_1[i] = sigmoid(c_1[i]);
                    }


                    matrixx(c, c_1, d_1, DefineConstants.SIZE_3_1, DefineConstants.SIZE_3_2);  // d_1 是5维

                    for (int i = 0; i < DefineConstants.SIZE_3_1; i++) {
                        d_1[i] = d_1[i] + bias_3[i];
                        d_1[i] = sigmoid(d_1[i]);
                    }

                    pred_label = max_i_(d_1, 5);  // 4个类别

                    sum_ = d_1[0] + d_1[1] + d_1[2] + d_1[3] + d_1[4];
                    d_1[0] = d_1[0] / sum_;
                    d_1[1] = d_1[1] / sum_;
                    d_1[2] = d_1[2] / sum_;
                    d_1[3] = d_1[3] / sum_;
                    d_1[4] = d_1[4] / sum_;

                    //    System.out.print("尖端放电概率：");
                    //    System.out.print(Math.floor(100 * d_1[0]));
                    resultIntArr[1] = (int) Math.floor(100 * d_1[0]);
                    //    System.out.print("\n");
                    //    System.out.print("颗粒放电概率：");
                    //    System.out.print(Math.floor(100 * d_1[1]));
                    resultIntArr[5] = (int) Math.floor(100 * d_1[1]);
                    //    System.out.print("\n");
                    //    System.out.print("气隙放电概率：");
                    //    System.out.print(Math.floor(100 * d_1[2]));
                    resultIntArr[4] = (int) Math.floor(100 * d_1[2]);
                    //    System.out.print("\n");
                    //    System.out.print("悬浮放电概率：");
                    //    System.out.print(Math.floor(100 * d_1[3]));
                    resultIntArr[2] = (int) Math.floor(100 * d_1[3]);
                    //    System.out.print("\n");
                    //    System.out.print("沿面放电概率：");
                    //    System.out.print(Math.floor(100 * d_1[4]));
                    resultIntArr[3] = (int) Math.floor(100 * d_1[4]);
                    //    System.out.print("\n");
                    //    System.out.print("干扰概率：");
                    //    System.out.print(0);
                    resultIntArr[6] = 0;
                    //    System.out.print("\n");
                    //    System.out.print("其他概率：");
                    //    System.out.print(0);
                    resultIntArr[7] = 0;
                    //    System.out.print("\n");
                    //    System.out.print("正常概率：");
                    //    System.out.print(0);
                    resultIntArr[0] = 0;
                    //    System.out.print("\n");
                    if (pred_label == 0) {
                        //        System.out.print("最终预测类别为：");
                        //        System.out.print("尖端放电");
                        //        System.out.print("\n");
                        result = "尖端放电";
                    }
                    if (pred_label == 1) {
                        //        System.out.print("最终预测类别为：");
                        //        System.out.print("颗粒放电");
                        //        System.out.print("\n");
                        result = "颗粒放电";
                    }
                    if (pred_label == 2) {
                        //        System.out.print("最终预测类别为：");
                        //        System.out.print("气隙放电");
                        //        System.out.print("\n");
                        result = "气隙放电";
                    }
                    if (pred_label == 3) {
                        //        System.out.print("最终预测类别为：");
                        //        System.out.print("悬浮放电");
                        //        System.out.print("\n");
                        result = "悬浮放电";
                    }
                    if (pred_label == 4) {
                        //        System.out.print("最终预测类别为：");
                        //        System.out.print("沿面放电");
                        //        System.out.print("\n");
                        result = "沿面放电";
                    }

                }
            }
        } catch (Exception e) {
            log.error("诊断异常!", e);
        }
        // 测试单个样本
        return result;
    }

    final class DefineConstants {
        public static final int sample_num = 1000;
        public static final int sample_len = 60;
        public static final int erc_size_1_1 = 50;
        public static final int erc_size_1_2 = 60;
        public static final int erc_size_2_1 = 40;
        public static final int erc_size_2_2 = 50;
        public static final int erc_size_3_1 = 20;
        public static final int erc_size_3_2 = 40;
        public static final int erc_size_4_1 = 2;
        public static final int erc_size_4_2 = 20;
        public static final int SIZE_1_1 = 50;
        public static final int SIZE_1_2 = 60;
        public static final int SIZE_2_1 = 50;
        public static final int SIZE_2_2 = 50;
        public static final int SIZE_3_1 = 5;
        public static final int SIZE_3_2 = 50;
    }

    @Getter
    private int[] resultIntArr = new int[8];

    public static void main(String[] args) {
        //此数组由外传入
        double[] p = new double[60];
        for (int i = 0; i < 60; i++) {
            p[i] = -50.0;
        }
        DiagHfct hfct = new DiagHfct();
        System.out.println(hfct.getResult(p));
    }
}
