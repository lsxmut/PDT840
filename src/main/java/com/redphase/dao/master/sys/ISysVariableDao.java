package com.redphase.dao.master.sys;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

/**
 * <p>系统_数据字典表 数据库处理接口类。
 */
@Mapper
public interface ISysVariableDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from sys_variable where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;

    /**
     * 逻辑删除
     */
    @Update("update sys_variable set version=version+1, date_updated=strftime('%s000','now'), del_flag=1 where  id = #{id} ")
    int deleteById(IEntity entity) throws Exception;

    /**
     * 根据主键 物理删除
     */
    @Delete("delete from sys_variable where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    List findChildDataIsList(IEntity entity) throws Exception;

    Object findDataByCode(IEntity entity) throws Exception;

    @Select("select t1.code from sys_variable t1 INNER JOIN sys_variable t2 on (t2.`code`='userlevel' and t1.parent_id=t2.id) where t1.del_flag=0 and t1.`name`=#{name}")
    String getCodeByVariableName(IEntity entity) throws Exception;

    @Update("update sys_variable set date_updated=strftime('%s000','now') ,version=version+1  ,value = #{value} where code = #{code} ")
    int updateByCode(IEntity entity) throws Exception;
}