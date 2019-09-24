package com.redphase.framework.util.word;


import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.redphase.dto.world.ae.AeDataDto;
import com.redphase.dto.world.ae.AeListDto;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * 超声波(AE)局部放电检测报告.doc
 */
@Slf4j
public class AeWordUtil extends WordUtil {

    public AeWordUtil() {
    }

    public static void main(String[] args) {
        AeWordUtil wordUtil = new AeWordUtil();
        AeDataDto dataDto = new AeDataDto() {{
            //一、基本信息
            setBdz("A-变电站");//变电站
            setWtdw("A-委托单位");//委托单位
            setSydw("A-试验单位");//试验单位
            setYxbh("A-运行编号");//运行编号
            setSyxz("A-试验性质");//试验性质
            setSyrq("A-试验日期");//试验日期
            setSyry("A-试验人员");//试验人员
            setSydd("A-试验地点");//试验地点
            setBgrq("A-报告日期");//报告日期
            setBzr("A-编制人");//编制人
            setShr("A-审核人");//审核人
            setPzr("A-批准人");//批准人
            setTq("A-试验天气");//试验天气
            setWd("A-环境温度（℃）");//环境温度（℃）
            setSd("A-环境相对湿度（%）");//环境相对湿度（%）
            //二、设备铭牌
            setSccj("A-生产厂家");//生产厂家
            setCcrq("A-出厂日期");//出厂日期
            setCcbh("A-出厂编号");//出厂编号
            setSbxh("A-设备型号");//设备型号
            setEddy("A-额定电压(kV)");//额定电压(kV)
            //三、检测数据
            setBjzs("A-背景噪声");//背景噪声
            setTzfx("A-特征分析");//特征分析
            setBjz("A-背景值");//背景值
            setYqcj("A-仪器厂家");//仪器厂家
            setYqxh("A-仪器型号");//仪器型号
            setYqbh("A-仪器编号");//仪器编号
            setBz("A-备注");//备注
            setDtoList(new ArrayList<AeListDto>() {{
                add(new AeListDto() {{
                    setXh("1");//序号
                    setJcwz("1-检测位置");//检测位置
                    setJcsz("1-检测数值");//检测数值
                    setTpwj("F:/PDT840/报告生成测试/图谱.png");// 图谱文件
                    setFh("A-负荷电流(A)");//负荷电流(A)
                    setJl("A-结论");//结论
                    setKjg("F:/PDT840/报告生成测试/可见光.jpg");//备注（可见光照片）
                }});
                add(new AeListDto() {{
                    setXh("2");//序号
                    setJcwz("2-检测位置");//检测位置
                    setJcsz("2-检测数值");//检测数值
                    setTpwj("F:/PDT840/报告生成测试/图谱.png");// 图谱文件
                    setFh("2-负荷电流(A)");//负荷电流(A)
                    setJl("2-结论");//结论
                    setKjg("F:/PDT840/报告生成测试/可见光.jpg");//备注（可见光照片）
                }});
            }});

        }};
        wordUtil.report("F:/PDT840/报告生成测试/超声波（AE）局部放电检测报告-" + (System.currentTimeMillis()) + ".doc", dataDto);
    }

