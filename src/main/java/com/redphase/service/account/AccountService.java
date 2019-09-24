package com.redphase.service.account;

import com.alibaba.fastjson.JSONReader;
import com.alibaba.fastjson.JSONWriter;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.account.IAccountService;
import com.redphase.dao.master.account.IAccountDao;
import com.redphase.dao.master.task.IAccountSiteInfoDao;
import com.redphase.dto.ZTreeNodeDto;
import com.redphase.dto.account.AccountDto;
import com.redphase.entity.account.Account;
import com.redphase.entity.task.AccountSiteInfo;
import com.redphase.framework.MultiTree;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.annotation.ALogOperation;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.*;
import com.redphase.framework.util.excel.ExcelUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;

/**
 * 台帐
 */
@Service
@Slf4j
public class AccountService extends BaseService implements IAccountService {

    @Autowired
    private IAccountDao accountDao;
    @Resource
    private IAccountSiteInfoDao accountSiteInfoDao;

    /**
     * 加载台帐树
     */
    @Override
    public List<ZTreeNodeDto> loadAccountTree(Map param, String... attrIds) throws Exception {
        //查询所有台帐
        List<AccountDto> deviceList = queryAccountList(param);
        if (deviceList == null) {
            return null;
        }
        MultiTree multiTree = new MultiTree(false,"id", deviceList, attrIds);
        List<ZTreeNodeDto> treeNode = multiTree.buildTree();
        return treeNode;
    }

