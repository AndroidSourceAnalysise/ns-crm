/**
 * project name: hdy_project
 * file name:BasCustomerExtController
 * package name:com.ns.customer.controller
 * date:2018-02-01 21:49
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.customer.controller;

import com.ns.common.exception.CustException;
import com.ns.common.json.JsonResult;
import com.ns.common.model.BasCustomerExt;
import com.ns.common.model.BasCustomerRequest;
import com.ns.common.utils.Util;
import com.ns.customer.service.BasCustPointsService;
import com.ns.customer.service.BasCustomerExtService;
import com.jfinal.core.Controller;

/**
 * description: //TODO <br>
 * date: 2018-02-01 21:49
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class BasCustomerExtController extends Controller {
    static BasCustomerExtService extService = BasCustomerExtService.me;
    static BasCustPointsService basCustPointsService = BasCustPointsService.me;

    public void getPointTransList() {
        String conNo = getPara("conNo");
        String orderNo = getPara("orderNo");
        int pageNumber = getParaToInt("pageNumber");
        int pageSize = getParaToInt("pageSize");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        Integer pointsType = getParaToInt("pointsType");
        renderJson(JsonResult.newJsonResult(basCustPointsService.getPointTransList(pageNumber, pageSize, conNo, orderNo, pointsType, startDt, endDt)));
    }

    //{"ID":"1","POINTS_ENABLED":"可用积分(剩余积分)","POINTS_TOTAL":"累计总积分","POINTS_CFMD":"已确认积分","POINTS_UNCFMD":"未确认积分"}
    public void update() {
        BasCustomerExt ext = Util.getRequestObject(getRequest(), BasCustomerExt.class);
        renderJson(JsonResult.newJsonResult(extService.update(ext)));
    }

    /**
     * 修正积分
     */
    //{"ID":"1","POINTS_ENABLED":"可用积分(剩余积分)"}
    public void amendPoints() {
        BasCustomerExt params = Util.getRequestObject(getRequest(), BasCustomerExt.class);
        BasCustomerExt ext = extService.getById(params.getID());
        int updatePoints = params.getPointsEnabled() - ext.getPointsEnabled();
        params.setPointsTotal(ext.getPointsTotal() + updatePoints);//总积分
        params.setPointsCfmd(ext.getPointsCfmd() + updatePoints);//已确认积分
        renderJson(JsonResult.newJsonResult(extService.update(params)));
    }

    public void getList() {
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        String conNo = getPara("conNo");
        renderJson(JsonResult.newJsonResult(extService.getList(conNo, pageNumber, pageSize)));
    }
}
