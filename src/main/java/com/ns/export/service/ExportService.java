package com.ns.export.service;

import com.ns.common.exception.CustException;
import com.ns.common.utils.ExcelExportUtil;
import com.ns.common.utils.ExcelImportUtil;
import com.jfinal.kit.PathKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.upload.UploadFile;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.CellStyle;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * description: //TODO <br>
 * date: 2018-04-26 16:12
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class ExportService {
    public static final ExportService me = new ExportService();

    /**
     * 导出分单excel表格操作
     *
     * @return
     */
    public HSSFWorkbook export(String[] splitId) {
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < splitId.length; i++) {
            List<Record> list = Db.find("select * from tld_order_split  where id = ?", splitId[i]);
            for (Record r : list) {
                if (r.getInt("STATUS") != 5) {
                    throw new CustException("订单:" + r.getStr("ORDER_NO") + ",未打印!");
                }
                if (r.getInt("IS_DELIVERY") == 1) {
                    throw new CustException("订单:" + r.getStr("ORDER_NO") + ",已经导出,不能重复导出!");
                }
                r.set("FH_NAME", "弘德苑文化传媒有限公司");
                r.set("FH_ADDRESS", "四川省成都市郫县金融中心5楼");
                r.set("FH_MOBILE", "18890363492");
                r.set("PNT_NAME", r.getStr("PNT_NAME") + "-" + r.getStr("SKU_NAME"));
                r.set("ADDRESS", r.getStr("PROVINCE") + r.getStr("CITY") + r.getStr("DISTRICT") + r.getStr("ADDRESS"));
            }
            records.addAll(list);
        }
        Map<String, String> titleData = new LinkedHashMap<>();//标题，后面用到
        titleData.put("ID", "物流订单号");
        titleData.put("PNT_NAME", "商品名称");
        titleData.put("SPLIT_NUMBER", "数量");
        titleData.put("RECIPIENTS", "收货人姓名");
        titleData.put("ADDRESS", "收货人地址");
        titleData.put("MOBILE", "收货人手机号");
        titleData.put("FH_NAME", "发货人姓名");
        titleData.put("FH_MOBILE", "发货人电话");
        titleData.put("FH_ADDRESS", "发货人地址");
        titleData.put("FH_POSTAL_CODE", "发件人邮编");
        titleData.put("HK", "代收货款");
        titleData.put("REMARK", "备注");
        titleData.put("FH_POSTAL_CODE", "收货人邮编");
        titleData.put("PHONE", "收件人固话");
        titleData.put("BJJE", "报价金额");
        return ExcelExportUtil.saveFile(titleData, records,false);
    }

    /**
     * 导出订单列表
     * @param orderId
     * @return
     */
    public HSSFWorkbook exportOrder(String[] orderId) {
        List<Record> records = new ArrayList<>();
        for (int i = 0; i < orderId.length; i++) {
            List<Record> list = Db.find("select ID,ORDER_NO,CON_NO,CON_NAME,PAY_DT,SHIPPING_DT,CONFIRM_DT,COUNTRY," +
                    "PROVINCE,CITY,DISTRICT,ADDRESS,MOBILE,RECIPIENTS,FREIGHT,PAYMENT_TYPE,ORDER_SOURCE,ORDER_TYPE,ORDER_TOTAL,COUPON_AMOUNT,INTEGRAL_AMOUNT,PNT_AMOUNT,CREATE_DT from tld_orders  where id = ?", orderId[i]);
            records.addAll(list);
        }
        Map<String, String> titleData = new LinkedHashMap<>();//标题，后面用到
        titleData.put("ORDER_NO", "订单编号");
        titleData.put("CON_NO", "会员号");
        titleData.put("CON_NAME", "会员昵称");
        titleData.put("PAY_DT", "订单付款时间");
        titleData.put("SHIPPING_DT", "发货时间");
        titleData.put("CONFIRM_DT", "收货确认时间");
        titleData.put("COUNTRY", "国家");
        titleData.put("PROVINCE", "省");
        titleData.put("CITY", "城市");
        titleData.put("DISTRICT", "城区");
        titleData.put("ADDRESS", "地址");
        titleData.put("MOBILE", "收货人移动电话");
        titleData.put("RECIPIENTS", "收货人姓名");
        titleData.put("FREIGHT", "运费");
        titleData.put("PAYMENT_TYPE", "付款类型");
        titleData.put("ORDER_SOURCE", "订单来源：1微信  2APP");
        titleData.put("ORDER_TYPE", "订单类型：1：销售订单");
        titleData.put("ORDER_TOTAL", "订单支付总费用，订单实收款");
        titleData.put("COUPON_AMOUNT", "优惠券支付金额");
        titleData.put("INTEGRAL_AMOUNT", "积分抵扣金额");
        titleData.put("PNT_AMOUNT", "商品总价格");
        //titleData.put("STATUS", "订单支付总费用，订单实收款");
        titleData.put("CREATE_DT", "创建日期");
        return ExcelExportUtil.saveFile(titleData, records,true);
    }
    /**
     * 导入Excel
     *
     * @param files
     * @return
     */
    public Object importExcel(UploadFile files) throws IOException {
        if (files == null || StrKit.isBlank(files.getFileName())) {
            throw new CustException("请选择excel文件");
        } else {
            String filename = PathKit.getWebRootPath() + "/upload/"
                    + files.getFileName();
            filename = filename.replaceAll("\\\\", "/");
            int cells = 0;
            int rows = 0;
            int ColCount = 24;//导入的字段数
            HSSFWorkbook wookbook = new HSSFWorkbook(new FileInputStream(filename));
            HSSFSheet sheet = wookbook.getSheetAt(0);
            rows = sheet.getPhysicalNumberOfRows();
            Object[][] paras = new Object[rows - 1][ColCount];
            for (int i = 0; i < rows; i++) {
                HSSFRow row = sheet.getRow(i);
                cells = row.getPhysicalNumberOfCells();
                if (row != null) {
                    if (i == 0) {
                        if (cells != ColCount) {
                            throw new CustException("请选择excel文件");
                        }
                    } else {
                        for (int j = 0; j < cells; j++) {
                            HSSFCell cell = row.getCell(j);
                            if (cell != null) {
                                switch (cell.getCellType()) {
                                    case HSSFCell.CELL_TYPE_FORMULA:
                                        break;
                                    case HSSFCell.CELL_TYPE_NUMERIC:
                                        if (HSSFDateUtil.isCellDateFormatted(cell)) {// 处理日期格式、时间格式
                                            SimpleDateFormat sdf = null;
                                            if (cell.getCellStyle().getDataFormat() == HSSFDataFormat
                                                    .getBuiltinFormat("h:mm")) {
                                                sdf = new SimpleDateFormat("HH:mm");
                                            } else {// 日期
                                                sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                            }
                                            Date date = cell.getDateCellValue();
                                            paras[i - 1][j] = sdf.format(date);
                                        } else if (cell.getCellStyle().getDataFormat() == 58) {
                                            // 处理自定义日期格式：m月d日(通过判断单元格的格式id解决，id的值是58)
                                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                            double value = cell.getNumericCellValue();
                                            Date date = org.apache.poi.ss.usermodel.DateUtil
                                                    .getJavaDate(value);
                                            paras[i - 1][j] = sdf.format(date);
                                        } else {
                                            double value = cell.getNumericCellValue();
                                            CellStyle style = cell.getCellStyle();
                                            DecimalFormat format = new DecimalFormat();
                                            String temp = style.getDataFormatString();
                                            // 单元格设置成常规
                                            if (temp.equals("General")) {
                                                format.applyPattern("#");
                                            }
                                            paras[i - 1][j] = format.format(value);
                                        }
                                        //										paras[i-1][j] = cell.getNumericCellValue();
                                        break;
                                    case HSSFCell.CELL_TYPE_STRING:
                                        paras[i - 1][j] = cell.getStringCellValue();
                                        break;
                                    default:
                                        paras[i - 1][j] = null;
                                        break;
                                }
                            }
                        }
                    }
                }
            }
            for (int i = 0; i < paras.length; i++) {
                for (int j = 0; j < paras[i].length; j++) {
                    if (j == 0 || j == 1) {//非空的列
                        if (paras[i][j] == null || validateIsBlank((String) paras[i][j])) {
                            throw new CustException("第" + (i + 2) + "行，第" + (j + 1) + "列不能为空！");
                        }
                    }

                }
                Db.update("update  `tld_order_split` set WAYBILL = ? ,EXP_COMPANY_NAME = ? where ID = ?", paras[i][1], "圆通快递", paras[i][0]);
            }
            ExcelImportUtil.deleteFile(new File(filename));//录入完将上传的文件删除
            return true;
        }
    }


    /**
     * 判断字符串是否是null或者空
     *
     * @param str 校验的字符串
     * @return 空的或null返回true
     */
    private boolean validateIsBlank(String str) {
        return str.isEmpty() || str == null;
    }
}