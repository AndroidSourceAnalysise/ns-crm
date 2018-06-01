/**
 * project name: ns-api
 * file name:PntProductCmtController
 * package name:com.ns.pnt.controller
 * date:2018-02-08 16:40
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.controller;

import com.alibaba.fastjson.JSONObject;
import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.PntProductCmt;
import com.ns.common.utils.Util;
import com.ns.pnt.service.PntProductCmtService;
import com.jfinal.aop.Before;

import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-02-08 16:40
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntProductCmtController extends BaseController {
    static PntProductCmtService cmtService = PntProductCmtService.me;


    //{"CON_ID":"0","PARENT_ID":"主评论ID","SOURCE_ID":"主评论ID","CONTENT":"内容!","TO_CON_ID":"被评论用户ID","TO_CON_NO":"1","TO_CON_NAME":""}
    public void inertCMT() {
        PntProductCmt cmt = Util.getRequestObject(getRequest(), PntProductCmt.class);
        renderJson(JsonResult.newJsonResult(cmtService.inertCMT(cmt)));
    }

    //{"ID":"1","ENABLED":"0"}
    public void updateCMT() {
        List<JSONObject> cmt = Util.getRequestObject(getRequest(), List.class);
        renderJson(JsonResult.newJsonResult(cmtService.updateProductCmt(cmt)));
    }

    public void delete() {
        String[] ids = Util.getRequestObject(getRequest(), String[].class);
        renderJson(JsonResult.newJsonResult(cmtService.delete(ids)));
    }

    public void getPntCmtList() {
        int pageNumber = getParaToInt("pageNumber");
        int pageSize = getParaToInt("pageSize");
        String pntId = getPara("pntId");
        renderJson(JsonResult.newJsonResult(cmtService.getPntCmtList(pageNumber, pageSize, pntId)));
    }

    public void getPntCmtChildren() {
        int pageNumber = getParaToInt("pageNumber");
        int pageSize = getParaToInt("pageSize");
        String id = getPara("id");
        renderJson(JsonResult.newJsonResult(cmtService.getPntCmtChildren(pageNumber, pageSize, id)));
    }
}
