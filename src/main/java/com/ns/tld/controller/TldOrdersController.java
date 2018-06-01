/**
 * project name: ns-api
 * file name:TldOrdersController
 * package name:com.ns.tld.controller
 * date:2018-03-10 14:46
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.controller;

import com.alibaba.fastjson.JSONObject;
import com.ns.common.Ytapi;
import com.ns.common.base.BaseController;
import com.ns.common.exception.CustException;
import com.ns.common.json.JsonResult;
import com.ns.common.kit.UrlKit;
import com.ns.common.model.TldOrders;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.Util;
import com.ns.tld.service.TldOrderSplitService;
import com.ns.tld.service.TldOrdersService;
import com.ns.weixin.service.NoticeService;
import com.jfinal.aop.Before;
import com.jfinal.kit.HttpKit;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-03-10 14:46
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldOrdersController extends BaseController {
    static TldOrdersService ordersService = TldOrdersService.me;
    static TldOrderSplitService splitService = TldOrderSplitService.me;
    static NoticeService noticeService = NoticeService.me;

    public void getOrderList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String conNo = getPara("conNo");
        String orderNo = getPara("orderNo");
        Integer status = getParaToInt("status");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        Integer payType = getParaToInt("payType");
        renderJson(JsonResult.newJsonResult(ordersService.getOrderList(pageNumber, pageSize, conNo, orderNo, payType, status, startDt, endDt)));
    }

    public void offlinePay() throws IOException {
        String orderId = getPara("orderId");
        Map map = new HashMap();
        map.put("orderId", orderId);
        String paramStr = UrlKit.getUrlParamsByMap(map, "utf-8");
        renderText(HttpKit.post("http://xhd777.com.cn/ns-api/api/order/offlinePay", paramStr));
    }

    public void getWaybill() throws IOException {
        renderText(Ytapi.ytPost(getPara("billNo")));
    }

    @Before(Tx.class)
    //{"ID":"1","COUNTRY":"中国","PROVINCE":"湖南省","CITY":"长沙市","DISTRICT":"岳麓区","ADDRESS":"五一广场","POSTAL_CODE":"412005","MOBILE":"13814554471","RECIPIENTS":"张三"}
    public void updateOrderAddress() {
        TldOrders orders = Util.getRequestObject(getRequest(), TldOrders.class);
        renderJson(JsonResult.newJsonResult(ordersService.updateOrderAddress(orders)));
    }

    public void getOrderSplitList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String conNo = getPara("conNo");
        String orderNo = getPara("orderNo");
        Integer status = getParaToInt("status");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        //是否已经导出
        Integer isDelivery = getParaToInt("isDelivery");
        renderJson(JsonResult.newJsonResult(splitService.getOrderSplitList(pageNumber, pageSize, conNo, orderNo, status, startDt, endDt, isDelivery)));
    }

    /**
     * 打印
     */
    @Before(Tx.class)
    public void orderPrint() {
        String[] orderIds = Util.getRequestObject(getRequest(), String[].class);
        renderJson(JsonResult.newJsonResult(splitService.orderPrint(orderIds)));

    }

    /**
     * 发货
     */
    @Before(Tx.class)
    public void shipments() {
        String dateTime = DateUtil.getNow();
        String[] orderIds = Util.getRequestObject(getRequest(), String[].class);
        for (int i = 0; i < orderIds.length; i++) {
            String orderId = orderIds[i];
            List<Record> list = Db.find("select CON_ID,ORDER_NO,EXP_COMPANY_NAME,WAYBILL,STATUS from tld_order_split where order_id = ?", orderIds);
            for (Record r : list) {
                if (r.getInt("STATUS") != 5) {
                    throw new CustException("订单:" + r.getStr("ORDER_NO") + ",未打印!");
                }
                if (StrKit.isBlank(r.getStr("WAYBILL"))) {
                    throw new CustException("订单:" + r.getStr("ORDER_NO") + ",没有填写物流单号，不能发货！");
                }
            }
            if (list != null && list.size() > 0) {
                String opneId = Db.queryStr("select OPENID from bas_customer where ID = ?", list.get(0).getStr("CON_ID"));
                Db.update("update tld_orders set status = 6,VERSION =VERSION+1,SHIPPING_DT = ?,UPDATE_DT = ?  where id = ?", dateTime, dateTime, orderId);
                Db.update("update tld_order_split set status = 6,VERSION =VERSION+1,UPDATE_DT = ?  where order_id = ?", dateTime, orderId);
                noticeService.sendOrdershipments(list.get(0).getStr("CON_ID"), opneId, list.get(0).getStr("ORDER_NO"), list.get(0).getStr("EXP_COMPANY_NAME"), list.get(0).getStr("WAYBILL"));
            }

        }
        renderJson(JsonResult.newJsonResult(true));
    }
}
