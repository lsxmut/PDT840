package com.redphase.controller.user;

import com.redphase.controller.BaseController;
import com.redphase.dto.user.LogOperationDto;
import com.redphase.framework.PageDto;
import com.redphase.framework.Response;
import com.redphase.framework.util.CommonConstant;
import com.redphase.framework.util.DateUtil;
import com.redphase.framework.util.I18nUtil;
import com.redphase.service.user.LogOperationService;
import com.redphase.view.DataButtonCell;
import com.redphase.view.DataTableCell;
import com.redphase.view.PageBar;
import com.redphase.view.user.OperactionInfoView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
@Data
public class LogOperationController extends BaseController implements Initializable {

    @Autowired
    private LogOperationService logOperationService;
    @FXML
    TableView<LogOperationDto> dataTableView;
    @FXML
    TableColumn seqTableColumn;//序号
    @FXML
    TableColumn createNameTableColumn;//操作人员
    @FXML
    TableColumn typeTableColumn;//操作类型
    @FXML
    TableColumn memoTableColumn;//操作描述
    @FXML
    TableColumn dateCreatedTableColumn;//操作时间
    @FXML
    TableColumn operTableColumn;//操作

    @FXML
    DatePicker dateBeginT;
    @FXML
    DatePicker dateEndT;
    @FXML
    TextField keywordT;
    @Autowired
    OperactionInfoView operactionInfoView;
    @FXML
    Label createNameL;
    @FXML
    Label dateCreatedL;
    @FXML
    Label typeL;
    @FXML
    Label memoL;
    @FXML
    Label detailInfoL;

    Integer pageNum;
    @FXML
    FlowPane pageBar;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
        pageNum = 1;
        loadPage();
    }

    /**
     * <p> 信息分页 (未删除)。
     */
//    @RequiresPermissions("alog:page")
    @FXML
    public void loadPage() {
        log.info("loadPage.........");
        try {
            ObservableList<LogOperationDto> dataList = FXCollections.observableArrayList();
            LogOperationDto dto = new LogOperationDto() {{
                setPageSize(CommonConstant.PAGEROW_DEFAULT_COUNT);
                setPageNum(pageNum);
                if (dateBeginT.getValue() != null)
                    setDateBegin(dateBeginT.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                if (dateEndT.getValue() != null)
                    setDateEnd(dateEndT.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                setKeyword(keywordT.getText());
            }};
            PageDto pageDto = getPageDto(logOperationService.findDataIsPage(dto));
            dataList.addAll(pageDto.getData());
            dataTableView.setItems(dataList);
            seqTableColumn.setCellFactory((col) -> {
                TableCell<LogOperationDto, String> cell = new TableCell<LogOperationDto, String>() {
                    @Override
                    public void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        this.setText(null);
                        this.setGraphic(null);
                        this.setStyle("-fx-pref-width: 60;-fx-text-overrun: center-ellipsis;");
                        if (!empty) {
                            int rowIndex = ((pageDto.getPageIndex() - 1) * pageDto.getPageSize()) + this.getIndex() + 1;
                            this.setText(String.valueOf(rowIndex));
                        }
                    }
                };
                return cell;
            });
            createNameTableColumn.setCellFactory((col) -> new DataTableCell("createName"));
            ;//操作人员
            typeTableColumn.setCellFactory((col) -> new DataTableCell("type"));
            ;//操作类型
            memoTableColumn.setCellFactory((col) -> new DataTableCell("memo"));
            ;//操作描述
            //创建时间
            dateCreatedTableColumn.setCellFactory((col) -> new DataTableCell("dateCreated"));
            //操作
            operTableColumn.setCellFactory((col) -> new ButtonCell(dataTableView));

            pageBar.getChildren().setAll(new PageBar(this, pageDto, "loadPage").getBar());
        } catch (Exception e) {
            ialert.error(e.getMessage());
        }
    }

    //    @RequiresPermissions("alog:info")
    public String info(long id) {
        log.info("info.........");
        Response result = new Response();
        try {
            LogOperationDto dto = new LogOperationDto();
            dto.setId(id);
            result.data = logOperationService.findDataById(dto);
        } catch (Exception e) {
            result = Response.error(e.getMessage());
        }
        return toJson(result);
    }

    public void showInfo(LogOperationDto dto) {
        log.info("showInfo.........");
        restFrom();
        if (dto != null) {
            createNameL.setText(dto.getCreateName());
            dateCreatedL.setText(DateUtil.getDateTimeStr(dto.getDateCreated()));
            typeL.setText(dto.getType());
            memoL.setText(dto.getMemo());
            detailInfoL.setText(dto.getDetailInfo());
        }
    }

    private void restFrom() {
        createNameL.setText(null);
        dateCreatedL.setText(null);
        typeL.setText(null);
        memoL.setText(null);
        detailInfoL.setText(null);
    }

    @FXML
    public void resetSearch() {
        log.info("resetSearch.........");
        dateBeginT.setValue(null);
        dateEndT.setValue(null);
        keywordT.setText(null);
    }

    /**
     * <li>逻辑删除。
     */
//    @RequiresPermissions("alog:del")
    public void del(long id) {
        log.info("del.........");
        try {
            LogOperationDto dto = new LogOperationDto();
            dto.setId(id);
            logOperationService.deleteData(dto);
            loadPage();
        } catch (Exception e) {
            ialert.error(e.getMessage());
            return;
        }
        ialert.success(I18nUtil.get("ialert.success"));
    }

    @Override
    public void dispose() {

    }


    class ButtonCell<T> extends DataButtonCell {

        Label delBtn = new Label("删除");

        {
            paddedButton.getStylesheets().add("/com/redphase/css/user.css");
        }

        public ButtonCell(final TableView table) {
            paddedButton.setPadding(new Insets(0));

            if (jwtUtil.isPermitted("alog:info")) {
                Label infoBtn = new Label("详情");
                infoBtn.getStyleClass().addAll("ob");
                ImageView editImageView = new ImageView(new Image("/com/redphase/images/info.png"));
                editImageView.setFitWidth(20);
                editImageView.setFitHeight(20);
                infoBtn.setGraphic(editImageView);

                infoBtn.setOnMouseClicked((m) -> {
                    log.debug("OperButtonCell...info");
                    LogOperationDto dto = (LogOperationDto) table.getSelectionModel().getSelectedItem();
                    idialog.openDialog("日志详情", operactionInfoView, 800.0, 600.0,false);
                    showInfo(dto);
                });
                paddedButton.getChildren().add(infoBtn);
            }
            if (jwtUtil.isPermitted("alog:del")) {
                delBtn.getStyleClass().addAll("ob");
                ImageView delImageView = new ImageView(new Image("/com/redphase/images/del.png"));
                delImageView.setFitWidth(20);
                delImageView.setFitHeight(20);
                delBtn.setGraphic(delImageView);
                delBtn.setOnMouseClicked((m) -> {
                    log.debug("OperButtonCell...delBtn");
                    LogOperationDto dto = (LogOperationDto) table.getSelectionModel().getSelectedItem();
                    del(dto.getId());
                });
                paddedButton.getChildren().add(delBtn);
            }
        }
    }
}
