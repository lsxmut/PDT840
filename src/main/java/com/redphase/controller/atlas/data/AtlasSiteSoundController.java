package com.redphase.controller.atlas.data;

import com.redphase.controller.atlas.AtlasBaseController;
import com.redphase.dto.atlas.DataDeviceDto;
import com.redphase.dto.atlas.DataDeviceSiteDto;
import com.redphase.dto.table.DataTableDto;
import com.redphase.framework.util.ConvertAudio2WavUtil;
import com.redphase.view.SeqlTableCell;
import de.felixroske.jfxsupport.FXMLController;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

@FXMLController
@Slf4j
public class AtlasSiteSoundController extends AtlasBaseController implements Initializable {
    @FXML
    Label bdzL;//变电站名称
    @FXML
    Label dydjL;//电压等级
    @FXML
    Label sbL;//设备名称
    @FXML
    Label wzL;//被测部位和测试点
    @FXML
    TableView dataTableView;//录音列表
    @FXML
    TableColumn codeTableColumn;//序号
    @FXML
    TableColumn nameTableColumn;//录音文件名称
    @FXML
    TableColumn operateTableColumn;//操作
    @FXML
    TableColumn progressTableColumn;//进度条

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        log.info("initialize...");
    }

    List<String> soundList;
    Integer beginIndex = 0;
    Integer endIndex = 0;

    public void init(List<String> soundList, Map map) {
        this.soundList = soundList;
        this.beginIndex = 0;
        this.endIndex = 0;
        clearL(bdzL, dydjL, sbL, wzL);
        if (map == null) {
            map = new HashMap();
        }
        DataTableDto dataTableDto = (DataTableDto) map.get("dataTableDto");
        if (dataTableDto != null) {
            DataDeviceDto dataDeviceDto = dataTableDto.getDataDeviceDto();
            bdzL.setText(dataDeviceDto.getSubstation());//变电站名称
            dydjL.setText(dataDeviceDto.getVoltageLevel());//电压等级
            sbL.setText(dataDeviceDto.getDeviceName());//设备名称
            if (StringUtils.isNotEmpty(dataTableDto.getSite())) {
                wzL.setText(dataTableDto.getSite());
            } else {
                DataDeviceSiteDto dataDeviceSiteDto = dataTableDto.getDataDeviceSiteDto();
                if (dataDeviceSiteDto != null) {
                    wzL.setText(dataDeviceSiteDto.getSiteName());//被测部位和测试点
                }
            }
        }
        ObservableList<String> dataList = FXCollections.observableArrayList(soundList);
        dataTableView.setItems(dataList);

        codeTableColumn.setCellFactory((col) -> new SeqlTableCell());
        nameTableColumn.setCellFactory((col) -> {
            TableCell<String, String> cell = new TableCell<String, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setGraphic(null);
                    this.setStyle("-fx-pref-width: 250");
                    if (!empty) {
                        String fileUrl = this.getTableView().getItems().get(this.getIndex());
                        this.setText(new File(fileUrl).getName());
                    }
                }
            };
            return cell;
        });
        operateTableColumn.setCellFactory((col) -> {
            TableCell<String, String> cell = new TableCell<String, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    this.setStyle("-fx-pref-width: 150");
                    if (!empty) {
                        HBox paddedButton = new HBox();
                        paddedButton.setAlignment(Pos.CENTER_LEFT);
                        paddedButton.setPadding(new Insets(0));
                        int id = this.getIndex();
                        ImageView audioImg = new ImageView(new Image("/com/redphase/images/audio.png"));
                        ImageView operatePay = new ImageView(new Image("/com/redphase/images/play.png"));
                        Pane operatePane = new Pane();
                        paddedButton.getStylesheets().add("/com/redphase/css/atlas.css");
                        audioImg.setId("audio_" + id);
                        audioImg.setFitWidth(25);
                        audioImg.setFitHeight(20);
                        audioImg.setPickOnBounds(true);
                        audioImg.setSmooth(false);
                        audioImg.setVisible(false);
                        operatePane.setId("operatePane_" + id);
                        operatePane.setMinHeight(20);
                        operatePane.setPrefWidth(55);
                        HBox operateHBox = new HBox();
                        operateHBox.setId("operateHBox_" + id);
                        operateHBox.setMinHeight(20);
                        operateHBox.setPrefWidth(55);
                        operateHBox.setAlignment(Pos.CENTER_RIGHT);
                        operatePay.setId("operatePay_" + id);
                        operatePay.setFitHeight(20);
                        operatePay.setFitWidth(30);
                        operatePay.setPickOnBounds(true);
                        operatePay.setSmooth(false);
                        operatePay.setStyle("-fx-cursor: hand;");
                        operateHBox.getChildren().add(operatePay);
                        operatePane.getChildren().add(operateHBox);
                        paddedButton.getChildren().addAll(audioImg, operatePane);

                        operatePay.setOnMouseClicked((me) -> {
                            if (beginIndex != this.getIndex()) {
                                endIndex = this.getIndex();
                            }
                            beginIndex = this.getIndex();
                            playSound(beginIndex);
                        });

                        this.setGraphic(paddedButton);
                    }
                }
            };
            return cell;
        });
        progressTableColumn.setCellFactory((col) -> {
            TableCell<String, String> cell = new TableCell<String, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);
                    this.setText(null);
                    if (!empty) {
                        HBox paddedButton = new HBox();
                        paddedButton.setPadding(new Insets(0));
                        int id = this.getIndex();
                        HBox progressHBox = new HBox();
                        progressHBox.setId("progressHBox_" + id);
                        progressHBox.setMinHeight(25);
                        progressHBox.setPrefWidth(400);
                        progressHBox.setAlignment(Pos.CENTER_LEFT);
                        progressHBox.setVisible(false);
                        Label progressLa = new Label("0:00");
                        progressLa.setId("progressLa_" + id);
                        progressLa.setPrefWidth(50);
                        progressLa.prefHeight(15);
                        progressLa.setAlignment(Pos.CENTER);
                        progressLa.setContentDisplay(ContentDisplay.CENTER);
                        progressHBox.getChildren().add(progressLa);
                        ProgressBar progress = new ProgressBar(0);
                        progress.setId("progress_" + id);
                        progress.setPrefHeight(15);
                        progress.setPrefWidth(260);
                        progress.setProgress(0);
                        StackPane.setMargin(progress, new Insets(0, 8, 0, 8));
                        progress.setVisible(true);
                        progressHBox.getChildren().add(progress);
                        Label progressLb = new Label("0:00");
                        progressLb.setId("progressLb_" + id);
                        progressLb.setPrefWidth(50);
                        progressLb.prefHeight(15);
                        progressLb.setAlignment(Pos.CENTER);
                        progressLb.setContentDisplay(ContentDisplay.CENTER);
                        progressHBox.getChildren().add(progressLb);
                        progressLb.setVisible(true);
                        paddedButton.getChildren().addAll(progressHBox);

                        this.setGraphic(paddedButton);
                    }
                }
            };
            return cell;
        });
    }

    @FXML
    public void payAll() {
        beginIndex = 0;
        endIndex = soundList.size() - 1;
        playSound(beginIndex);
    }

    @FXML
    public void cwAll() {
        try {
            if (soundList != null) {
                for (String soundFile : soundList) {
                    File fileUrl = new File(soundFile);
                    File wavFile = new File(fileUrl.getParentFile().getPath() + File.separator + fileUrl.getName().substring(0, fileUrl.getName().length() - 3) + "wav");
                    if (!wavFile.exists()) {
                        ConvertAudio2WavUtil.convertAudio(fileUrl.getPath(), wavFile.getPath());
                    }
                }
                ialert.success("转换完成!");
            } else {
                ialert.warning("无可转换音频文件!");
            }
        } catch (Exception e) {
            log.error("音频转换异常!", e);
            ialert.error("音频转换异常,原因:" + e.getMessage());
        }
    }

    private void playSound(int rowIndex) {
        try {
            File fileUrl = new File(soundList.get(rowIndex));
            hideNode("" + rowIndex, false);
            //log.debug(fileUrl);
            MediaPlayer player = mediaPlayerMap.get("operatePay_" + rowIndex);
            if (player == null) {
                File wavFile = new File(fileUrl.getParentFile().getPath() + File.separator + fileUrl.getName().substring(0, fileUrl.getName().length() - 3) + "wav");
//                String wavFile = System.getProperty("java.io.tmpdir") + new File(fileUrl).getName();
                if (!wavFile.exists()) {
                    ConvertAudio2WavUtil.convertAudio(fileUrl.getPath(), wavFile.getPath());
                }
                if (wavFile.exists()) {
                    Media media = new Media(wavFile.toURI().toString());
                    player = new MediaPlayer(media);
                    player.setAutoPlay(false); //设置自动播放
                    mediaPlayerMap.put("operatePay_" + rowIndex, player);
                } else {
                    ialert.error("音频文件不存在!");
                    return;
                }
            }
            ImageView audioImg = (ImageView) dataTableView.lookup("#audio_" + rowIndex);
            ImageView operatePay = (ImageView) dataTableView.lookup("#operatePay_" + rowIndex);
            HBox progressHBox = (HBox) dataTableView.lookup("#progressHBox_" + rowIndex);
            Label progressLa = (Label) progressHBox.lookup("#progressLa_" + rowIndex);
            ProgressBar progress = (ProgressBar) dataTableView.lookup("#progress_" + rowIndex);
            Label progressLb = (Label) progressHBox.lookup("#progressLb_" + rowIndex);

            //音频控制(通常写在控件动作中)
            switch (player.getStatus()) {
                //MediaPlayer.Status.DISPOSED：當執行dispose()方法釋放資源時。
                //MediaPlayer.Status.HALTED：錯誤發生時。
                //MediaPlayer.Status.PAUSED：暫停播放時。
                //MediaPlayer.Status.PLAYING：正在播放時。
                //MediaPlayer.Status.READY：媒體準備播放時。
                //MediaPlayer.Status.STALLED：當無法處理媒體致使播放速度變慢或停止時。
                //MediaPlayer.Status.STOPPED：停止播放時。
                //MediaPlayer.Status.UNKNOWN：MediaPlayer物件剛建立時。
                case PLAYING: {
                    play(false, player, audioImg, operatePay, progressHBox, progressLa, progress, progressLb);
                    break;
                }
                default:
                    play(true, player, audioImg, operatePay, progressHBox, progressLa, progress, progressLb);
            }
        } catch (Exception e) {
            log.error("音频播放异常!", e);
            ialert.error("音频播放异常,原因:" + e.getMessage());
        }
    }

    Map<String, MediaPlayer> mediaPlayerMap = new HashMap<>();

    private void play(boolean isPlay, MediaPlayer player, ImageView audioImg, ImageView operatePay, HBox progressHBox, Label progressLa, ProgressBar progress, Label progressLb) {
        Platform.runLater(() -> {
            //player.setAutoPlay(true);
            audioImg.setVisible(true);
            progressHBox.setVisible(true);
            player.currentTimeProperty().addListener((Observable ov) -> {
                Duration currentTime = player.currentTimeProperty().getValue();
                Duration totalDuration = player.getTotalDuration();
                if (totalDuration.greaterThan(Duration.ZERO)) {
                    progress.setProgress(currentTime.toMillis() / totalDuration.toMillis());
                }
                progressLa.setText(formatTime(currentTime, totalDuration));
            });
            player.setOnReady(() -> {
                progress.setProgress(0);
                int totalTimeSecond = (int) (player.getTotalDuration().toSeconds());
                progressLb.setText(String.format("%02d:%02d", (totalTimeSecond / 60), (totalTimeSecond % 60)));
            });
            player.setOnEndOfMedia(() -> {
                player.stop();
                //            player.dispose();
                operatePay.setImage(new Image("/com/redphase/images/play.png"));
                if (beginIndex < endIndex) {
                    beginIndex++;
                    playSound(beginIndex);
                }
            });

            if (isPlay) {
                operatePay.setImage(new Image("/com/redphase/images/pause.png"));
                player.play();
            } else {
                operatePay.setImage(new Image("/com/redphase/images/play.png"));
                player.pause();
            }

        });
    }

    public void hideNode(String id, boolean isDispose) {
        for (int i = 0; i < soundList.size(); i++) {
            if (id.equalsIgnoreCase("" + i)) {
                continue;
            }
            ImageView operatePay = (ImageView) dataTableView.lookup("#operatePay_" + i);
            MediaPlayer player = mediaPlayerMap.get(operatePay.getId());//mediaPlayerMap.get(soundList.get(i));
            if (player != null) {
                try {
                    player.stop();
                } catch (Exception e) {
                }
                if (isDispose) {
                    try {
                        player.dispose();
                    } catch (Exception e) {
                    }
                }
            }
            ImageView audioImg = (ImageView) dataTableView.lookup("#audio_" + i);
            if (audioImg != null) {
                audioImg.setVisible(false);
            }
            if (operatePay != null)
                operatePay.setImage(new Image("/com/redphase/images/play.png"));
            HBox progressHBox = (HBox) dataTableView.lookup("#progressHBox_" + i);
            if (progressHBox != null)
                progressHBox.setVisible(false);
        }
    }


    private String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60 - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
//            int durationMinutes = intDuration / 60;
//            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;

            if (durationHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours, elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes, elapsedSeconds);
            }
        }
    }

    @Override
    public void dispose() {
        hideNode("-1", true);
        mediaPlayerMap.clear();
    }

    public static void main(String[] args) {
        File fileUrl = new File("D:\\PDT840\\data\\红相股份\\局部放电测试任务2019-02-25\\红相高压实验室\\开关柜\\检测数据\\红相高压实验室_110kV开关柜_TEV_20180820\\#01开关柜\\后上\\#01开关柜_后上_20100101012126.pcm");
        File wavFile = new File(fileUrl.getParentFile().getPath() + File.separator + fileUrl.getName().substring(0, fileUrl.getName().length() - 3) + "wav");
        System.out.println(wavFile.getPath());
    }
}