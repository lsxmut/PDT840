package com.redphase.framework.util.word;


import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.rtf.RtfWriter2;
import com.redphase.dto.world.hfct.HfctDataDto;
import com.redphase.dto.world.hfct.HfctListDto;
import lombok.extern.slf4j.Slf4j;

import java.awt.*;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

/**
 * 高频局部放电检测报告.doc
 */
@Slf4j
public class HfctWordUtil extends WordUtil {

    public HfctWordUtil() {
    }

    public static void main(String[] args) {
        HfctWordUtil wordUtil = new HfctWordUtil();
        HfctDataDto dataDto = new HfctDataDto() {{
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
            setSccj("A-生产厂家");// 生产厂家
            setCcrq("A-出厂日期");// 出厂日期
            setCcbh("A-出厂编号");// 出厂编号
            setSbxh("A-设备型号");// 设备型号
            setEddy("A-额定电压");// 额定电压
            //三、检测数据
            setDtoList(new ArrayList<HfctListDto>() {{
                add(new HfctListDto() {{
                    setXh("1");// 序号
                    setJgmc("A-间隔名称");// 间隔名称
                    setSbmc("A-设备名称");// 设备名称
                    setXw("A-相位");// 相位
                    setTpwj("F:/PDT840/报告生成测试/图谱.png");// 图谱文件
                    setFd("A-是否存在放电信号（打勾）");// 是否存在放电信号（打勾）
                    setFz("A-测试值（峰值）（mV）");// 测试值（峰值）（mV）
                }});
                add(new HfctListDto() {{
                    setXh("2");// 序号
                    setJgmc("A-间隔名称");// 间隔名称
                    setSbmc("A-设备名称");// 设备名称
                    setXw("A-相位");// 相位
                    setTpwj("F:/PDT840/报告生成测试/图谱.png");// 图谱文件
                    setFd("A-是否存在放电信号（打勾）");// 是否存在放电信号（打勾）
                    setFz("A-测试值（峰值）（mV）");// 测试值（峰值）（mV）
                }});
                add(new HfctListDto() {{
                    setXh("3");// 序号
                    setJgmc("A-间隔名称");// 间隔名称
                    setSbmc("A-设备名称");// 设备名称
                    setXw("A-相位");// 相位
                    setTpwj("F:/PDT840/报告生成测试/图谱.png");// 图谱文件
                    setFd("A-是否存在放电信号（打勾）");// 是否存在放电信号（打勾）
                    setFz("A-测试值（峰值）（mV）");// 测试值（峰值）（mV）
                }});
            }});
            setTzfx("A-特征分析");// 特征分析
            setJcyq("A-检测仪器");// 检测仪器
            setJl("A-结论");// 结论
            setBz("A-备注");// 备注
        }};
        wordUtil.report("F:/PDT840/报告生成测试/高频局部放电检测报告-" + (System.currentTimeMillis()) + ".doc", dataDto);
    }

    public void report(String path, HfctDataDto dataDto) {
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
            Paragraph p = new Paragraph("高频局部放电检测报告", new Font(Font.NORMAL, 10.5f, Font.BOLDITALIC, new Color(0, 0, 0)));
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
            for (String title : new String[]{"序号", "间隔名称", "设备名称", "相位", "图谱文件", "是否存在放电信号（打勾）", "测试值（峰值）（mV）"}) {
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
                addListDto(table3, new HfctListDto());
            }
            table3.addCell(newCell("特征分析"));
            table3.addCell(newCell(dataDto.getTzfx(), 6));
            table3.addCell(newCell("检测仪器"));
            table3.addCell(newCell(dataDto.getJcyq(), 6));
            table3.addCell(newCell("结论"));
            table3.addCell(newCell(dataDto.getJl(), 6));
            table3.addCell(newCell("备注"));
            table3.addCell(newCell(dataDto.getBz(), 6));
            
            document.add(table3);
            document.add(new Paragraph("\t   注：如测试时，探头后需增加滤波器进行消噪处理，请在备注处予以注明。", new Font(Font.NORMAL, 9, Font.NORMAL, new Color(0, 0, 0))));
            //新页
            document.newPage();
            document.add(new Paragraph("附录："));
            if (dataDto != null && dataDto.getDtoList() != null) {
                for (int i = 0; i < dataDto.getDtoList().size(); i++) {
                    addImgDto(document, dataDto.getDtoList().get(i));
                }
            }
            document.close();
        } catch (Exception e) {
            log.error("报表异常!", e);
        }
    }

    private void addListDto(Table table3, HfctListDto listDto) {
        try {
            table3.addCell(newCell(listDto.getXh()));//序号
            table3.addCell(newCell(listDto.getJgmc()));//间隔名称
            table3.addCell(newCell(listDto.getSbmc()));//设备名称
            table3.addCell(newCell(listDto.getXw()));//相位
            table3.addCell(newCell(""));//图谱文件
            table3.addCell(newCell(listDto.getFd()));//是否存在放电信号（打勾）
            table3.addCell(newCell(listDto.getFz()));//测试值（峰值）（mV）
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }

    private void addImgDto(Document document, HfctListDto listDto) {
        try {
//            document.add(Image.getInstance(listDto.getTpwj()));
//            Paragraph pg = new Paragraph(listDto.getSbmc() + "-" + listDto.getJgmc() + "--图谱文件");
//            pg.setAlignment(Element.ALIGN_CENTER);
//            document.add(pg);
            addImgDto(document,listDto.getTpwj(),listDto.getSbmc() + "-" + listDto.getJgmc() + "--图谱文件");
        } catch (Exception e) {
            log.error("word列表异常!", e);
        }
    }
}
