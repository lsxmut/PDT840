package com.redphase.api.account;

import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.framework.Response;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * 台帐接口
 */
public interface IAccountService {

    /**
     * 加载台帐树
     *
     * @param param 过滤条件
     * @return
     * @throws Exception
     */
    List<ZTreeNodeDto> loadAccountTree(Map param, String... attrIds) throws Exception;

    /**
     * 新增台帐
     *
     * @param accountDto
     * @return 台帐ID
     */
    Long addAccount(AccountDto accountDto) throws Exception;

    /**
     * 修改台帐
     *
     * @param accountDto
     * @return 台帐ID
     */
    Long updateAccount(AccountDto accountDto) throws Exception;

    /**
     * 开关柜上移/下移
     *
     * @param accountId 设备ID
     * @param tag       标志位（1、上移；2、下移）
     * @return
     * @throws Exception
     */
    Response moveStep(Long accountId, Integer tag) throws Exception;

    /**
     * 开关柜置顶
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    Response moveTop(Long accountId) throws Exception;

    /**
     * 开关柜置底
     *
     * @param accountId
     * @return
     * @throws Exception
     */
    Response moveBottom(Long accountId) throws Exception;

    /**
     * 导入台账数据
     *
     * @param file
     * @return
     * @throws Exception
     */
    Response importAccount(File file) throws Exception;

    /**
     * 导出台帐
     *
     * @param dir  导出目录
     * @param data 需要导出的台帐参数
     * @return
     * @throws Exception
     */
    Response exportAccount(File dir, ZTreeNodeDto data) throws Exception;

    /**
     * 拷贝台帐模板
     *
     * @param dir      目标目录
     * @param fileName 生成的文件名
     * @return
     * @throws Exception
     */
    File copyTemplate(File dir, String fileName) throws Exception;

    /**
     * 删除台帐
     *
     * @param id
     * @return
     * @throws Exception
     */
    Response deleteAccount(Long id) throws Exception;

    /**
     * 查询台帐
     *
     * @param param
     * @return
     * @throws Exception
     */
    List<AccountDto> queryAccountList(Map param) throws Exception;

    AccountDto findDataById(Long id);

    List<AccountDto> findDataIsListDirId(AccountDto dto) throws Exception;

    List<AccountDto> findDataIsList(AccountDto dto) throws Exception;

    /**
     * 根据变电站获取台账信息
     */
    List<AccountDto> findDataListBySubstation(AccountDto dto) throws Exception;

    /**
     * 查询
     */
    AccountDto findDataByFullName(AccountDto dto) throws Exception;

    /**
     * 备份台账
     */
    Response backupAccount(File dir, ZTreeNodeDto zTreeNodeDto) throws Exception;

    /**
     * 还原台账
     */
    Response restoreAccount(File file) throws Exception;

}
