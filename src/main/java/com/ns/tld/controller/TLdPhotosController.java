/**
 * project name: ns-crm
 * file name:TLdPhotosController
 * package name:com.ns.tld.controller
 * date:2018-02-09 17:08
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.controller;

import com.alibaba.fastjson.JSONObject;
import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.TldPhotos;
import com.ns.common.utils.Util;
import com.ns.tld.service.TldPhotosService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-02-09 17:08
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TLdPhotosController extends BaseController {
    static TldPhotosService tldPhotosService = TldPhotosService.me;

    //{"RELATION_ID":"商品ID","SYS_ID":"1","TYPE":"4","URL":"图片地址","HREF_URL":"链接地址，用于跳转","DISPLAY_SEQ":"1"}
    @Before(Tx.class)
    public void addPotos() {
        List<JSONObject> tldPhotos = Util.getRequestObject(getRequest(), List.class);
        renderJson(JsonResult.newJsonResult(tldPhotosService.addPotos(tldPhotos)));
    }

    @Before(Tx.class)
    public void updatePotos() {
        List<JSONObject> tldPhotos = Util.getRequestObject(getRequest(), List.class);
        renderJson(JsonResult.newJsonResult(tldPhotosService.updatePotos(tldPhotos)));
    }

    @Before(Tx.class)
    public void deletePotos() {
        String id = getPara("id");
        renderJson(JsonResult.newJsonResult(tldPhotosService.deletePotos(id)));
    }

    public void getPhotos() {
        Integer sysId = getParaToInt("sysId");
        Integer type = getParaToInt("type");
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        renderJson(JsonResult.newJsonResult(tldPhotosService.getPhotos(sysId, type, pageNumber, pageSize)));
    }
}
