package com.ns.common.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.*;

import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public class ExcelExportUtil {

    public static HSSFWorkbook saveFile(Map<String, String> headData, List<Record> list, boolean isOrder) {
        // 创建工作薄
        HSSFWorkbook hssfWorkbook = new HSSFWorkbook();

        //标题行的style
        CellStyle titleCellStyle = hssfWorkbook.createCellStyle();
        titleCellStyle.setAlignment(CellStyle.ALIGN_CENTER);                //居中
        //titleCellStyle.setWrapText(true);                                    //自动换行
        Font font = hssfWorkbook.createFont();
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);                        //加粗
        font.setFontName("微软雅黑");
        titleCellStyle.setFont(font);
        titleCellStyle.setFillForegroundColor(HSSFColor.LIME.index);
        titleCellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        titleCellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN); //下边框
        titleCellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);//左边框
        titleCellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);//上边框
        titleCellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);//右边框

        //内容行的style
        CellStyle cellStyle = hssfWorkbook.createCellStyle();
        cellStyle.setVerticalAlignment(CellStyle.VERTICAL_CENTER);    //垂直居中
        cellStyle.setWrapText(true);
        Font font2 = hssfWorkbook.createFont();
        font2.setFontName("微软雅黑");
        cellStyle.setFont(font2);
        // sheet:一张表的简称
        // row:表里的行
        // 创建工作薄中的工作表
        HSSFSheet hssfSheet = hssfWorkbook.createSheet();
        // 创建行
        HSSFRow row = hssfSheet.createRow(0);
        // 创建单元格，设置表头 创建列
        HSSFCell cell = null;
        // 初始化索引
        int rowIndex = 0;
        int cellIndex = 0;

        // 创建标题行
        row = hssfSheet.createRow(rowIndex);
        rowIndex++;
        // 遍历标题
        for (String h : headData.keySet()) {
            //创建列
            cell = row.createCell(cellIndex);

            row.setHeight((short) 450);
            hssfSheet.setColumnWidth(cellIndex, 9700);
            cell.setCellStyle(titleCellStyle);                //设置样式
            //索引递增
            cellIndex++;
            //逐列插入标题
            cell.setCellValue(headData.get(h));
        }

        // 得到所有记录 行：列

        Record record = null;

        if (list != null) {
            // 获取所有的记录 有多少条记录就创建多少行
            for (int i = 0; i < list.size(); i++) {
                row = hssfSheet.createRow(rowIndex);
                // 得到所有的行 一个record就代表 一行
                record = list.get(i);
                //下一行索引
                rowIndex++;
                //刷新新行索引
                cellIndex = 0;
                // 在有所有的记录基础之上，便利传入进来的表头,再创建N行
                for (String h : headData.keySet()) {
                    cell = row.createCell(cellIndex);
                    cell.setCellStyle(cellStyle);                //设置样式
                    cellIndex++;
                    //按照每条记录匹配数据
                    cell.setCellValue(record.get(h) == null ? "" : record.get(h).toString());
                }
                //如果是分单，则改分单状态
                if (!isOrder) {
                    Db.update("update tld_order_split set IS_DELIVERY = 1 where id = ?", record.get("ID"));
                }
            }
        }
        return hssfWorkbook;
    }
}