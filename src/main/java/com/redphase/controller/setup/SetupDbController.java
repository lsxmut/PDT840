package com.redphase.controller.setup;

import com.redphase.api.setup.ISetupService;
import com.redphase.api.setup.ISqlitBackupService;
import com.redphase.controller.BaseController;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.ValidatorUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class SetupDbController extends BaseController implements Initializable {

    @Autowired
    private ISetupService setupService;
    @Autowired
    private ISqlitBackupService sqlitBackupService;
    @FXML
    private TextField dbBackName;
    @FXML
    private TextField dbBackDir;
    @FXML
    private TextField dbBackUrl;
    private final String DB_BACK_NAME_TEXT = "DB_BACK_NAME_TEXT";
    private final String DB_BACK_DIR_TEXT = "DB_BACK_DIR_TEXT";
    private final String DB_BACK_URL_TEXT = "DB_BACK_URL_TEXT";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");

        dbBackDir.setEditable(false);
        dbBackDir.setMouseTransparent(true);
        dbBackDir.setFocusTraversable(false);

        dbBackUrl.setEditable(false);
        dbBackUrl.setMouseTransparent(true);
        dbBackUrl.setFocusTraversable(false);

        dbBackName.setText("dbbackup-" + DateUtil.getCurDateStr("yyyyMMddHHmmsss") + ".sql");
    }

    @FXML
    public void backUp() {
        log.info("backUp..");
        if (ValidatorUtil.isEmpty(dbBackName.getText())) {
            ialert.error("备份名称必填!");
            return;
        }
        if (ValidatorUtil.isEmpty(dbBackDir.getText())) {
            ialert.error("备份文件夹必填!");
            return;
        }
        ehcacheUtil.setCache(DB_BACK_NAME_TEXT, dbBackName.getText());
        ehcacheUtil.setCache(DB_BACK_DIR_TEXT, dbBackDir.getText());

        try {
            sqlitBackupService.backup(dbBackDir.getText() + "/" + dbBackName.getText());
            ialert.success("数据备份成功!");
        } catch (Exception e) {
            log.error("数据库备份异常!", e);
            ialert.warning("系统异常!" + e.getMessage());
        }
    }

    @FXML
    public void restore() {
        log.info("restore..");
        if (ValidatorUtil.isEmpty(dbBackUrl.getText())) {
            ialert.error("请先选择备份文件!");
            return;
        }

        ehcacheUtil.setCache(DB_BACK_URL_TEXT, dbBackUrl.getText());
        try {
            try {
                //为了防止误操作,数据库还原操作前备份当前数据库
                //1.备份路径
                String backupPath = sqlitBackupService.getPath() + "/dbbackup";
                File backupDir = new File(backupPath);
                if (!backupDir.exists()) {
                    backupDir.mkdirs();
                }
                sqlitBackupService.backup(backupPath + "/hx-" + DateUtil.getCurDateStr("yyyyMMddHHmmsss") + ".sql");
            } catch (Exception e) {
                log.error("备份现有数据库异常!", e);
                ialert.error("备份现有数据库异常!");
            }

            sqlitBackupService.restore(dbBackUrl.getText());
            ialert.success("数据已恢复成功!");
        } catch (Exception e) {
            log.error("数据库还原异常!", e);
            ialert.warning("系统异常!" + e.getMessage());
        }
    }

    @FXML
    public void selectBackDir() {
        String path = directoryChooser("请选择库备份存放文件夹", (String) ehcacheUtil.getCache(DB_BACK_DIR_TEXT));
        if (ValidatorUtil.notEmpty(path)) {
            dbBackDir.setText(path);
            ehcacheUtil.setCache(DB_BACK_DIR_TEXT, dbBackDir.getText());
        }
    }

    @FXML
    public void selectBackFile() {
        String oldDirPath = (String) ehcacheUtil.getCache(DB_BACK_URL_TEXT);
        if (ValidatorUtil.notEmpty(oldDirPath)) {
            oldDirPath = oldDirPath.substring(0, oldDirPath.lastIndexOf(File.separator));
        }
        String path = fileChooser("请选择库备份文件", oldDirPath, "backup sql files (*.sql)", "*.sql");
        if (ValidatorUtil.notEmpty(path)) {
            dbBackUrl.setText(path);
            ehcacheUtil.setCache(DB_BACK_URL_TEXT, dbBackUrl.getText());
        }
    }

    @Override
    public void dispose() {

    }
}