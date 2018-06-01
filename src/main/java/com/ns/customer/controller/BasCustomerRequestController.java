/**
 * project name: ns-api
 * file name:BasCustomerRequestController
 * package name:com.ns.customer.controller
 * date:2018-03-22 9:55
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.customer.controller;

import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.BasCustomerRequest;
import com.ns.common.utils.Util;
import com.ns.customer.service.BasCustomerRequestService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.math.BigDecimal;

/**
 * description: //TODO <br>
 * date: 2018-03-22 9:55
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class BasCustomerRequestController extends BaseController {
    static BasCustomerRequestService basCustomerRequestService = BasCustomerRequestService.me;

    public void getRequestList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String conNo = getPara("conNo");
        String orderNo = getPara("orderNo");
        Integer requestStatus = getParaToInt("status");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        renderJson(JsonResult.newJsonResult(basCustomerRequestService.getRequestList(pageNumber, pageSize, conNo, orderNo, startDt, endDt, requestStatus)));
    }

    //{"ID","1","EXP_COMPANY_ID":"","EXP_WAYBILL":""}
    public void updateRequest() {
        BasCustomerRequest request = Util.getRequestObject(getRequest(), BasCustomerRequest.class);
        renderJson(JsonResult.newJsonResult(basCustomerRequestService.updateRequest(request)));
    }

    @Before(Tx.class)
    public void custServiceCheck() {
        String id = getPara("id");
        String amount = getPara("amount");
        Integer checkResult = getParaToInt("checkResult");
        String checkRemark = getPara("checkRemark");
        renderJson(JsonResult.newJsonResult(basCustomerRequestService.custServiceCheck(id, new BigDecimal(amount), checkResult, checkRemark)));
    }

    @Before(Tx.class)
    public void finCheck() {
        String id = getPara("id");
        String amount = getPara("amount");
        Integer checkResult = getParaToInt("checkResult");
        String checkRemark = getPara("checkRemark");
        renderJson(JsonResult.newJsonResult(basCustomerRequestService.finCheck(id, new BigDecimal(amount), checkResult, checkRemark)));
    }

    public void getById() {
        String id = getPara("id");
        renderJson(JsonResult.newJsonResult(basCustomerRequestService.getById(id)));
    }

    public void getByOrderId() {
        String orderId = getPara("orderId");
        renderJson(JsonResult.newJsonResult(basCustomerRequestService.getByOrderId(orderId)));
    }

    public Record selectOrderAfterSale() {
        return Db.findFirst("SELECT " +
                "sum(CASE REQUEST_STATUS = 1 WHEN 1 THEN 1 ELSE 0 END ) APPLYING," +
                "sum(CASE REQUEST_STATUS = 2 WHEN 1 THEN 1 ELSE 0 END ) APP_PASS" +
                " FROM " +
                " bas_customer_request");
    }
}
