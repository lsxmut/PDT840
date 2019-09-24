package com.redphase.api.task;

import com.github.pagehelper.PageInfo;
import com.redphase.dto.task.AccountSiteInfoDto;
import com.redphase.framework.Response;

import java.util.List;

/**
 * <p>account_site_info 业务处理接口类。
 */
public interface IAccountSiteInfoService {
    String acPrefix = "/feign//IAccountSiteInfoService";

    /**
     * <p>批量保存。
     */
    public Response batchSaveByAccountId(String accountId, List<AccountSiteInfoDto> dictDtos) throws Exception;

    /**
     * <p>信息编辑。
     */
    public Response saveOrUpdateData(AccountSiteInfoDto dto) throws Exception;

    /**
     * <p>物理删除。
     */
    public String deleteData(AccountSiteInfoDto dto) throws Exception;

    /**
     * <p>信息列表 分页。
     */
    public PageInfo findDataIsPage(AccountSiteInfoDto dto) throws Exception;

    /**
     * <p>信息列表。
     */
    public List<AccountSiteInfoDto> findDataIsList(AccountSiteInfoDto dto) throws Exception;

    /**
     * <p>信息详情。
     */
    public AccountSiteInfoDto findDataById(AccountSiteInfoDto dto) throws Exception;
}