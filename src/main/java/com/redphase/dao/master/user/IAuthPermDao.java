package com.redphase.dao.master.user;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;
import java.util.Map;

/**
 * <p>权限_权限信息  数据库处理接口类。
 */
@Mapper
public interface IAuthPermDao extends IBaseDao {
    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from auth_perm where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;

    /**
     * 逻辑删除
     */
    @Update("update auth_perm set version=version+1, date_updated=strftime('%s000','now'), del_flag=1 where  id = #{id} ")
    int deleteById(IEntity entity) throws Exception;

    /**
     * 根据主键 物理删除
     */
    @Delete("delete from auth_perm where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    /**
     * 恢复逻辑删除的数据
     */
    @Update("update auth_perm set version=version+1, date_updated=strftime('%s000','now') ,del_flag=0 where  id = #{id} ")
    int recoveryDataById(IEntity dto) throws Exception;

    /**
     * 角色权限信息列表>根据员工id。
     */
    List getPermListByUserId(Map dto) throws Exception;

    /**
     * <p>根据角色id获取对应的权限信息列表。
     */
    List<?> findPermDataIsListByRoleId(Map dto) throws Exception;

    @Select("select IFNULL(count(0),0) as count from auth_perm where id=#{id} and nodel_flag = 1")
    int isNoDelFlag(IEntity entity) throws Exception;
}