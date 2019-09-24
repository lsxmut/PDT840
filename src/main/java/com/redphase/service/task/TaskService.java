package com.redphase.service.task;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.redphase.api.task.ITaskDetailService;
import com.redphase.api.task.ITaskHistoryService;
import com.redphase.api.task.ITaskService;
import com.redphase.dao.master.account.IAccountDao;
import com.redphase.dao.master.task.IAccountSiteInfoDao;
import com.redphase.dao.master.task.ITaskDao;
import com.redphase.dao.master.task.ITaskDetailDao;
import com.redphase.dto.account.AccountDto;
import com.redphase.dto.sys.SysVariableDto;
import com.redphase.dto.task.*;
import com.redphase.entity.account.Account;
import com.redphase.entity.task.Task;
import com.redphase.framework.Response;
import com.redphase.framework.SysErrorCode;
import com.redphase.framework.annotation.ALogOperation;
import com.redphase.framework.exception.ServiceException;
import com.redphase.framework.service.BaseService;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.I18nUtil;
import com.redphase.framework.util.IdUtil;
import com.redphase.service.account.AccountService;
import com.redphase.service.setup.SetupService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
public class TaskService extends BaseService implements ITaskService {
    @Autowired
    private ITaskDao taskDao;
    @Autowired
    private AccountService accountService;
    @Autowired
    private ITaskDetailService taskDetailService;
    @Autowired
    private SetupService setupService;
    @Resource
    private IAccountDao accountDao;
    @Resource
    private ITaskDetailDao taskDetailDao;
    @Resource
    private IAccountSiteInfoDao iAccountSiteInfoDao;
    @Resource
    private ITaskHistoryService taskHistoryService;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response batchSave(List<TaskDto> taskDtos) throws ServiceException {
        Response result = new Response(0, "success");
        Date date = new Date();
        try {
            for (TaskDto taskDto : taskDtos) {
                taskDto.setDateCreated(date);
                taskDto.setDateUpdated(date);
                taskDto.setBiUpdateTs(date);
                String id = (String) saveOrUpdateData(taskDto).data;
                //保存task获取ID;
                for (TaskDetailDto detailDto : taskDto.getTaskDetailDtos()) {
                    //保存detailDto
                    detailDto.setTaskId(id);
                    taskDetailService.saveOrUpdateData(detailDto);
                }
            }
        } catch (Exception e) {
            log.error("批量保存失败!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    @ALogOperation(type = "创建任务单", desc = "任务单")
    public Response createFile(List<TaskDto> taskDtos) throws ServiceException {
        Response result = new Response(0, "success");
        String basePath;
        try {
            log.debug("================创建任务单文件开始================");
            basePath = subPath(setupService.getVariableByCode(new SysVariableDto() {{
                setCode("i-path-data");
            }}).getValue());
            TaskDto taskDto = taskDtos.get(0);
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            String dateDetection = formatDate.format(taskDto.getBiUpdateTs());
            if (taskDto.getDeviceType() == TaskDto.DeviceType.SWITCH_CUBICLE.getValue()) {
                String val = loadSwitchText(taskDtos, taskDto.getBiUpdateTs());
                File file = createSwitchFileInfo(taskDto, basePath);
                writerFile(file, val);
                taskHistoryService.saveOrUpdateData(new TaskHistoryDto() {{
                    setDateDetection(dateDetection);//检测日期
                    setTestTechnology("TEV");//检测技术 0 TEV
                    setElectricBureau(taskDto.getElectricBureau());//电业局
                    setSubstation(taskDto.getSubstation());//变电站
                    setVoltageLevel(taskDto.getVoltageLevel());//电压等级
                    setDeviceType(taskDto.getDeviceType());//设备类型
                    setFileName(file.getName());//文件名称
                    setFilePath(file.getPath());//文件路径
                }});
            } else {
                List<TaskFileDto> fileDtos = loadTaskFileDto(taskDtos, taskDto.getBiUpdateTs(), basePath);
                for (TaskFileDto taskFileDto : fileDtos) {
                    File file = createFile(taskFileDto.getFilePath(), taskFileDto.getFileName(), taskFileDto.getTestFilePath());
                    String val = loadOtherText(taskFileDto);
                    writerFile(file, val);
                    taskHistoryService.saveOrUpdateData(new TaskHistoryDto() {{
                        setDateDetection(dateDetection);//检测日期
                        setTestTechnology(taskFileDto.getTestTechnology());//检测技术 0 TEV
                        setElectricBureau(taskDto.getElectricBureau());//电业局
                        setSubstation(taskDto.getSubstation());//变电站
                        setVoltageLevel(taskDto.getVoltageLevel());//电压等级
                        setDeviceType(taskDto.getDeviceType());//设备类型
                        setFileName(file.getName());//文件名称
                        setFilePath(file.getPath());//文件路径
                    }});
                }
            }
            log.debug("================创建任务单文件完成================");
        } catch (Exception e) {
            log.error("文件生成异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.DEFAULT, timeout = CommonConstant.DB_DEFAULT_TIMEOUT, rollbackFor = {Exception.class, RuntimeException.class})
    public Response saveOrUpdateData(TaskDto dto) throws ServiceException {
        Response result = new Response(0, "success");
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            Task entity = copyTo(dto, Task.class);
            //判断数据是否存在
            if (taskDao.isDataYN(entity) != 0) {
                //数据存在
                taskDao.update(entity);
            } else {
                //新增
                entity.setId(String.valueOf(IdUtil.nextId()));
                taskDao.insert(entity);
                result.data = entity.getId();
            }
        } catch (Exception e) {
            log.error("信息保存异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public String deleteData(TaskDto dto) throws ServiceException {
        String result = "success";
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            Task entity = copyTo(dto, Task.class);
            if (taskDao.deleteByPrimaryKey(entity) == 0) {
                throw new RuntimeException(I18nUtil.get("error.dataNotFound"));
            }
        } catch (Exception e) {
            log.error("物理删除异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public PageInfo findDataIsPage(TaskDto dto) throws ServiceException {
        PageInfo pageInfo;
        try {
            if (dto == null) throw new RuntimeException(I18nUtil.get("error.paramError"));
            Task entity = copyTo(dto, Task.class);
            PageHelper.startPage(PN(dto.getPageNum()), PS(dto.getPageSize()));
            List list = taskDao.findDataIsPage(entity);
            pageInfo = new PageInfo(list);
            pageInfo.setList(copyTo(pageInfo.getList(), TaskDto.class));
        } catch (Exception e) {
            log.error("信息[分页]查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return pageInfo;
    }

    @Override
    public List<TaskDto> findDataIsList(TaskDto dto) throws ServiceException {
        List<TaskDto> results;
        try {
            Task entity = copyTo(dto, Task.class);
            results = copyTo(taskDao.findDataIsList(entity), TaskDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return results;
    }

    @Override
    public TaskDto findDataById(TaskDto dto) throws ServiceException {
        TaskDto result;
        try {
            Task entity = copyTo(dto, Task.class);
            result = copyTo(taskDao.selectByPrimaryKey(entity), TaskDto.class);
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return result;
    }

    @Override
    public HashMap<String, Object> importTask(File file) throws ServiceException {
        HashMap<String, Object> map = new HashMap<>();
        try {
            log.debug("===============导入任务单开始===============");
            String fileName = file.getName();
            if (fileName.contains("UHF") || fileName.contains("AE") || fileName.contains("HFCT")) {    //组合项目
                String substation = fileName.substring(0, fileName.indexOf("_"));
                String voltageLevel = fileName.substring(fileName.indexOf("_") + 1, substation.length() + fileName.substring(fileName.indexOf("_"), fileName.length()).indexOf("kV"));
                Integer deviceType = AccountDto.DeviceType.getValueByText(fileName.substring(substation.length() + fileName.substring(fileName.indexOf("_"), fileName.length()).indexOf("kV") + 2, fileName.indexOf("_", fileName.indexOf("_") + 1)));
                String deviceName = fileName.substring(fileName.lastIndexOf("_") + 1, fileName.indexOf(".txt"));
                List<String> valList = readLinedFile(file);
                String dateStr = valList.get(3);
                String timeStr = valList.get(4);
                AccountDto accountDto = new AccountDto() {{
                    setSubstation(substation);
                    setVoltageLevel(new Integer(voltageLevel));
                    setDeviceType(deviceType);
                    setDeviceName(deviceName);
                    setBiUpdateTsStr(dateStr.substring(0, dateStr.indexOf("。")) + " " + timeStr.substring(0, timeStr.indexOf("。")));
                }};
                Account account = accountDao.selectByOtherFileName(accountDto);
                if (account == null) {
                    throw new RuntimeException("该任务单对应的台帐已修改或删除,无法导入!");
                }
                TaskDto taskDto = new TaskDto() {{
                    setAccountId(account.getId().toString());
                    setBiUpdateTsStr(accountDto.getBiUpdateTsStr());
                }};
                List<TaskHistoryConfigDto> configDtos = taskDetailDao.findDataIsListByTask(taskDto);
                for (int i = 0; i < configDtos.size(); i++) {
                    TaskHistoryConfigDto configDto = configDtos.get(i);
                    AccountSiteInfoDto infoDto = new AccountSiteInfoDto() {{
                        setAccountId(configDto.getAccountId());
                        setSiteName(configDto.getSiteName());
                        setTestTechnology(configDto.getTestTechnology());
                    }};
                    configDto.setCheckboxValue(configDto.getSiteName() + iAccountSiteInfoDao.findNumByAccountSiteInfo(infoDto));
                }
                List<AccountDto> accounts = new ArrayList<>();
                accounts.add(copyTo(account, AccountDto.class));
                for (AccountDto dto : accounts) {
                    if (iAccountSiteInfoDao.isDataConfig(dto.getId()) != 0) dto.setConfigState(1);
                    else dto.setConfigState(0);
                }
                map.put("account", accounts);
                map.put("config", new ArrayList<List<TaskHistoryConfigDto>>() {{
                    add(configDtos);
                }});
            } else {                                                          //开关柜
                List<String> valList = readLinedFile(file);
                String path = file.getPath();
                String electricBureauStr = path.substring(0, path.indexOf("\\局部放电测试任务"));
                String electricBureau = electricBureauStr.substring(electricBureauStr.lastIndexOf("\\") + 1, electricBureauStr.length());
                String substation = fileName.substring(0, fileName.indexOf("_"));
                String substation1 = fileName.substring(fileName.indexOf("_") + 1, fileName.length());
                String voltageLevel = substation1.substring(0, substation1.indexOf("kV"));
                Integer deviceType = AccountDto.DeviceType.getValueByText(substation1.substring(substation1.indexOf("kV") + 2, substation1.lastIndexOf(".txt")));
                String dateStr = valList.get(2);
                String timeStr = valList.get(3);
                AccountDto accountDto = new AccountDto() {{
                    setElectricBureau(electricBureau);
                    setSubstation(substation);
                    setVoltageLevel(new Integer(voltageLevel));
                    setDeviceType(deviceType);
                    setBiUpdateTsStr(dateStr.substring(0, dateStr.length() - 1) + " " + timeStr.substring(0, timeStr.length() - 1));
                }};
                List<Account> accounts = accountDao.selectBySwitchFileName(accountDto);
                if (accounts == null || accounts.size() <= 0) {
                    throw new RuntimeException("该任务单对应的台帐已修改或删除,无法导入!");
                }
                List<AccountDto> accountDtos = copyTo(accounts, AccountDto.class);
                List<List<TaskHistoryConfigDto>> configDtos = new ArrayList<>();
                for (AccountDto dto : accountDtos) {
                    TaskDto taskDto = new TaskDto() {{
                        setAccountId(dto.getId().toString());
                        setBiUpdateTsStr(accountDto.getBiUpdateTsStr());
                    }};
                    configDtos.add(taskDetailDao.findDataIsListByTask(taskDto));
                }
                for (AccountDto dto : accountDtos) {
                    if (iAccountSiteInfoDao.isDataConfig(dto.getId()) != 0) dto.setConfigState(1);
                    else dto.setConfigState(0);
                }
                map.put("account", accountDtos);
                map.put("config", configDtos);
                log.info("===============导入任务单完成===============");
            }
        } catch (Exception e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return map;
    }

    /**
     * 读取任务单文件行
     *
     * @param file
     * @return
     */
    private List<String> readLinedFile(File file) throws ServiceException {
        List<String> filecon = new ArrayList<>();
        try {
            String m;
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((m = reader.readLine()) != null) {
                if (!m.equals("")) { // 不需要读取空行
                    filecon.add(m);
                }
            }
            reader.close();
        } catch (IOException e) {
            log.error("查询异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return filecon;
    }

    /**
     * 装载任务文件
     *
     * @param taskDtos
     * @param date
     * @param basePath
     * @return
     */
    private List<TaskFileDto> loadTaskFileDto(List<TaskDto> taskDtos, Date date, String basePath) throws ServiceException {
        List<TaskFileDto> fileDtos = new ArrayList<>();
        try {
            for (TaskDto taskDto : taskDtos) {
                SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
                AccountDto accountDto = accountService.findDataById(Long.valueOf(taskDto.getAccountId()));
                String testTechnology = taskDto.getTaskDetailDtos().get(0).getTestTechnology();
                List<TaskDetailDto> dtos = taskDto.getTaskDetailDtos();
                List<TaskDetailDto> detailDtos = new ArrayList<>();
                TaskDetailDto detailDto;
                String path, filePath, testFilePath, fileName;
                TaskFileDto fileDto;
                List<TaskDetailDto> emp;
                dtos.sort((o1, o2) -> {
                            if (new Integer(o1.getTestTechnology()) < new Integer(o2.getTestTechnology()))
                                return 1;
                            else if (new Integer(o1.getTestTechnology()) > new Integer(o2.getTestTechnology()))
                                return -1;
                            else
                                return 0;
                        }
                );
                for (int i = 0; i < dtos.size(); i++) {
                    detailDto = dtos.get(i);
                    if (!testTechnology.equals(detailDto.getTestTechnology())) {
                        path = basePath + accountDto.getElectricBureau() + "/局部放电测试任务" + formatDate.format(date) + "/" +
                                accountDto.getSubstation() + "/" + AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType());
                        filePath = path + "/任务单目录";
                        testFilePath = path + "/检测数据";
                        fileName = accountDto.getSubstation() + "_" + accountDto.getVoltageLevel() + "kV" +
                                AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType()) + "_" +
                                TaskDetailDto.TestTechnology.getTextByValue(Integer.valueOf(testTechnology)) + "_" +
                                accountDto.getDeviceName() + ".txt";
                        fileDto = new TaskFileDto();
                        fileDto.setFilePath(filePath);
                        fileDto.setTestFilePath(testFilePath);
                        fileDto.setFileName(fileName);
                        fileDto.setTestTechnology(TaskDetailDto.TestTechnology.getTextByValue(new Integer(testTechnology)));
                        fileDto.setVoltage(accountDto.getVoltageLevel() + "kV");
                        fileDto.setDeviceName(accountDto.getDeviceName());
                        fileDto.setDateStr(formatDate.format(date));
                        fileDto.setTimeStr(formatTime.format(date));
                        fileDto.setDeviceTypeName(accountDto.getDeviceName());
                        fileDto.setSubstationName(accountDto.getSubstation());
                        emp = new ArrayList<>();
                        emp.addAll(detailDtos);
                        fileDto.setTaskDetailDtos(emp);
                        detailDtos.clear();
                        testTechnology = detailDto.getTestTechnology();
                        fileDtos.add(fileDto);
                    }
                    detailDtos.add(detailDto);
                }
                path = basePath + accountDto.getElectricBureau() + "/局部放电测试任务" + formatDate.format(date) + "/" +
                        accountDto.getSubstation() + "/" + AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType());
                filePath = path + "/任务单目录";
                testFilePath = path + "/检测数据";
                fileName = accountDto.getSubstation() + "_" + accountDto.getVoltageLevel() + "kV" +
                        AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType()) + "_" +
                        TaskDetailDto.TestTechnology.getTextByValue(Integer.valueOf(testTechnology)) + "_" +
                        accountDto.getDeviceName() + ".txt";
                fileDto = new TaskFileDto();
                fileDto.setFilePath(filePath);
                fileDto.setTestFilePath(testFilePath);
                fileDto.setFileName(fileName);
                fileDto.setTestTechnology(TaskDetailDto.TestTechnology.getTextByValue(new Integer(testTechnology)));
                fileDto.setVoltage(accountDto.getVoltageLevel() + "kV");
                fileDto.setDeviceName(accountDto.getDeviceName());
                fileDto.setDateStr(formatDate.format(date));
                fileDto.setTimeStr(formatTime.format(date));
                fileDto.setDeviceTypeName(accountDto.getDeviceName());
                fileDto.setSubstationName(accountDto.getSubstation());
                fileDto.setTaskDetailDtos(detailDtos);
                fileDtos.add(fileDto);
            }
        } catch (Exception e) {
            log.error("装载任务文件dto异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return fileDtos;
    }

    /**
     * 创建开关柜文件信息
     *
     * @param taskDto
     * @param basePath
     * @return
     */
    private File createSwitchFileInfo(TaskDto taskDto, String basePath) throws ServiceException {
        String path, filePath, testFilePath, fileName;
        try {
            AccountDto dto = new AccountDto();
            dto.setId(Long.valueOf(taskDto.getAccountId()));
            AccountDto accountDto = accountService.findDataById(Long.valueOf(taskDto.getAccountId()));
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            String datestr = format.format(new Date());
            //path表示你所创建文件的路径
            path = basePath + accountDto.getElectricBureau() + "/局部放电测试任务" + datestr + "/" + accountDto.getSubstation() + "/" + AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType());
            filePath = path + "/任务单目录";
            testFilePath = path + "/检测数据";
            fileName = accountDto.getSubstation() + "_" + accountDto.getVoltageLevel() + "kV" + AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType()) + ".txt";
        } catch (Exception e) {
            log.error("开关柜生成文件路径及文件名称异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }

        return createFile(filePath, fileName, testFilePath);
    }

    /**
     * 补全路径
     *
     * @param path
     * @return
     */
    private String subPath(String path) throws ServiceException {
        try {
            if (!"/".equals(path.substring(path.length() - 1, path.length()))) path += "/";
        } catch (Exception e) {
            log.error("补全路径异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return path;
    }

    /**
     * 创建组合项目文件
     *
     * @param path
     * @param fileName
     * @return
     */
    private File createFile(String path, String fileName, String testFilePath) throws ServiceException {
        File file;
        try {
            File tf = new File(testFilePath);
            if (!tf.exists()) tf.mkdirs();
            File f = new File(path);
            if (!f.exists()) f.mkdirs();
            file = new File(f, fileName);
            if (!file.exists()) file.createNewFile();
        } catch (Exception e) {
            log.error("创建组合项目文件异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return file;
    }

    /**
     * 装载文件
     *
     * @param file
     * @param value
     */
    private void writerFile(File file, String value) throws ServiceException {
        FileWriter fw;
        BufferedWriter bw;
        try {
            if (!file.exists()) file.createNewFile();
            fw = new FileWriter(file);  //true表示可以追加新内容
            bw = new BufferedWriter(fw);
            bw.write(value);
            bw.close();
        } catch (IOException e) {
            log.error("装载文件异常!", e);
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
    }

    /**
     * 生成开关柜的文件内容
     *
     * @param dtos
     * @param date
     * @return
     */
    private String loadSwitchText(List<TaskDto> dtos, Date date) throws ServiceException {
        StringBuilder builder = new StringBuilder();
        try {
            AccountDto accDto = new AccountDto();
            accDto.setId(Long.valueOf(dtos.get(0).getAccountId()));
            AccountDto accountDto = accountService.findDataById(Long.valueOf(dtos.get(0).getAccountId()));
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            builder.append(accountDto.getSubstation());
            builder.append("。\r\n");
            builder.append(accountDto.getVoltageLevel() +
                    AccountDto.DeviceType.getTextByValue(accountDto.getDeviceType()));
            builder.append("。\r\n");
            builder.append(formatDate.format(date));
            builder.append("。\r\n");
            builder.append(formatTime.format(date));
            builder.append("。\r\n");
            for (TaskDto dto : dtos) {
                builder.append(dto.getDeviceName());
                builder.append("：\r\n");
                List<TaskDetailDto> taskDetailDtos = dto.getTaskDetailDtos();
//                taskDetailDtos.sort(Comparator.comparing(TaskDetailDto::getSiteName));
                for (int i = 0; i < taskDetailDtos.size(); i++) {
                    TaskDetailDto detailDto = taskDetailDtos.get(i);
                    if (i == taskDetailDtos.size() - 1) {
                        builder.append(detailDto.getSiteName());
                        builder.append("。\r\n");
                        break;
                    }
                    builder.append(detailDto.getSiteName());
                    builder.append("；\r\n");
                }
            }
        } catch (Exception e) {
            log.error("生成开关柜的文件内容异常!");
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return builder.toString();
    }

    /**
     * 装载其他项目数据
     *
     * @param taskFileDto
     * @return
     */
    private String loadOtherText(TaskFileDto taskFileDto) throws ServiceException {
        StringBuilder value = new StringBuilder();
        try {
            value.append(taskFileDto.getSubstationName());
            value.append("。\r\n");
            value.append(taskFileDto.getVoltage());
            value.append(taskFileDto.getDeviceTypeName());
            value.append("。\r\n");
            value.append(taskFileDto.getTestTechnology());
            value.append("。\r\n");
            value.append(taskFileDto.getDateStr());
            value.append("。\r\n");
            value.append(taskFileDto.getTimeStr());
            value.append("。\r\n");
            value.append(taskFileDto.getDeviceName());
            value.append("：\r\n");
            for (int i = 0; i < taskFileDto.getTaskDetailDtos().size(); i++) {
                TaskDetailDto detailDto = taskFileDto.getTaskDetailDtos().get(i);
                if (i == taskFileDto.getTaskDetailDtos().size() - 1) {
                    value.append(detailDto.getSiteName());
                    value.append("。\r\n");
                    break;
                }
                value.append(detailDto.getSiteName());
                value.append("；\r\n");
            }
        } catch (Exception e) {
            log.error("装载其他项目数据异常!");
            throw new ServiceException(SysErrorCode.defaultError, e.getMessage());
        }
        return value.toString();
    }
}