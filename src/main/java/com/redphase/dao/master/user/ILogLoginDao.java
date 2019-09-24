package com.redphase.dao.master.user;

import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * <p>组织架构_员工登录日志 数据库处理接口类。
 */
@Mapper
public interface ILogLoginDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from log_login where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;

    /**
     * 根据主键 物理删除
     */
    @Delete("delete from log_login where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    /**
     * 根据过期天数 物理删除
     */
    @Delete("delete from log_login where (julianday(strftime('%Y-%m-%d', 'now'))-julianday(strftime('%Y-%m-%d', datetime(date_created/1000, 'unixepoch', 'localtime')))) <= #{expireDay} ")
    int deleteByExpireDay(Integer expireDay) throws Exception;
}