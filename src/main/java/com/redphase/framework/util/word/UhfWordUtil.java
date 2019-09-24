package com.redphase.framework.util.word;


import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.redphase.dto.world.uhf.UhfDataDto;
import com.redphase.dto.world.uhf.UhfListDto;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * 特高频局部放电检测报告.doc
 */
@Slf4j
public class UhfWordUtil extends WordUtil {

    public UhfWordUtil() {
    }

    public static void main(String[] args) {
        UhfWordUtil wordUtil = new UhfWordUtil();
        UhfDataDto dataDto = new UhfDataDto() {{

            setBdz("A-变电站");// 变电站
            setWtdw("A-委托单位");// 委托单位
            setSydw("A-试验单位");// 试验单位
            setYxbh("A-运行编号");// 运行编号
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
            //二、设备铭牌
            setSbxh("A-设备型号");// 设备型号
            setSccj("A-生产厂家");// 生产厂家
            setEddy("A-额定电压");// 额定电压
            setTyrq("A-投运日期");// 投运日期
            setCcrq("A-出厂日期");// 出厂日期
            setCcbh("A-出厂编号");// 出厂编号
            //三、检测数据
            setDtoList(new ArrayList<UhfListDto>() {{
                add(new UhfListDto() {{
                    setXh("1");// 序号
                    setJcwz("A-检测位置");// 检测位置
                    setFh("A-负荷电流");// 负荷电流
                    setTpwj("D:/Pictures/04191336f17u.jpg");// 图谱文件

                }});
                add(new UhfListDto() {{
                    setXh("2");// 序号
                    setJcwz("A-检测位置");// 检测位置
                    setFh("A-负荷电流");// 负荷电流
                    setTpwj("D:/Pictures/04191336f17u.jpg");// 图谱文件
                }});
                add(new UhfListDto() {{
                    setXh("3");// 序号
                    setJcwz("A-检测位置");// 检测位置
                    setFh("A-负荷电流");// 负荷电流
                    setTpwj("D:/Pictures/04191336f17u.jpg");// 图谱文件
                }});
            }});
            setTzfx("A-特征分析");// 特征分析
            setYqxh("A-仪器型号");// 仪器型号
            setJl("A-结论");// 结论
            setBz("A-备注");// 备注
        }};
        wordUtil.report("d:/特高频局部放电检测报告-" + (System.currentTimeMillis()) + ".doc", dataDto);
    }

    public void report(String path, UhfDataDto dataDto) {
        // 创建word文档,并设置纸张的大小
        Document document = new Document(PageSize.A4);
        try {
            File file = new File(path);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            RtfWriter2.getInstance(document, new FileOutputStream(path));

            //页码
            setHeaderFooter(document);

            document.open();
            //标题头
            Paragraph p = new Paragraph("特高频局部放电检测报告", new Font(Font.NORMAL, 10.5f, Font.BOLDITALIC, new Color(0, 0, 0)));
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
            table1.addCell(newCell("运行编号"));
            table1.addCell(newCell(dataDto.getYxbh()));
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
            table1.addCell(newCell("环境温度（℃）"));
            table1.addCell(newCell(dataDto.getWd()));
            table1.addCell(newCell("环境相对湿度（%）"));
            table1.addCell(newCell(dataDto.getSd()));
            table1.addCell("");
            table1.addCell("");
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
            table2.addCell(newCell("额定电压(kV)"));
            table2.addCell(newCell(dataDto.getEddy()));
            table2.addCell(newCell("投运日期"));
            table2.addCell(newCell(dataDto.getTyrq()));
            table2.addCell(newCell("出厂日期"));
            table2.addCell(newCell(dataDto.getCcrq()));
            table2.addCell(newCell("出厂编号"));
            table2.addCell(newCell(dataDto.getCcbh()));
            document.add(table2);
            //三、检测数据
            Table table3 = new Table(4);
            table3.setBorderWidth(1);
            table3.setBorderColor(Color.BLACK);
            table3.setPadding(0);
            table3.setSpacing(0);
            Cell titleCell3 = newCell("三、检测数据", new Font(Font.NORMAL, 9, Font.BOLDITALIC, new Color(0, 0, 0)));
            titleCell3.setHeader(true);
            titleCell3.setColspan(4);
            table3.addCell(titleCell3);

            for (String title : new String[]{"序号", "检测位置", "负荷电流", "图谱文件"}) {
                Cell tCall = newCell(title);
//                if ("序号".equals(title)) {
//                    tCall.setColspan(2);
//                }
                table3.addCell(tCall);
            }
            table3.endHeaders();// 表头结束
            if (dataDto != null && dataDto.getDtoList() != null) {
                for (int i = 0; i < dataDto.getDtoList().size(); i++) {
                    addListDto(table3, dataDto.getDtoList().get(i));
                }
            }
            for (int i = 0; i < 5; i++) {
                addListDto(table3, new UhfListDto());
            }
            table3.addCell(newCell("特征分析"));
            table3.addCell(newCell(dataDto.getTzfx(), 3));
            table3.addCell(newCell("仪器型号"));
            table3.addCell(newCell(dataDto.getYqxh(), 3));
            table3.addCell(newCell("结论"));
            table3.addCell(newCell(dataDto.getJl(), 3));
            table3.addCell(newCell("备注"));
            table3.addCell(newCell(dataDto.getBz(), 3));
            document.add(table3);

            //新页
            document.newPage();
            document.add(new Paragraph("附录:"));
            if (dataDto != null && dataDto.getDtoList() != null) {
                for (int i = 0; i < dataDto.getDtoList().size(); i++) {
                    UhfListDto listDto = dataDto.getDtoList().get(i);
                    addImgDto(document, listDto.getTpwj(), listDto.getJcwz() + "--图谱文件");
                }
            }
            document.close();
        } catch (Exception e) {
            log.error("报表异常!", e);
        }
    }

    private void addListDto(Table table3, UhfListDto listDto) {
        try {
            table3.addCell(newCell(listDto.getXh()));//序号
            table3.addCell(newCell(listDto.getJcwz()));//检测位置
            table3.addCell(newCell(listDto.getFh()));//负荷电流（A）
            table3.addCell(newCell(""));//图谱文件
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }
}
