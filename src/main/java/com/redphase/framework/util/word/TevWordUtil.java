package com.redphase.framework.util.word;


import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.redphase.dto.world.tev.TevDataDto;
import com.redphase.dto.world.tev.TevListDto;
import com.redphase.framework.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * 暂态地电压局部放电检测报告.doc
 */
@Slf4j
public class TevWordUtil extends WordUtil {

    public TevWordUtil() {
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        TevWordUtil wordUtil = new TevWordUtil();
        TevDataDto dataDto = new TevDataDto() {{
            setBdz("A-变电站");// 变电站
            setWtdw("A-委托单位");// 委托单位
            setSydw("A-试验单位");// 试验单位
            setSyxz("A-试验性质");// 试验性质
            setSyrq("A-试验日期");// 试验日期
            setSyry("A-试验人员");// 试验人员
            setSydd("A-试验地点");// 试验地点
            setBgrq("A-报告日期");// 报告日期
            setBzr("A-编制人");// 编制人
            setShr("A-审核人");// 审核人
            setPzr("A-批准人");// 批准人
            setSytq("A-试验天气");// 试验天气
            setWd("A-温度");// 温度
            setSd("A-湿度");// 湿度
            setBjzs("A-背景噪声");// 背景噪声
            //二、设备铭牌
            setSbxh("A-设备型号");// 设备型号
            setSccj("A-生产厂家");// 生产厂家
            setEddy("A-额定电压");// 额定电压
            setTyrq("A-投运日期");// 投运日期
            setCcrq("A-出厂日期");// 出厂日期
            //三、检测数据
            //三、前次-检测数据
            setOldDtoList(new ArrayList<TevListDto>() {{
                add(new TevListDto() {{
                    setIsNew(0);//0:旧,1:新
                    setXh("1");// 序号
                    setBh("A-开关柜编号");// 开关柜编号
                    setQz("A-前中");// 前中
                    setQx("A-前下");// 前下
                    setHs("A-后上");// 后上
                    setHz("A-后中");// 后中
                    setHx("A-后下");// 后下
                    setCs("A-侧上");// 侧上
                    setCz("A-侧中");// 侧中
                    setCx("A-侧下");// 侧下
                    setFh("A-负荷");// 负荷
                    setKjg("D:/Pictures/04191336f17u.jpg");// 备注（可见光照片）
                    setJl("A-结论");// 结论

                }});
                add(new TevListDto() {{
                    setIsNew(0);//0:旧,1:新
                    setXh("2");// 序号
                    setBh("A-开关柜编号");// 开关柜编号
                    setQz("A-前中");// 前中
                    setQx("A-前下");// 前下
                    setHs("A-后上");// 后上
                    setHz("A-后中");// 后中
                    setHx("A-后下");// 后下
                    setCs("A-侧上");// 侧上
                    setCz("A-侧中");// 侧中
                    setCx("A-侧下");// 侧下
                    setFh("A-负荷");// 负荷
                    setKjg("D:/Pictures/04191336f17u.jpg");// 备注（可见光照片）
                    setJl("A-结论");// 结论

                }});
            }});
            //三、本次-检测数据
            setNewDtoList(new ArrayList<TevListDto>() {{
                add(new TevListDto() {{
                    setIsNew(1);//0:旧,1:新
                    setXh("1");// 序号
                    setBh("A-开关柜编号");// 开关柜编号
                    setQz("A-前中");// 前中
                    setQx("A-前下");// 前下
                    setHs("A-后上");// 后上
                    setHz("A-后中");// 后中
                    setHx("A-后下");// 后下
                    setCs("A-侧上");// 侧上
                    setCz("A-侧中");// 侧中
                    setCx("A-侧下");// 侧下
                    setFh("A-负荷");// 负荷
                    setKjg("D:/Pictures/04191336f17u.jpg");// 备注（可见光照片）
                    setJl("A-结论");// 结论

                }});
                add(new TevListDto() {{
                    setIsNew(1);//0:旧,1:新
                    setXh("2");// 序号
                    setBh("A-开关柜编号");// 开关柜编号
                    setQz("A-前中");// 前中
                    setQx("A-前下");// 前下
                    setHs("A-后上");// 后上
                    setHz("A-后中");// 后中
                    setHx("A-后下");// 后下
                    setCs("A-侧上");// 侧上
                    setCz("A-侧中");// 侧中
                    setCx("A-侧下");// 侧下
                    setFh("A-负荷");// 负荷
                    setKjg("D:/Pictures/04191336f17u.jpg");// 备注（可见光照片）
                    setJl("A-结论");// 结论

                }});
            }});
            setTzfx("A-特征分析");// 特征分析
            setBjz("A-背景值");// 背景值
            setYqcj("A-仪器厂家");// 仪器厂家
            setYqxh("A-仪器型号");// 仪器型号
            setYqbh("A-仪器编号");// 仪器编号
            setBz("A-备注");// 备注
        }};

        wordUtil.report("d:/暂态地电压局部放电检测报告-" + (System.currentTimeMillis()) + ".doc", dataDto);
    }

