package com.redphase.dao.master.task;

import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * <p>account_site_info 数据库处理接口类。
 */
@Mapper
public interface IAccountSiteInfoDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from account_site_info where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from account_site_info where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    /**
     * 根据台帐ID删除测试位置字典
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    @Delete("delete from account_site_info where  account_id = #{accountId} ")
    int deleteByAccountId(@Param("accountId") Long accountId) throws Exception;

    /**
     * 判断台帐是否配置
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    @Select("select IFNULL(count(0),0) as count from account_site_info where  account_id = #{accountId} and del_flag=0")
    int isDataConfig(Long accountId) throws Exception;

    int findNumByAccountSiteInfo(AccountSiteInfoDto dto) throws Exception;
}