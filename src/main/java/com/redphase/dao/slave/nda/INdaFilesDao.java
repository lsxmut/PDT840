package com.redphase.dao.slave.nda;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>数据附件表 数据库处理接口类。
 */
@Mapper
public interface INdaFilesDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from data_files where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from data_files where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

}