    public void report(String path, TevDataDto dataDto) {
        // 创建word文档,并设置纸张的大小
        Document document = new Document(PageSize.A4);
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            RtfWriter2.getInstance(document, new FileOutputStream(path));
            document.open();

            //页码
            setHeaderFooter(document);

            //标题头
            Paragraph p = new Paragraph("暂态地电压局部放电检测报告", new Font(Font.NORMAL, 10.5f, Font.BOLDITALIC, new Color(0, 0, 0)));
            p.setAlignment(1);
            document.add(p);

            //一、基本信息
            Table table1 = new Table(8);
            table1.setBorderWidth(1);
            table1.setBorderColor(Color.BLACK);
            table1.setPadding(0);
            table1.setSpacing(0);
            Cell titleCell1 = newCell("一、基本信息", new Font(Font.NORMAL, 9, Font.BOLDITALIC, new Color(0, 0, 0)));
            titleCell1.setHeader(true);
            titleCell1.setColspan(8);
            table1.addCell(titleCell1);
            table1.endHeaders();// 表头结束

            table1.addCell(newCell("变电站"));
            table1.addCell(newCell(dataDto.getBdz()));
            table1.addCell(newCell("委托单位"));
            table1.addCell(newCell(dataDto.getWtdw()));
            table1.addCell(newCell("试验单位"));
            table1.addCell(newCell(dataDto.getSydw()));
            table1.addCell("");
            table1.addCell("");
            table1.addCell(newCell("试验性质"));
            table1.addCell(newCell(dataDto.getSyxz()));
            table1.addCell(newCell("试验日期"));
            table1.addCell(newCell(dataDto.getSyxz()));
            table1.addCell(newCell("试验人员"));
            table1.addCell(newCell(dataDto.getSyry()));
            table1.addCell(newCell("试验地点"));
            table1.addCell(newCell(dataDto.getSydd()));
            table1.addCell(newCell("报告日期"));
            table1.addCell(newCell(dataDto.getBgrq()));
            table1.addCell(newCell("编制人"));
            table1.addCell(newCell(dataDto.getBzr()));
            table1.addCell(newCell("审核人"));
            table1.addCell(newCell(dataDto.getShr()));
            table1.addCell(newCell("批准人"));
            table1.addCell(newCell(dataDto.getPzr()));
            table1.addCell(newCell("试验天气"));
            table1.addCell(newCell(dataDto.getSytq()));
            table1.addCell(newCell("温度"));
            table1.addCell(newCell(dataDto.getWd()));
            table1.addCell(newCell("湿度"));
            table1.addCell(newCell(dataDto.getSd()));
            table1.addCell(newCell("背景噪声"));
            table1.addCell(newCell(dataDto.getBjzs()));
            document.add(table1);
            //二、设备铭牌
            Table table2 = new Table(6);
            table2.setBorderWidth(1);
            table2.setBorderColor(Color.BLACK);
            table2.setPadding(0);
            table2.setSpacing(0);
            Cell titleCell2 = newCell("二、设备铭牌", new Font(Font.NORMAL, 9, Font.BOLDITALIC, new Color(0, 0, 0)));
            titleCell2.setHeader(true);
            titleCell2.setColspan(6);
            table2.addCell(titleCell2);
            table2.endHeaders();// 表头结束

            table2.addCell(newCell("设备型号"));
            table2.addCell(newCell(dataDto.getSbxh()));
            table2.addCell(newCell("生产厂家"));
            table2.addCell(newCell(dataDto.getSccj()));
            table2.addCell(newCell("额定电压"));
            table2.addCell(newCell(dataDto.getEddy()));
            table2.addCell(newCell("投运日期"));
            table2.addCell(newCell(dataDto.getTyrq()));
            table2.addCell(newCell("出厂日期"));
            table2.addCell(newCell(dataDto.getCcrq()));
            document.add(table2);
            //三、检测数据
            Table table3 = new Table(14);
            table3.setBorderWidth(1);
            table3.setBorderColor(Color.BLACK);
            table3.setPadding(0);
            table3.setSpacing(0);
            Cell titleCell3 = newCell("三、检测数据", new Font(Font.NORMAL, 9, Font.BOLDITALIC, new Color(0, 0, 0)));
            titleCell3.setHeader(true);
            titleCell3.setColspan(14);
            table3.addCell(titleCell3);

            for (String title : new String[]{"序号", "开关柜编号", "前中", "前下", "后上", "后中", "后下", "侧上", "侧中", "侧下", "负荷", "备注", "结论"}) {
                Cell tCall = newCell(title);
                if ("开关柜编号".equals(title)) {
                    tCall.setColspan(2);
                }
                table3.addCell(tCall);
            }
            table3.endHeaders();// 表头结束
            if (dataDto != null && dataDto.getNewDtoList() != null) {
                for (int i = 0; i < dataDto.getNewDtoList().size(); i++) {
                    addListDto(table3, dataDto.getOldDtoList().get(i));
                    addListDto(table3, dataDto.getNewDtoList().get(i));
                }
            }
            //添加5个空行
            for (int i = 0; i < 5; i++) {
                int xh = i + 1;
                if (dataDto != null && dataDto.getNewDtoList() != null) {
                    xh += dataDto.getNewDtoList().size();
                }
                TevListDto tevListDto = new TevListDto() {{
                    setIsNew(0);
                }};
                tevListDto.setXh(StrUtil.asString(xh));
                addListDto(table3, tevListDto);
                tevListDto.setIsNew(1);
                addListDto(table3, tevListDto);
            }
            table3.addCell(newCell("特征分析", 2));
            table3.addCell(newCell(dataDto.getTzfx(), 12));
            table3.addCell(newCell("背景值", 2));
            table3.addCell(newCell(dataDto.getBjz(), 12));
            table3.addCell(newCell("仪器厂家", 2));
            table3.addCell(newCell(dataDto.getYqcj(), 12));
            table3.addCell(newCell("仪器型号", 2));
            table3.addCell(newCell(dataDto.getYqxh(), 12));
            table3.addCell(newCell("仪器编号", 2));
            table3.addCell(newCell(dataDto.getYqbh(), 12));
            table3.addCell(newCell("备注", 2));
            table3.addCell(newCell(dataDto.getBz(), 12));
            document.add(table3);
            document.add(new Paragraph("\t   注：在备注中对停运开关柜进行记录。", new Font(Font.NORMAL, 9, Font.NORMAL, new Color(0, 0, 0))));

            //新页
            document.newPage();
            document.add(new Paragraph("附录:"));
            if (dataDto != null && dataDto.getNewDtoList() != null) {
                for (int i = 0; i < dataDto.getNewDtoList().size(); i++) {
                    addImgDto(document, dataDto.getOldDtoList().get(i));
                    addImgDto(document, dataDto.getNewDtoList().get(i));
                }
            }
            document.close();
        } catch (Exception e) {
            log.error("报表异常!", e);
        }
    }

    private void addImgDto(Document document, TevListDto listDto) {
        try {
            if (StringUtils.isNotEmpty(listDto.getKjg())) {
//                document.add(Image.getInstance(listDto.getKjg()));
//                Paragraph pg = new Paragraph(listDto.getBh() + "--" + (listDto.getIsNew() == 0 ? "前次" : "本次"));
//                pg.setAlignment(Element.ALIGN_CENTER);
//                document.add(pg);
                addImgDto(document,listDto.getKjg(),listDto.getBh() + "--" + (listDto.getIsNew() == 0 ? "前次" : "本次"));
            }
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }

    private void addListDto(Table table3, TevListDto listDto) {
        try {
            if (listDto.getIsNew() == 0) {
                Cell xhCall = newCell(listDto.getXh());
                xhCall.setRowspan(2);
                table3.addCell(xhCall);//序号
                Cell bhCall = newCell(listDto.getBh());
                bhCall.setRowspan(2);
                table3.addCell(bhCall);//开关柜编号
                table3.addCell(newCell("前次"));//前次
            } else {
                table3.addCell(newCell("本次"));//本次
            }
            table3.addCell(newCell(listDto.getQz()));//前中
            table3.addCell(newCell(listDto.getQx()));//前下
            table3.addCell(newCell(listDto.getHs()));//后上
            table3.addCell(newCell(listDto.getHz()));//后中
            table3.addCell(newCell(listDto.getHx()));//后下
            table3.addCell(newCell(listDto.getCs()));//侧上
            table3.addCell(newCell(listDto.getCz()));//侧中
            table3.addCell(newCell(listDto.getCx()));//侧下
            table3.addCell(newCell(listDto.getFh()));//负荷
            table3.addCell("");//备注（可见光照片）
            table3.addCell(newCell(listDto.getJl()));//结论
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }
}
