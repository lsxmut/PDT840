package com.redphase.dao.master.user;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.*;

import java.util.Map;

/**
 * <p>组织架构_员工  数据库处理接口类。
 */
@Mapper
public interface IUserDao extends IBaseDao {
    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from user where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;

    /**
     * 逻辑删除
     */
    @Update("update user set version=version+1, date_updated=strftime('%s000','now'), del_flag=1 where  id = #{id} ")
    int deleteById(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from user where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    /**
     * 恢复逻辑删除的数据
     */
    @Update("update user set version=version+1 ,del_flag=0 where  id = #{id} ")
    int recoveryDataById(IEntity dto) throws Exception;

    /**
     * <p>获取员工信息>根据员工登录名。
     */
    Object findUserByAccount(Map dto) throws Exception;

    Object selectUserPwdByPrimaryKey(IEntity entity) throws Exception;

    /**
     * <p>判断员工账号是否存在
     */
    @Select(" select count(0) from user where  account=#{account} ")
    int isAccountYN(@Param("account") String account) throws Exception;

    @Select(" select count(0) from user where  job_no=#{jobNo} ")
    int isJobNoYN(@Param("jobNo") String jobNo) throws Exception;

    /**
     * <p>密码修改
     */
    @Update("update user set version=version+1,date_updated=strftime('%s000','now'),pwd=#{confirmpwd} where id = #{id} and pwd=#{oldpwd}")
    int updatePwd(IEntity entity) throws Exception;

    /**
     * <p>密码重置
     */
    @Update("update user set version=version+1,date_updated=strftime('%s000','now'),pwd=#{pwd} where id = #{id}")
    int resetPwd(IEntity entity) throws Exception;

    /**
     * <p>最后登陆记录
     */
    @Update("update user set count=count+1,last_login=strftime('%s000','now') where id = #{id}")
    int lastLogin(IEntity entity) throws Exception;
}