package com.redphase.dao.master.setup;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>系统字典 数据库处理接口类。
 */
@Mapper
public interface ISetupDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from sys_dic where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from sys_dic where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

}