package com.redphase.service.convert.tofile;


public enum JSAtlasType {
    多图谱(0x00),
    高频多图谱(0x10),
    高频PRPD图(0x11),
    HFCT_TJ_高频PRPS图(0x12),
    HFCT_TJZS_高频PRPS图(0x12),
    高频脉冲波形图(0x13),
    特高频多图谱(0x20),
    特高频PRPD图(0x21),
    UHF_TJ_特高频PRPS图(0x22),
    UHF_TJZS_特高频PRPS图(0x22),
    特高频峰值统计图(0x23),
    特高频脉冲波形图(0x24),
    超声多图谱(0x30),
    AA_XJ_超声特征图(0x31),
    AA_XJZS_超声特征图(0x31),
    AA_TJ_超声相位图(0x32),
    AA_TJZS_超声相位图(0x32),
    AA_FX_超声脉冲图(0x33),
    AA_BX_超声波形图(0x34),
    AE_XJ_超声特征图(0x31),
    AE_XJZS_超声特征图(0x31),
    AE_TJ_超声相位图(0x32),
    AE_TJZS_超声相位图(0x32),
    AE_FX_超声脉冲图(0x33),
    AE_BX_超声波形图(0x34),
    暂态地电压多图谱(0x40),
    TEV_XJ_暂态地电压幅值(0x41),
    TEV_XJZS_暂态地电压幅值(0x41),

    UNTIL_TEV_XJ(0x03),//dBmV
    UNTIL_AA_XJ(0x04),//dBuV
    UNTIL_AE_XJ(0x06),//mV
    UNTIL_HFCT_XJ(0x06),//mV
    UNTIL_UHF_XJ(0x02),//dBm
    UNTIL_TEV_TJ(0x03),//dBmV
    UNTIL_AA_TJ(0x04),//dBuV
    UNTIL_AE_TJ(0x06),//mV
    UNTIL_HFCT_TJ(0x06),//mV
    UNTIL_UHF_TJ(0x02),//dBm
    //    UNTIL_未使用(0x00),
    //    UNTIL_dB(0x01),
    //    UNTIL_dBm(0x02),
    //    UNTIL_dBmV(0x03),
    //    UNTIL_dBμV(0x04),
    //    UNTIL_V(0x05),
    //    UNTIL_mV(0x06),
    //    UNTIL_μV(0x07),
    //    UNTIL_%(0x08),
    //    UNTIL_A(0x09),
    //    UNTIL_mA(0x0A),
    //    UNTIL_μA(0x0B),
    //    UNTIL_Ω(0x0C),
    //    UNTIL_mΩ(0x0D),
    //    UNTIL_μΩ(0x0E),
    //    UNTIL_m/s²(0x0F),
    //    UNTIL_mm(0x10),
    //    UNTIL_℃(0x11),
    //    UNTIL_℉(0x12),
    //    UNTIL_Pa(0x13),
    //    UNTIL_C(0x14),
    //    UNTIL_mC(0x15),
    //    UNTIL_μC(0x16),
    //    UNTIL_nC(0x17),
    //    UNTIL_pC(0x18),

    DATATYPE_未使用(0x00),
    DATATYPE_INT8(0x01),
    DATATYPE_UINT8(0x02),
    DATATYPE_INT16(0x03),
    DATATYPE_INT32(0x04),
    DATATYPE_INT64(0x05),
    DATATYPE_FLOAT(0x06),
    DATATYPE_DOUBLE(0x07),
    ;
    byte value;

    JSAtlasType(Integer value) {
        this.value = value.byteValue();
    }
}