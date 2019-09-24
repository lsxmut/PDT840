package com.redphase.dao.master.account;

import com.redphase.dto.account.AccountDto;
import com.redphase.entity.account.Account;
import com.redphase.framework.IBaseDao;
import com.redphase.framework.IEntity;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * <p>account 数据库处理接口类。
 */
@Mapper
public interface IAccountDao extends IBaseDao {

    /**
     * 判断是否存在
     */
    @Select("select IFNULL(count(0),0) as count from account where  id = #{id} ")
    int isDataYN(IEntity entity) throws Exception;


    /**
     * 根据主键 物理删除
     */
    @Delete("delete from account where  id = #{id} ")
    int deleteByPrimaryKey(IEntity entity) throws Exception;

    List<?> findDataIsListByDirId(IEntity dirId) throws Exception;

    /**
     * 查询某一目录下当前最大的排序号
     *
     * @return
     * @throws Exception
     */
    int getMaxSortNum(Account account);

    /**
     * 查询指定台帐的上一个台帐
     *
     * @param account
     * @return
     */
    Account getPrevAccount(Account account);

    /**
     * 查询指定台帐的下一个台帐
     *
     * @param account
     * @return
     */
    Account getNextAccount(Account account);

    /**
     * 将所有在该台帐之前的设备整体下移一位
     *
     * @param account
     */
    void moveDownAllBefore(Account account);

    /**
     * 将所有在该台帐之前的设备整体上移一位
     *
     * @param account
     */
    void moveUpAllAfter(Account account);

    /**
     * 查询台帐列表
     *
     * @param param
     * @return
     */
    List<Account> queryAccountList(Map param);
    /**
     * 查询台帐列表
     *
     * @param param
     * @return
     */
    List<Account> findAccountList(Map param);

    /**
     * 批量插入台帐
     *
     * @param accounts
     * @return
     */
    int batchInsert(List<Account> accounts);

    /**
     * 根据导入的组合项目的任务单文件名称查询台帐信息
     *
     * @param account
     * @return
     */
    Account selectByOtherFileName(AccountDto account);

    /**
     * 根据导入的开关柜的任务单文件名称查询台帐信息
     *
     * @param account
     * @return
     */
    List<Account> selectBySwitchFileName(AccountDto account);

    List<Account> findDataListBySubstation(Account account);

    Account findDataByFullName(Account account);
}