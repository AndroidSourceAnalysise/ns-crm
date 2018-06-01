/**
 * project name: hdy_project
 * file name:BasCustomerController
 * package name:com.ns.customer.controller
 * date:2018-02-01 17:24
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.customer.controller;

import com.ns.common.MyConfig;
import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.BasCustomer;
import com.ns.common.model.TldOrders;
import com.ns.common.task.Task;
import com.ns.common.task.TaskQueuePlugin;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.Util;
import com.ns.customer.service.BasCustomerExtService;
import com.ns.customer.service.BasCustomerService;
import com.ns.customer.task.CreateCustomerTask;
import com.ns.tld.service.TldOrdersService;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.weixin.sdk.api.ApiConfigKit;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.UserApi;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-02-01 17:24
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class BasCustomerController extends BaseController {
    static BasCustomerService service = BasCustomerService.me;
    static BasCustomerExtService extService = BasCustomerExtService.me;
    static TldOrdersService ordersService = TldOrdersService.me;

    public void test() {
        //oCMeO0qn2r-PEEuLFsNdWoebMq_g
        ApiConfigKit.putApiConfig(MyConfig.getApiConfig());
        ApiResult apiResult = UserApi.getUserInfo("oCMeO0qn2r-PEEuLFsNdWoebMq_g");
        renderText(apiResult.getJson());
    }

    public void getCustomerList() {
        final int pageNumber = getParaToInt("pageNumber", 1);
        final int pageSize = getParaToInt("pageSize", 5);
        final String conNo = getPara("conNo");
        final String mobile = getPara("mobile");
        renderJson(JsonResult.newJsonResult(service.getCustomerList(pageNumber, pageSize, conNo, mobile)));
    }

    public void getById() {
        renderJson(JsonResult.newJsonResult(service.getCustomerByIdNotNull(getPara("conId"))));
    }

    public void getByConNo() {
        renderJson(JsonResult.newJsonResult(service.getCustomerByConNoNotNull(getPara("conNo"))));
    }

    public void getByOpenId() {
        renderJson(JsonResult.newJsonResult(service.getCustomerByOpenIdNotNull(getPara("openId"))));
    }

    public void getInfo() {
        Map map = new HashMap();
        String conId = getPara("conId");
        map.put("customer", service.getCustomerById(conId));
        map.put("customerExt", extService.getByConId(conId));
        renderJson(JsonResult.newJsonResult(map));
    }

    public void getMyCustomer() {
        final int pageNumber = getParaToInt("pageNumber", 1);
        final int pageSize = getParaToInt("pageSize", 5);
        renderJson(JsonResult.newJsonResult(service.getMyCustomerList(pageNumber, pageSize, getPara("conId"))));
    }

    //{"ID":"1","CON_NAME":"123","MOBILE":"123455666","IS_LOCKOUT":"：0未锁定  1：锁定","RP_NO":"上级会员编码"}
    public void updateCustomer() {
        BasCustomer customer = Util.getRequestObject(getRequest(), BasCustomer.class);
        renderJson(JsonResult.newJsonResult(service.updateCustomer(customer)));
    }

    public void homeOrder() {
        int type = getParaToInt("type");
        String startDate = returnDate(type);
        Map map = ordersService.getOrderStatistics(startDate);
        renderJson(JsonResult.newJsonResult(map));
    }

    private String returnDate(int type) {
        String startDate = "";
        switch (type) {
            case 0:
                startDate = DateUtil.getNow("yyyy-MM-dd");
                break;
            case 1:
                startDate = DateUtil.getNow("yyyy-MM");
                break;
            default:
                break;
        }
        return startDate;
    }

    public void homeCustomer() {
        String startDate = returnDate(getParaToInt("type"));
        Map<String, Object> resultMap = new HashMap<>();
        //总人数
        int total = Db.queryInt("select count(1) from bas_customer");
        resultMap.put("TOTAL", total);
        //关注人数
        int subscribeNum = Db.queryInt("select count(1) from bas_customer where IS_SUBSCRIBE = 0");
        resultMap.put("SUBSCRIBE_NUM", subscribeNum);
        //非推客
        int notTwitterNum = Db.queryInt("select count(1) from bas_customer where CON_TYPE = 0 and create_dt like '" + startDate + "%'");
        resultMap.put("NOT_TWITTER_NUM", notTwitterNum);
        //推客
        int twitterNum = Db.queryInt("select count(1) from bas_customer where CON_TYPE = 1 and create_dt like '" + startDate + "%'");
        resultMap.put("TWITTER_NUM", twitterNum);
        BigDecimal conversion_rate;
        if (twitterNum == 0) {
            conversion_rate = BigDecimal.ZERO;
        } else {
            conversion_rate = new BigDecimal(twitterNum).divide((new BigDecimal(notTwitterNum).add(new BigDecimal(twitterNum))),2, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal(100));
        }
        resultMap.put("CONVERSION_RATE", conversion_rate);
        renderJson(JsonResult.newJsonResult(resultMap));
    }

    public void homeRequest() {
        Record record = Db.findFirst("SELECT " +
                "sum(CASE REQUEST_STATUS = 1 WHEN 1 THEN 1 ELSE 0 END ) APPLYING," +
                "sum(CASE REQUEST_STATUS = 2 WHEN 1 THEN 1 ELSE 0 END ) APP_PASS" +
                " FROM " +
                " bas_customer_request");
        renderJson(JsonResult.newJsonResult(record));
    }
}
