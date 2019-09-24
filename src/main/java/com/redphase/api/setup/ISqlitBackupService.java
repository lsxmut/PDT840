package com.redphase.api.setup;

/**
 * <p>数据库备份还原 业务处理接口类。
 */
public interface ISqlitBackupService {
    /**
     * 备份sqlite数据库
     */
    String getPath() throws Exception;

    /**
     * 备份sqlite数据库
     */
    void backup(String backupFile) throws Exception;

    /**
     * 恢复sqlite数据库
     **/
    void restore(String backupFile) throws Exception;

}