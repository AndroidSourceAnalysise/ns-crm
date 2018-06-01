/**
 * project name: ns-api
 * file name:NodeContentController
 * package name:com.ns.node.controller
 * date:2018-03-29 16:14
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.node.controller;

import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.NodeContent;
import com.ns.common.utils.Util;
import com.ns.node.service.NodeContentService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.tx.Tx;

/**
 * description: //TODO <br>
 * date: 2018-03-29 16:14
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class NodeContentController extends BaseController {
    static NodeContentService nodeContentService = NodeContentService.me;

    /**
     * 根据分类查笔记
     */
    public void getList() {
        String conNo = getPara("conNo");
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        String categoryId = getPara("categoryId");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        renderJson(JsonResult.newJsonResult(nodeContentService.getList(conNo, pageNumber, pageSize, categoryId, startDt, endDt)));
    }


    /**
     * 删除笔记
     */
    @Before(Tx.class)
    public void delete() {
        String[] ids = Util.getRequestObject(getRequest(), String[].class);
        for (int i = 0; i < ids.length; i++) {
            nodeContentService.delete(ids[i]);
        }
        renderJson(JsonResult.newJsonResult(true));
    }

    @Before(Tx.class)
    public void disable() {
        String[] ids = Util.getRequestObject(getRequest(), String[].class);
        for (int i = 0; i < ids.length; i++) {
            Db.update("update node_content set ENABLED = 0  where id = ? ", ids[i]);
        }
        renderJson(JsonResult.newJsonResult(true));
    }
}