    public void report(String path, AeDataDto dataDto) {
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
            Paragraph p = new Paragraph("超声波（AE）局部放电检测报告", new Font(Font.NORMAL, 10.5f, Font.BOLDITALIC, new Color(0, 0, 0)));
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
            table1.addCell(newCell(dataDto.getSyrq()));
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
            table1.addCell(newCell(dataDto.getTq()));
            table1.addCell(newCell("环境温度（℃）"));
            table1.addCell(newCell(dataDto.getWd()));
            table1.addCell(newCell("环境相对湿度（%）"));
            table1.addCell(newCell(dataDto.getSd()));

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

            table2.addCell(newCell("生产厂家"));
            table2.addCell(newCell(dataDto.getSccj()));
            table2.addCell(newCell("出厂日期"));
            table2.addCell(newCell(dataDto.getCcrq()));
            table2.addCell(newCell("出厂编号"));
            table2.addCell(newCell(dataDto.getCcbh()));
            table2.addCell(newCell("设备型号"));
            table2.addCell(newCell(dataDto.getSbxh()));
            table2.addCell(newCell("额定电压(kV)"));
            table2.addCell(newCell(dataDto.getEddy()));

            document.add(table2);
            //三、检测数据
            Table table3 = new Table(7);
            table3.setBorderWidth(1);
            table3.setBorderColor(Color.BLACK);
            table3.setPadding(0);
            table3.setSpacing(0);
            Cell titleCell3 = newCell("三、检测数据", new Font(Font.NORMAL, 9, Font.BOLDITALIC, new Color(0, 0, 0)));
            titleCell3.setHeader(true);
            titleCell3.setColspan(7);
            table3.addCell(titleCell3);

            Cell bjzsCellT = newCell("背景噪声");
            bjzsCellT.setColspan(1);
            table3.addCell(bjzsCellT);
            Cell bjzsCellV = newCell(dataDto.getBjzs());
            bjzsCellV.setColspan(6);
            table3.addCell(bjzsCellV);

            for (String title : new String[]{"序号", "检测位置", "检测数值", "图谱文件", "负荷电流(A)", "结论", "备注（可见光照片）"}) {
                Cell tCall = newCell(title);
                table3.addCell(tCall);
            }
            table3.endHeaders();// 表头结束
            if (dataDto != null && dataDto.getDtoList() != null) {
                for (int i = 0; i < dataDto.getDtoList().size(); i++) {
                    addListDto(table3, dataDto.getDtoList().get(i));
                }
            }
            for (int i = 0; i < 5; i++) {
                addListDto(table3, new AeListDto());
            }
            
            Cell tzfxCellT = newCell("特征分析");
            tzfxCellT.setColspan(1);
            table3.addCell(tzfxCellT);
            Cell tzfxCellV = newCell(dataDto.getTzfx());
            tzfxCellV.setColspan(6);
            table3.addCell(tzfxCellV);
            
            Cell bjzCellT = newCell("背景值");
            bjzCellT.setColspan(1);
            table3.addCell(bjzCellT);
            Cell bjzCellV = newCell(dataDto.getBjz());
            bjzCellV.setColspan(6);
            table3.addCell(bjzCellV);
            
            Cell yqcjCellT = newCell("仪器厂家");
            yqcjCellT.setColspan(1);
            table3.addCell(yqcjCellT);
            Cell yqcjCellV = newCell(dataDto.getYqcj());
            yqcjCellV.setColspan(6);
            table3.addCell(yqcjCellV);
            
            Cell yqxhCellT = newCell("仪器型号");
            yqxhCellT.setColspan(1);
            table3.addCell(yqxhCellT);
            Cell yqxhCellV = newCell(dataDto.getYqxh());
            yqxhCellV.setColspan(6);
            table3.addCell(yqxhCellV);
            
            Cell yqbhCellT = newCell("仪器编号");
            yqbhCellT.setColspan(1);
            table3.addCell(yqbhCellT);
            Cell yqbhCellV = newCell(dataDto.getYqbh());
            yqbhCellV.setColspan(6);
            table3.addCell(yqbhCellV);
            
            Cell bzCellT = newCell("备注");
            bzCellT.setColspan(1);
            table3.addCell(bzCellT);
            Cell bzCellV = newCell(dataDto.getBz());
            bzCellV.setColspan(6);
            table3.addCell(bzCellV);
            
            document.add(table3);
            document.add(new Paragraph("\t   注：异常时记录负荷和图谱，正常时记录数值。", new Font(Font.NORMAL, 9, Font.NORMAL, new Color(0, 0, 0))));
            //新页
            document.newPage();
            document.add(new Paragraph("附录："));

            if (dataDto != null && dataDto.getDtoList() != null) {
                for (int i = 0; i < dataDto.getDtoList().size(); i++) {
                    AeListDto listDto = dataDto.getDtoList().get(i);
                    addImgDto(document, listDto.getTpwj(), listDto.getJcwz() + "--图谱文件");
                }
            }
            if (dataDto != null && dataDto.getDtoList() != null) {
                for (int i = 0; i < dataDto.getDtoList().size(); i++) {
                    AeListDto listDto = dataDto.getDtoList().get(i);
                    addImgDto(document, listDto.getKjg(), listDto.getJcwz() + "--可见光照片");
                    System.err.println(listDto.getKjg());
                    System.err.println(listDto.getJcwz());
                }
            }
//            //————————页脚设置————————
//            //页脚段落(这里为空，可以自己添加文字)
//            Paragraph paraFooter = new Paragraph();
//            Font font = new Font();
//            //页脚的字体大小
//            font.setSize(12f);
//            font.setColor(new Color(0,0,0));
//            paraFooter.setFont(font);
//            paraFooter.setAlignment("center");
//            //（参数一）页脚的段落   和  （参数二）是否有页码
//            HeaderFooter footer = new HeaderFooter(paraFooter, false);
//            //页脚的对齐方式（应该在footer设置而不是段落中设置）
//            footer.setAlignment(1);
//            document.setFooter(footer);

            document.close();
        } catch (Exception e) {
            log.error("报表异常!", e);
        }
    }

    private void addListDto(Table table3, AeListDto listDto) {
        try {
            table3.addCell(newCell(listDto.getXh()));//序号
            table3.addCell(newCell(listDto.getJcwz()));// 检测位置
            table3.addCell(newCell(listDto.getJcsz()));// 检测数值
            table3.addCell("");// 图谱文件
            table3.addCell(newCell(listDto.getFh()));// 负荷电流(a)
            table3.addCell(newCell(listDto.getJl()));// 结论
            table3.addCell("");// 备注（可见光照片）
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }
}
