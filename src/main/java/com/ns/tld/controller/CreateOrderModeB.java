package com.ns.tld.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.weixin.sdk.kit.PaymentKit;
import org.apache.commons.codec.binary.Base64;

/**
 * 创建订单
 * @author luojia
 *
 */
public class CreateOrderModeB {

	public static void main(String[] args) {
		//String apiUrl = "http://112.64.239.247:7800/web/CommonOrderModeBServlet.action";
		String apiUrl = "http://58.32.246.71:8000/CommonOrderModeBPlusServlet.action";
//		String apiUrl = "http://service.yto56.net.cn/CommonOrderModeBServlet.action";
//		String apiUrl = "http://localhost:8088/CommonOrderServlet.action";
//		String apiUrl = "http://localhost:8088/CommonOrderModeBServlet.action";//B模式接口地址
//		String apiUrl = "http://192.168.8.10:8088/web/CommonOrderServlet.action";
		String parternId = "u2Z1F7Fh";
//		String clientId = "KGJB00001";
//		String customerId = "KGJB00001";
		//String parternId = "7XnZRHNx";
		//String customerId = "KGJB00001";
		String clientId = "K21000119";
		String customerId = "K21000119";
//		String parternId = "123456";
		//String mailNo = "5555555358";//申请但没有使用过的电子面单号
		
		String array[] = {
				GUIDUtil.getGUID()
		};

		for(int i=0; i<array.length; i++){
		try{
			//打开连接
			URL url = new URL(apiUrl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			OutputStreamWriter out = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
			//数据
			StringBuilder xmlBuilder = new StringBuilder();
			xmlBuilder.append("<RequestOrder>");
			xmlBuilder.append("    <clientID>"+clientId+"</clientID>");
			xmlBuilder.append("    <logisticProviderID>YTO</logisticProviderID>");
			xmlBuilder.append("    <customerId>"+customerId+"</customerId>");
			xmlBuilder.append("    <txLogisticID>"+"LP"+array[i]+"</txLogisticID>");
			xmlBuilder.append("    <tradeNo>1</tradeNo>");
			xmlBuilder.append("    <totalServiceFee>1</totalServiceFee>");
			xmlBuilder.append("    <codSplitFee>1</codSplitFee>");
			xmlBuilder.append("    <orderType>1</orderType>");
			xmlBuilder.append("    <serviceType>1</serviceType>");
			xmlBuilder.append("    <flag>1</flag>");
			xmlBuilder.append("    <sendStartTime>2014-03-06 12:12:12</sendStartTime>");
			xmlBuilder.append("    <sendEndTime>2014-03-06 12:12:12</sendEndTime>");
			xmlBuilder.append("    <goodsValue>1</goodsValue>");
			xmlBuilder.append("    <itemsValue>1</itemsValue>");
			xmlBuilder.append("    <insuranceValue>1</insuranceValue>");
			xmlBuilder.append("    <special>1</special>");
			xmlBuilder.append("    <remark>1</remark>");
			xmlBuilder.append("    <deliverNo>1</deliverNo>");
			xmlBuilder.append("    <type>1</type>");
			xmlBuilder.append("    <totalValue>1</totalValue>");
			xmlBuilder.append("    <itemsWeight>1</itemsWeight>");
			xmlBuilder.append("    <packageOrNot>1</packageOrNot>");
			xmlBuilder.append("    <orderSource>1</orderSource>");
			xmlBuilder.append("    <sender>");
			xmlBuilder.append("        <name>汪明新</name>");
			xmlBuilder.append("        <postCode>123456</postCode>");
			xmlBuilder.append("        <phone>1234567</phone>");
			xmlBuilder.append("        <mobile>18221885929</mobile>");
			xmlBuilder.append("        <prov>上海</prov>");
			xmlBuilder.append("        <city>上海,青浦区</city>");
			xmlBuilder.append("        <address>上海市青浦区华徐公路民兴大道</address>");
			xmlBuilder.append("    </sender>");
			xmlBuilder.append("    <receiver>");
			xmlBuilder.append("        <name>汪明新</name>");
			xmlBuilder.append("        <postCode>123456</postCode>");
			xmlBuilder.append("        <phone>1234567</phone>");
			xmlBuilder.append("        <mobile>18221885929</mobile>");
			xmlBuilder.append("        <prov>浙江省</prov>");
			xmlBuilder.append("        <city>金华市</city>");
			xmlBuilder.append("        <address>赤松路308号</address>");
//			xmlBuilder.append("        <prov>宁夏回族自治区</prov>");
//			xmlBuilder.append("        <city>固原市,西吉县</city>");
//			xmlBuilder.append("        <address>宁夏回族自治区固原市西吉县新营乡</address>");
			xmlBuilder.append("    </receiver>");
			xmlBuilder.append("    <items>");
			xmlBuilder.append("        <item>");
			xmlBuilder.append("            <itemName>36ab0b08-3b5c-4423-a352-08477f050e55</itemName>");
			xmlBuilder.append("            <number>2</number>");
			xmlBuilder.append("            <itemValue>50</itemValue>");
			xmlBuilder.append("        </item>");
			xmlBuilder.append("        <item>");
			xmlBuilder.append("            <itemName>0a4e51b9-5616-4feb-b8a8-d2e1ba24401f</itemName>");
			xmlBuilder.append("            <number>2</number>");
			xmlBuilder.append("            <itemValue>50</itemValue>");
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
					+ "&data_digest="+ URLEncoder.encode(data_digest,"UTF-8")
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
			System.err.print("响应时间："+c + "ms\n");
			
			System.err.print("请求的返回信息："+responseString);
			Map<String, String> params = PaymentKit.xmlToMap(responseString);
			System.out.println(JSON.toJSONString(params));
		}catch(Exception e){
			e.printStackTrace();
		}
		}
	}

}
