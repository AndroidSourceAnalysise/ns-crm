/**
 * project name: ns-crm
 * file name:TldOrderSplitService
 * package name:com.ns.tld.service
 * date:2018-03-22 13:18
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.service;

import com.ns.common.exception.CustException;
import com.ns.common.model.TldOrderSplit;
import com.ns.common.utils.DateUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.List;
import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-03-22 13:18
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldOrderSplitService {
    private final TldOrderSplit dao = new TldOrderSplit();
    public static final TldOrderSplitService me = new TldOrderSplitService();
    public final String COLUMN = "ID,ORDER_ID,ORDER_NO,CON_ID,CON_NO,CON_NAME,PNT_ID,PNT_NAME,SKU_ID,SKU_NAME,SPLIT_NUMBER,EXP_COMPANY_ID,EXP_COMPANY_NAME,WAYBILL,IS_DELIVERY,COUNTRY,PROVINCE,CITY,DISTRICT,ADDRESS,POSTAL_CODE,MOBILE,RECIPIENTS,SALE_PRICE,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public Page<Record> getOrderSplitList(int pageNumber, int pageSize, String conNo, String orderNo, Integer status, String startDt, String endDt,Integer isDelivery) {
        StringBuffer sqlExceptSelect = new StringBuffer("from tld_order_split where 1=1");
        if (status != null) {
            sqlExceptSelect.append(" and status = " + status);
        }
        if (isDelivery != null) {
            sqlExceptSelect.append(" and IS_DELIVERY = " + isDelivery);
        }
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and con_no = '" + conNo + "'");
        }
        if (StrKit.notBlank(orderNo)) {
            sqlExceptSelect.append(" and order_no = '" + orderNo + "'");
        }
        if (StrKit.notBlank(startDt)) {
            sqlExceptSelect.append(" and CREATE_DT >= " + "'" + startDt + "'");
        }
        if (StrKit.notBlank(endDt)) {
            sqlExceptSelect.append(" and CREATE_DT <= " + "'" + endDt + "'");
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        Page<Record> tldOrdersPage = Db.paginate(pageNumber, pageSize, "select " + COLUMN + "", sqlExceptSelect.toString());
        return tldOrdersPage;
    }

    public boolean orderPrint(String[] orderIds) {
        String dateTime = DateUtil.getNow();
        for (int i = 0; i < orderIds.length; i++) {
            String orderId = orderIds[i];
//            List<TldOrderSplit> splitList = dao.find("select " + COLUMN + " from tld_order_split where order_id = ?", orderId);
//            for (TldOrderSplit split : splitList) {
//                Map<String, String> result = createOrderModeB(split);
//                try {
//                    split.setExpCompanyId(result.get("logisticProviderID"));
//                    split.setExpCompanyName("圆通快递");
//                    split.setWAYBILL(result.get("mailNo"));
//                    split.setSTATUS(5);
//                    split.setUpdateDt(DateUtil.getNow());
//                    split.update();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
            Db.update("update tld_orders set status = 5,VERSION =VERSION+1,UPDATE_DT = ?  where id = ?", dateTime, orderId);
            Db.update("update tld_order_split set status = 5,VERSION =VERSION+1,UPDATE_DT = ?  where order_id = ?", dateTime, orderId);
        }
        return true;
    }

    public Map<String, String> createOrderModeB(TldOrderSplit split) {
        Map<String, String> resultMap = null;
        //String apiUrl = "http://service.yto56.net.cn/CommonOrderModeBPlusServlet.action";
//        String parternId = "7mChOLQN";
//        String clientId = "K280260163";
//        String customerId = "K280260163";
        String apiUrl = "http://58.32.246.71:8000/CommonOrderModeBPlusServlet.action";
        String parternId = "u2Z1F7Fh";
        String clientId = "K21000119";
        String customerId = "K21000119";
        try {
            //打开连接
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
            //数据
            StringBuilder xmlBuilder = new StringBuilder();
            xmlBuilder.append("<RequestOrder>");
            xmlBuilder.append("    <clientID>" + clientId + "</clientID>");
            xmlBuilder.append("    <logisticProviderID>YTO</logisticProviderID>");
            xmlBuilder.append("    <customerId>" + customerId + "</customerId>");
            xmlBuilder.append("    <txLogisticID>" + "LP" + split.getID() + "0" + "</txLogisticID>");
            //xmlBuilder.append("    <tradeNo>1</tradeNo>");
            //xmlBuilder.append("    <totalServiceFee>1</totalServiceFee>");
            //xmlBuilder.append("    <codSplitFee>1</codSplitFee>");
            xmlBuilder.append("    <orderType>1</orderType>");
            xmlBuilder.append("    <serviceType>1</serviceType>");
            xmlBuilder.append("    <flag>1</flag>");
            //xmlBuilder.append("    <sendStartTime>2014-03-06 12:12:12</sendStartTime>");
            //xmlBuilder.append("    <sendEndTime>2014-03-06 12:12:12</sendEndTime>");
            //xmlBuilder.append("    <goodsValue>1</goodsValue>");
            //xmlBuilder.append("    <itemsValue>1</itemsValue>");
            //xmlBuilder.append("    <insuranceValue>1</insuranceValue>");
            //xmlBuilder.append("    <special>1</special>");
            xmlBuilder.append("    <remark>测试</remark>");
            //xmlBuilder.append("    <deliverNo>1</deliverNo>");
            xmlBuilder.append("    <type>1</type>");
            //xmlBuilder.append("    <totalValue>1</totalValue>");
            //xmlBuilder.append("    <itemsWeight>1</itemsWeight>");
            //xmlBuilder.append("    <packageOrNot>1</packageOrNot>");
            //xmlBuilder.append("    <orderSource>1</orderSource>");
            xmlBuilder.append("    <sender>");
            xmlBuilder.append("        <name>测试</name>");
            xmlBuilder.append("        <postCode>123456</postCode>");
            xmlBuilder.append("        <phone>1234567</phone>");
            xmlBuilder.append("        <mobile>18221885929</mobile>");
            xmlBuilder.append("        <prov>上海</prov>");
            xmlBuilder.append("        <city>上海,青浦区</city>");
            xmlBuilder.append("        <address>上海市青浦区华徐公路民兴大道</address>");
            xmlBuilder.append("    </sender>");
            xmlBuilder.append("    <receiver>");
            xmlBuilder.append("        <name>" + split.getRECIPIENTS() + "</name>");
            xmlBuilder.append("        <postCode>" + split.getPostalCode() + "</postCode>");
            xmlBuilder.append("        <phone>" + split.getMOBILE() + "</phone>");
            xmlBuilder.append("        <mobile>" + split.getMOBILE() + "</mobile>");
            xmlBuilder.append("        <prov>" + split.getPROVINCE() + "</prov>");
            xmlBuilder.append("        <city>" + split.getCITY() + "</city>");
            xmlBuilder.append("        <address>" + split.getADDRESS() + "</address>");
            xmlBuilder.append("    </receiver>");
            xmlBuilder.append("    <items>");
            xmlBuilder.append("        <item>");
            xmlBuilder.append("            <itemName>" + split.getPntName() + ":" + split.getSkuName() + "</itemName>");
            xmlBuilder.append("            <number>" + split.getSplitNumber() + "</number>");
            xmlBuilder.append("            <itemValue>" + split.getSalePrice() + "</itemValue>");
            xmlBuilder.append("        </item>");
            xmlBuilder.append("    </items>");
            xmlBuilder.append("</RequestOrder>");
            //加密
            MessageDigest messagedigest = MessageDigest.getInstance("MD5");
            messagedigest.update((xmlBuilder.toString() + parternId).getBytes("UTF-8"));
            byte[] abyte0 = messagedigest.digest();
            String data_digest = new String(Base64.encodeBase64(abyte0));
            //开始时间
            long a = System.currentTimeMillis();

            //查询
            String queryString = "logistics_interface=" + URLEncoder.encode(xmlBuilder.toString(), "UTF-8")
                    + "&data_digest=" + URLEncoder.encode(data_digest, "UTF-8")
                    + "&clientId=" + URLEncoder.encode(clientId, "UTF-8");
            out.write(queryString);
            out.flush();
            out.close();
            //获取服务端的反馈
            String responseString = "";
            String strLine = "";
            InputStream in = connection.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            while ((strLine = reader.readLine()) != null) {
                responseString += strLine + "\n";
            }
            in.close();

            //结束时间
            long b = System.currentTimeMillis();

            //响应时间
            long c = b - a;
            //System.err.print("响应时间：" + c + "ms\n");
            resultMap = PaymentKit.xmlToMap(responseString);
            if (resultMap.get("success").equals("true")) {
                return resultMap;
            } else {
                throw new CustException(resultMap.get("reason"));
            }
        } catch (Exception e) {
            if (e instanceof CustException) {
                CustException custException = (CustException) e;
                throw custException;
            } else {
                throw new CustException(resultMap.get("reason"));
            }
        }
    }
}