    /**
     * 新增台帐数据
     *
     * @param accountDto
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "新增", desc = "台账建设")
    public Long addAccount(AccountDto accountDto) throws Exception {
        //获取台帐目录ID
        accountDto.setId(null);
        accountDto.setSortNum(accountDao.getMaxSortNum(copyTo(accountDto, Account.class)) + 1);
        return NumberUtils.toLong(saveOrUpdateData(accountDto).data.toString());
    }

    /**
     * 修改台帐数据
     *
     * @param accountDto
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "修改", desc = "台账建设")
    public Long updateAccount(AccountDto accountDto) throws Exception {
        saveOrUpdateData(accountDto);
        return accountDto.getId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "顺序调整-上下移动", desc = "台账建设")
    public Response moveStep(Long accountId, Integer tag) throws Exception {
        AccountDto account1 = findDataById(accountId);
        AccountDto account2;
        if (tag.equals(1)) {
            //上移
            account2 = getPrevAccount(account1);
        } else {
            //下移
            account2 = getNextAccount(account1);
        }

        if (account2 == null) {
            return Response.error("该设备不能再" + (tag.equals(1) ? "上移了" : "下移了"));
        }
        //交换顺序
        Integer tmp = account1.getSortNum();
        account1.setSortNum(account2.getSortNum());
        account2.setSortNum(tmp);
        saveOrUpdateData(account1);
        saveOrUpdateData(account2);
        return new Response();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "顺序调整-置顶", desc = "台账建设")
    public Response moveTop(Long accountId) throws Exception {
        AccountDto accountDto = findDataById(accountId);
        //将所有在该台帐之前的设备整体下移一位
        Account account = copyTo(accountDto, Account.class);
        accountDao.moveDownAllBefore(account);
        //置顶
        accountDto.setSortNum(1);
        return saveOrUpdateData(accountDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "顺序调整-置底", desc = "台账建设")
    public Response moveBottom(Long accountId) throws Exception {
        AccountDto accountDto = findDataById(accountId);
        //将所有在该台帐之前的设备整体上移一位
        Account account = copyTo(accountDto, Account.class);
        accountDao.moveUpAllAfter(account);
        //置底
        accountDto.setSortNum(accountDao.getMaxSortNum(account) + 1);
        return saveOrUpdateData(accountDto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "导入台账", desc = "台账建设")
    public Response importAccount(File file) throws Exception {
        Response result = new Response();
        InputStream is = null;
        Workbook wb = null;
        int repeatCount = 0;
        int newCount = 0;
        try {
            is = new FileInputStream(file);
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            if ("xls".equals(fileType)) {
                wb = new HSSFWorkbook(is);
            } else if ("xlsx".equals(fileType)) {
                wb = new XSSFWorkbook(is);
            } else {
                throw new Exception("文件格式错误！");
            }
            //只处理第一个sheet
            Sheet sheet = wb.getSheetAt(0);
            int rowSize = sheet.getLastRowNum() + 1;
            List<AccountDto> importAccountDtos = new ArrayList<>(rowSize - 2);
            //前两行是标题和说明，从第三行开始遍历行
            for (int i = 2; i < rowSize; i++) {
                Row row = sheet.getRow(i);
                if (row == null) {
                    continue;
                }
                //读取一行数据
                List rowData = (List) ExcelUtil.readSheet(ExcelUtil.ReturnTypeEnum.LIST, row);
                try {
                    importAccountDtos.add(handleAccountData(rowData));
                } catch (ServiceException e) {
                    return Response.error("第" + (i + 1) + "行" + e.getMessage());
                }
            }

            //创建台帐目录，设置排序号
            List<AccountDto> oldAccountList = findDataIsList(null);
            List<AccountDto> newAccountList = new ArrayList<>();
            for (int i = 0; i < importAccountDtos.size(); i++) {
                AccountDto account = importAccountDtos.get(i);
                //查找重复
                AccountDto oldAccount = oldAccountList.stream()
                        .filter(old -> old.getElectricBureau().equals(account.getElectricBureau())
                                && old.getSubstation().equals(account.getSubstation())
                                && old.getDeviceType().equals(account.getDeviceType())
                                && old.getVoltageLevel().equals(account.getVoltageLevel())
                                && old.getDeviceName().equals(account.getDeviceName())
                        ).findFirst().orElse(null);
                if (oldAccount != null) {
                    repeatCount++;
                    continue;
                }
                newCount++;
                newAccountList.add(account);
                //查找和当前遍历对象相同目录的排序号最大的台帐
                AccountDto oldMaxAccount = oldAccountList.stream()
                        .filter(old -> old.getElectricBureau().equals(account.getElectricBureau()) && old.getSubstation().equals(account.getSubstation()) &&
                                old.getDeviceType().equals(account.getDeviceType()) && old.getVoltageLevel().equals(account.getVoltageLevel()))
                        .max(Comparator.comparing(AccountDto::getSortNum)).orElse(null);
                if (oldMaxAccount == null) {
//                    account.setDirId(accountDirService.createDir(account));
                    account.setSortNum(1);
                } else {
                    account.setDirId(oldMaxAccount.getDirId());
                    account.setSortNum(oldMaxAccount.getSortNum() + 1);
                }
                oldAccountList.add(account);
            }
            if (newAccountList != null && newAccountList.size() > 0) {
                accountDao.batchInsert(copyTo(newAccountList, Account.class));
            }
        } finally {
            if (wb != null) {
                wb.close();
            }
            if (is != null) {
                is.close();
            }
        }
        result.message = "新入库" + newCount + "条,历史存在:" + repeatCount + "条";
        return result;
    }

    @Override
    @ALogOperation(type = "导出台账", desc = "台账建设")
    public Response exportAccount(File dir, ZTreeNodeDto zTreeNodeDto) throws Exception {
        //查询所有需要导出的台帐
        Map<String, Object> queryParam = new HashMap<>();
        if (zTreeNodeDto != null) {
            String fullName = zTreeNodeDto.getFullName();
            if (ValidatorUtil.notEmpty(fullName)) {
                String[] paramArr = fullName.split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
                for (int i = 0; i < paramArr.length; i++) {
                    switch (i) {
                        case 0: {
                            queryParam.put("electricBureau", paramArr[i]);
                            break;
                        }
                        case 1: {
                            queryParam.put("substation", paramArr[i]);
                            break;
                        }
                        case 2: {
                            queryParam.put("voltageLevel", paramArr[i].replace("kV", ""));
                            break;
                        }
                        case 3: {
                            queryParam.put("deviceType", AccountDto.DeviceType.getValueByText(paramArr[i]));
                            break;
                        }
                        case 4: {
                            queryParam.put("deviceName", paramArr[i]);
                            break;
                        }
                    }
                }
            }
        }
        List<Account> accountList = accountDao.findAccountList(queryParam);
        //拷贝模板
        File file = copyTemplate(dir, "/台帐数据_" + DateUtil.getCurDateStr("yyyyMMddHHmmss") + ".xls");
        InputStream inputStream = null;
        HSSFWorkbook workBook = null;
        try {
            inputStream = new FileInputStream(file);
            workBook = new HSSFWorkbook(inputStream);
            Sheet sheet = workBook.getSheetAt(0);

            for (int i = 0; i < accountList.size(); i++) {
                Account account = accountList.get(i);
                //从第三行开始写
                Row row = sheet.createRow(i + 2);
                row.createCell(0).setCellValue(i + 1);
                row.createCell(1).setCellValue(StrUtil.asString(account.getElectricBureau()));
                row.createCell(2).setCellValue(StrUtil.asString(account.getSubstation()));
                row.createCell(3).setCellValue(StrUtil.asString(account.getSubstationCode()));
                row.createCell(4).setCellValue(StrUtil.asString(account.getSpaceName()));
                row.createCell(5).setCellValue(StrUtil.asString(account.getSpaceCode()));
                row.createCell(6).setCellValue(AccountDto.DeviceType.getTextByValue(account.getDeviceType()));
                row.createCell(7).setCellValue(StrUtil.asString(account.getSortNum()));
                row.createCell(8).setCellValue(StrUtil.asString(account.getDeviceName()));
                row.createCell(9).setCellValue(StrUtil.asString(account.getDeviceCode()));
                row.createCell(10).setCellValue(account.getVoltageLevel() + "kV");
                row.createCell(11).setCellValue(StrUtil.asString(account.getManufacturer()));
                row.createCell(12).setCellValue(StrUtil.asString(account.getDeviceVersion()));
                row.createCell(13).setCellValue(StrUtil.asString(account.getRunDept()));
                if (account.getUseDate() != null) {
                    row.createCell(14).setCellValue(DateUtil.getDateStr(account.getUseDate()));
                }
                if (account.getProduceDate() != null) {
                    row.createCell(15).setCellValue(DateUtil.getDateStr(account.getProduceDate()));
                }
                row.createCell(16).setCellValue(account.getRunState());
                if (account.getOutageDate() != null) {
                    row.createCell(17).setCellValue(DateUtil.getDateStr(account.getOutageDate()));
                }
            }

            workBook.write(file);
        } finally {
            if (workBook != null) {
                workBook.close();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return new Response();
    }

    @Value("${path.tempXls}")
    private String tempXlsPath;

    @Override
    public File copyTemplate(File dir, String fileName) throws Exception {
        File template = new File(FileUtil.getPath() + tempXlsPath);
        File targetFile = new File(dir.getAbsolutePath() + fileName);
        if (targetFile.exists()) {
            targetFile.delete();
        }
        FileUtils.copyFile(template, targetFile);
        return targetFile;
    }

    @Override
    @ALogOperation(type = "删除台账", desc = "台账建设")
    public Response deleteAccount(Long id) throws Exception {
        AccountDto accountDto = findDataById(id);
        if (accountDto == null) {
            return Response.error("台帐不存在！");
        }

        //删除位置信息
        accountSiteInfoDao.deleteByAccountId(id);

        accountDto.setDelFlag(1);
        return saveOrUpdateData(accountDto);
    }

    /**
     * 处理excel一行数据（包括校验）
     *
     * @param rowData
     * @return
     */
    public AccountDto handleAccountData(List rowData) throws Exception {
        AccountDto accountDto = new AccountDto();
        accountDto.setId(IdUtil.nextId());
        //从第二列开始读取，第一列是序号
        String electricBureau = StrUtil.asString(rowData.get(1));
        if (StringUtils.isEmpty(electricBureau)) {
            throw new ServiceException(SysErrorCode.defaultError, "电业局名称不能为空！");
        }
        if (electricBureau.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "电业局名称不能超过200个字符！");
        }
        accountDto.setElectricBureau(electricBureau);

