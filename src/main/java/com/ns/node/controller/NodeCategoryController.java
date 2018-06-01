/**
 * project name: ns-crm
 * file name:NodeCategoryController
 * package name:com.ns.node.controller
 * date:2018-03-27 16:49
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.node.controller;

import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.NodeCategory;
import com.ns.common.utils.Util;
import com.ns.node.service.NodeCategoryService;

/**
 * description: //TODO <br>
 * date: 2018-03-27 16:49
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class NodeCategoryController extends BaseController {
    static NodeCategoryService nodeCategoryService = NodeCategoryService.me;

    //{"NAME":"育儿专家","NODE_INDEX":"1","DESCRIPTION":"育儿专家"}
    public void insert() {
        NodeCategory nodeCategory = Util.getRequestObject(getRequest(), NodeCategory.class);
        renderJson(JsonResult.newJsonResult(nodeCategoryService.insert(nodeCategory)));
    }

    //{"ID":"1","NAME":"育儿专家","NODE_INDEX":"1","DESCRIPTION":"育儿专家"}
    public void update() {
        NodeCategory nodeCategory = Util.getRequestObject(getRequest(), NodeCategory.class);
        renderJson(JsonResult.newJsonResult(nodeCategoryService.update(nodeCategory)));
    }

    public void delete() {
        renderJson(JsonResult.newJsonResult(nodeCategoryService.delete(getPara("id"))));
    }

    public void getList() {
        renderJson(JsonResult.newJsonResult(nodeCategoryService.getList()));
    }
    public void getById() {
        renderJson(JsonResult.newJsonResult(nodeCategoryService.getById(getPara("id"))));
    }
}
