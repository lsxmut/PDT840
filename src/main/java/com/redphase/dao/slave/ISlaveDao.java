package com.redphase.dao.slave;

import com.redphase.framework.IBaseDao;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ISlaveDao extends IBaseDao {
    @Delete("delete from data_aa_bx")
    int flushDbDataAaBx() throws Exception;

    @Delete("delete from data_aa_fx")
    int flushDbDataAaFx() throws Exception;

    @Delete("delete from data_aa_tj")
    int flushDbDataAaTj() throws Exception;

    @Delete("delete from data_aa_tjlb")
    int flushDbDataAaTjlb() throws Exception;

    @Delete("delete from data_aa_tjzs")
    int flushDbDataAaTjzs() throws Exception;

    @Delete("delete from data_aa_xj")
    int flushDbDataAaXj() throws Exception;

    @Delete("delete from data_aa_xjzs")
    int flushDbDataAaXjzs() throws Exception;

    @Delete("delete from data_ae_bx")
    int flushDbDataAeBx() throws Exception;

    @Delete("delete from data_ae_fx")
    int flushDbDataAeFx() throws Exception;

    @Delete("delete from data_ae_tj")
    int flushDbDataAeTj() throws Exception;

    @Delete("delete from data_ae_tjlb")
    int flushDbDataAeTjlb() throws Exception;

    @Delete("delete from data_ae_tjzs")
    int flushDbDataAeTjzs() throws Exception;

    @Delete("delete from data_ae_xj")
    int flushDbDataAeXj() throws Exception;

    @Delete("delete from data_ae_xjzs")
    int flushDbDataAeXjzs() throws Exception;

    @Delete("delete from data_analyze")
    int flushDbDataAnalyze() throws Exception;

    @Delete("delete from data_device")
    int flushDbDataDevice() throws Exception;

    @Delete("delete from data_device_site")
    int flushDbDataDeviceSite() throws Exception;

    @Delete("delete from data_hfct_tj")
    int flushDbDataHfctTj() throws Exception;

    @Delete("delete from data_hfct_tjlb")
    int flushDbDataHfctTjlb() throws Exception;

    @Delete("delete from data_hfct_tjzs")
    int flushDbDataHfctTjzs() throws Exception;

    @Delete("delete from data_hfct_xj")
    int flushDbDataHfctXj() throws Exception;

    @Delete("delete from data_hfct_xjzs")
    int flushDbDataHfctXjzs() throws Exception;

    @Delete("delete from data_hj")
    int flushDbDataHj() throws Exception;

    @Delete("delete from data_lc")
    int flushDbDataLc() throws Exception;

    @Delete("delete from data_tev_tj")
    int flushDbDataTevTj() throws Exception;

    @Delete("delete from data_tev_tjlb")
    int flushDbDataTevTjlb() throws Exception;

    @Delete("delete from data_tev_tjzs")
    int flushDbDataTevTjzs() throws Exception;

    @Delete("delete from data_tev_xj")
    int flushDbDataTevXj() throws Exception;

    @Delete("delete from data_tev_xjlh")
    int flushDbDataTevXjlh() throws Exception;

    @Delete("delete from data_tev_xjzs")
    int flushDbDataTevXjzs() throws Exception;

    @Delete("delete from data_uhf_tj_1")
    int flushDbDataUhfTj1() throws Exception;

    @Delete("delete from data_uhf_tj_2")
    int flushDbDataUhfTj2() throws Exception;

    @Delete("delete from data_uhf_tjlb_1")
    int flushDbDataUhfTjlb1() throws Exception;

    @Delete("delete from data_uhf_tjlb_2")
    int flushDbDataUhfTjlb2() throws Exception;

    @Delete("delete from data_uhf_tjzs_1")
    int flushDbDataUhfTjzs1() throws Exception;

    @Delete("delete from data_uhf_tjzs_2")
    int flushDbDataUhfTjzs2() throws Exception;
}