        String substation = StrUtil.asString(rowData.get(2));
        if (StringUtils.isEmpty(substation)) {
            throw new ServiceException(SysErrorCode.defaultError, "变电站名称不能为空！");
        }
        if (substation.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "变电站名称不能超过200个字符！");
        }
        accountDto.setSubstation(substation);

        String substationCode = StrUtil.asString(rowData.get(3));
        if (substationCode.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "变电站编码不能超过200个字符！");
        }
        accountDto.setSubstationCode(substationCode);

        String spaceName = StrUtil.asString(rowData.get(4));
        if (spaceName.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "间隔名称不能超过200个字符！");
        }
        accountDto.setSpaceName(spaceName);

        String spaceCode = StrUtil.asString(rowData.get(5));
        if (spaceCode.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "间隔编码不能超过200个字符！");
        }
        accountDto.setSpaceCode(spaceCode);

        String deviceType = StrUtil.asString(rowData.get(6));
        if (StringUtils.isEmpty(deviceType)) {
            throw new ServiceException(SysErrorCode.defaultError, "设备类型不能为空！");
        }
        Integer deviceTypeEnum = AccountDto.DeviceType.getValueByText(deviceType);
        if (deviceTypeEnum == null) {
            throw new ServiceException(SysErrorCode.defaultError, "设备类型错误！");
        }
        accountDto.setDeviceType(deviceTypeEnum);

        String deviceName = StrUtil.asString(rowData.get(8));
        if (StringUtils.isEmpty(deviceName)) {
            throw new ServiceException(SysErrorCode.defaultError, "设备名称不能为空！");
        }
        if (deviceName.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "设备名称不能超过200个字符！");
        }
        accountDto.setDeviceName(deviceName);

        String deviceCode = StrUtil.asString(rowData.get(9));
        if (deviceCode.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "设备编码不能超过200个字符！");
        }
        accountDto.setDeviceCode(deviceCode);

        String voltageLevel = StrUtil.asString(rowData.get(10)).toUpperCase().replace("交流", "").replace("kV", "");
        if (StringUtils.isEmpty(voltageLevel)) {
            throw new ServiceException(SysErrorCode.defaultError, "电压等级不能为空！");
        }
        accountDto.setVoltageLevel(Integer.valueOf(voltageLevel));

        String manufacturer = StrUtil.asString(rowData.get(11));
        if (manufacturer.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "制造商名称不能超过200个字符！");
        }
        accountDto.setManufacturer(manufacturer);

        String deviceVersion = StrUtil.asString(rowData.get(12));
        if (StringUtils.isEmpty(deviceVersion)) {
            throw new ServiceException(SysErrorCode.defaultError, "设备型号不能为空！");
        }
        if (deviceVersion.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "设备型号不能超过200个字符！");
        }
        accountDto.setDeviceVersion(deviceVersion);

        String runDept = StrUtil.asString(rowData.get(13));
        if (runDept.length() > 200) {
            throw new ServiceException(SysErrorCode.defaultError, "运行单位不能超过200个字符！");
        }
        accountDto.setRunDept(runDept);

        String useDate = StrUtil.asString(rowData.get(14));
        if (StringUtils.isNotEmpty(useDate)) {
            accountDto.setUseDate(DateUtil.parseDate(useDate));
        }
        String produceDate = StrUtil.asString(rowData.get(15));
        if (StringUtils.isNotEmpty(produceDate)) {
            accountDto.setProduceDate(DateUtil.parseDate(produceDate));
        }

        String runState = StrUtil.asString(rowData.get(16));
        if (StringUtils.isEmpty(runState)) {
            throw new ServiceException(SysErrorCode.defaultError, "运行状态不能为空！");
        }
        accountDto.setRunState(runState);

        if (rowData.size() >= 18) {
            String outageDate = StrUtil.asString(rowData.get(17));
            if (StringUtils.isNotEmpty(outageDate)) {
                accountDto.setOutageDate(DateUtil.parseDate(outageDate));
            }
        }
        return accountDto;
    }

    /**
     * 查询指定台帐的上一个台帐
     *
     * @param dto
     * @return
     * @throws Exception
     */
    public AccountDto getPrevAccount(AccountDto dto) throws Exception {
        AccountDto result = null;
        try {
            Account entity = copyTo(dto, Account.class);
            result = copyTo(accountDao.getPrevAccount(entity), AccountDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    /**
     * 查询指定台帐的下一个台帐
     *
     * @param dto
     * @return
     * @throws Exception
     */
    public AccountDto getNextAccount(AccountDto dto) throws Exception {
        AccountDto result = null;
        try {
            Account entity = copyTo(dto, Account.class);
            result = copyTo(accountDao.getNextAccount(entity), AccountDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(AccountDto dto) {
        Response result = new Response(0, "success");
        try {
            Account entity = copyTo(dto, Account.class);
            //判断数据是否存在
            if (accountDao.isDataYN(entity) != 0) {
                //数据存在
                accountDao.update(entity);
            } else {
                //新增
                entity.setId(IdUtil.nextId());
                accountDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    public String deleteData(AccountDto dto) {
        String result = "success";
        try {
            Account entity = copyTo(dto, Account.class);
            if (accountDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    public PageInfo findDataIsPage(AccountDto dto) {
        PageInfo pageInfo = null;
        try {
            Account entity = copyTo(dto, Account.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = accountDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), AccountDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    public List<AccountDto> findDataIsList(AccountDto dto) {
        List<AccountDto> results = null;
        try {
            Account entity = copyTo(dto, Account.class);
            results = copyTo(accountDao.findDataIsList(entity), AccountDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public List<AccountDto> queryAccountList(Map param) {
        List<AccountDto> results = null;
        try {
            results = copyTo(accountDao.queryAccountList(param), AccountDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public AccountDto findDataById(Long id) {
        AccountDto result = null;
        try {
            AccountDto dto = new AccountDto() {{
                setId(id);
            }};
            Account entity = copyTo(dto, Account.class);
            AccountDto accountDto = copyTo(accountDao.selectByPrimaryKey(entity), AccountDto.class);
            if (accountSiteInfoDao.isDataConfig(accountDto.getId()) != 0) accountDto.setConfigState(1);
            else accountDto.setConfigState(0);
            result = accountDto;
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<AccountDto> findDataIsListDirId(AccountDto dto) throws Exception {
        List<AccountDto> result;
        try {
            if (null == dto) {
                throw new RuntimeException("台帐父节点ID不能更新!");
            } else {
                Account entity = copyTo(dto, Account.class);
                List<AccountDto> dtos = copyTo(accountDao.findDataIsList(entity), AccountDto.class);
                for (AccountDto accountDto : dtos) {
                    if (accountSiteInfoDao.isDataConfig(accountDto.getId()) != 0) accountDto.setConfigState(1);
                    else accountDto.setConfigState(0);
                }
                result = dtos;
            }
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public List<AccountDto> findDataListBySubstation(AccountDto dto) {
        List<AccountDto> results = null;
        try {
            Account entity = copyTo(dto, Account.class);
            results = copyTo(accountDao.findDataListBySubstation(entity), AccountDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public AccountDto findDataByFullName(AccountDto dto) throws Exception {
        AccountDto result = null;
        try {
            Account entity = copyTo(dto, Account.class);
            result = copyTo(accountDao.findDataByFullName(entity), AccountDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    @ALogOperation(type = "备份台账", desc = "台账建设")
    public Response backupAccount(File dir, ZTreeNodeDto zTreeNodeDto) throws Exception {
        try {
            //查询所有需要导出的台帐
            Map<String, Object> queryParam = new HashMap<>();
            if (zTreeNodeDto != null) {
                String fullName = zTreeNodeDto.getFullName();
                if (ValidatorUtil.notEmpty(fullName)) {
                    String[] paramArr = fullName.split(CommonConstant.FEIGN_ERROR_SYMBOL_STRING);
                    for (int i = 0; i < paramArr.length; i++) {
                        switch (i) {
                            case 0: {
                                queryParam.put("electricBureau", paramArr[i]);
                                break;
                            }
                            case 1: {
                                queryParam.put("substation", paramArr[i]);
                                break;
                            }
                            case 2: {
                                queryParam.put("voltageLevel", paramArr[i].replace("kV", ""));
                                break;
                            }
                            case 3: {
                                queryParam.put("deviceType", AccountDto.DeviceType.getValueByText(paramArr[i]));
                                break;
                            }
                            case 4: {
                                queryParam.put("deviceName", paramArr[i]);
                                break;
                            }
                        }
                    }
                }
            }
            List<Account> accountList = accountDao.findAccountList(queryParam);
            if (accountList != null) {
                JSONWriter writer = new JSONWriter(new FileWriter(dir + "/台帐数据_" + DateUtil.getCurDateStr("yyyyMMddHHmmss") + ".bak"));
                writer.startArray();
                for (Account account : accountList) {
                    account.setSiteList((List<AccountSiteInfo>) accountSiteInfoDao.findDataIsList(new AccountSiteInfo() {{
                        setAccountId("" + account.getId());
                    }}));
                    writer.writeValue(account);
                }
                writer.endArray();
                writer.close();
            }
        } catch (Exception e) {
            log.error("台账备份失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return new Response();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    @ALogOperation(type = "还原台账", desc = "台账建设")
    public Response restoreAccount(File file) throws Exception {
        Response result = new Response();
        int repeatCount = 0;
        int newCount = 0;
        try {
            String fileName = file.getName();
            String fileType = fileName.substring(fileName.lastIndexOf(".") + 1, fileName.length());
            if (!"bak".equalsIgnoreCase(fileType)) {
                throw new Exception("文件格式错误！");
            }
            List<Account> restoreAccountList=new ArrayList<>();
            JSONReader reader = new JSONReader(new FileReader(file.getPath()));
            reader.startArray();
            while (reader.hasNext()) {
                restoreAccountList.add(reader.readObject(Account.class));
            }
            reader.endArray();
            reader.close();
            //创建台帐目录，设置排序号
            List<Account> oldAccountList = (List<Account>) accountDao.findDataIsList(null);
            for (int i = 0; i < restoreAccountList.size(); i++) {
                Account account = restoreAccountList.get(i);
                //查找重复
                Account oldAccount = oldAccountList.stream()
                        .filter(old -> old.getElectricBureau().equals(account.getElectricBureau())
                                && old.getSubstation().equals(account.getSubstation())
                                && old.getDeviceType().equals(account.getDeviceType())
                                && old.getVoltageLevel().equals(account.getVoltageLevel())
                                && old.getDeviceName().equals(account.getDeviceName())
                        ).findFirst().orElse(null);
                if (oldAccount != null) {
                    repeatCount++;
                    continue;
                }
                newCount++;
                //查找和当前遍历对象相同目录的排序号最大的台帐
                Account oldMaxAccount = oldAccountList.stream()
                        .filter(old -> old.getElectricBureau().equals(account.getElectricBureau()) && old.getSubstation().equals(account.getSubstation()) &&
                                old.getDeviceType().equals(account.getDeviceType()) && old.getVoltageLevel().equals(account.getVoltageLevel()))
                        .max(Comparator.comparing(Account::getSortNum)).orElse(null);
                if (oldMaxAccount == null) {
//                    account.setDirId(accountDirService.createDir(account));
                    account.setSortNum(1);
                } else {
                    account.setDirId(oldMaxAccount.getDirId());
                    account.setSortNum(oldMaxAccount.getSortNum() + 1);
                }
                oldAccountList.add(account);
                //----------------------------------------------------------
                accountDao.insert(account);
                accountSiteInfoDao.insertBatch(account.getSiteList());
            }
        } finally {

        }
        result.message = "新入库" + newCount + "条,历史存在:" + repeatCount + "条";
        return result;
    }

}
