package com.redphase.dao.master.user;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * <p>权限_角色vs功能 数据库处理接口类。
 */
@Mapper
public interface IAuthRoleVsPermDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from auth_role_vs_perm where  role_id = #{roleId} and  perm_id = #{permId} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from auth_role_vs_perm where  role_id = #{roleId} and  perm_id = #{permId} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    /**
     * 根据主键role_id 物理删除
     */
    @Delete("delete from auth_role_vs_perm where  role_id = #{roleId}")
    int deleteBulkDataByRoleId(IEntity entity) throws Exception;

    /**
     * 根据主键perm_id 物理删除
     */
    @Delete("delete from auth_role_vs_perm where  perm_id = #{permId}")
    int deleteBulkDataByPermId(IEntity entity) throws Exception;

    /**
     * 根据角色获取权限列表
     */
    List<?> findPermIsList(IEntity entity) throws Exception;
}