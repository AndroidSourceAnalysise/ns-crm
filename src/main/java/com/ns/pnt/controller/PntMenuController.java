/**
 * project name: ns-api
 * file name:PntMenuController
 * package name:com.ns.pnt.controller
 * date:2018-04-03 10:36
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.controller;

import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.PntMenu;
import com.ns.common.utils.Util;
import com.ns.pnt.service.PntMenuService;

/**
 * description: //TODO <br>
 * date: 2018-04-03 10:36
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntMenuController extends BaseController {
    static PntMenuService service = PntMenuService.me;

    public void insert() {
        PntMenu pntMenu = Util.getRequestObject(getRequest(), PntMenu.class);
        renderJson(JsonResult.newJsonResult(service.insert(pntMenu)));
    }

    public void update() {
        PntMenu pntMenu = Util.getRequestObject(getRequest(), PntMenu.class);
        renderJson(JsonResult.newJsonResult(service.update(pntMenu)));
    }

    public void delete() {
        renderJson(JsonResult.newJsonResult(service.deleteById(getPara("id"))));
    }

    public void getList() {
        Integer pageNumber = getParaToInt("pageNumber");
        Integer pageSize = getParaToInt("pageSize");
        renderJson(JsonResult.newJsonResult(service.getMenu(pageNumber,pageSize)));
    }

    public void getById() {
        renderJson(JsonResult.newJsonResult(service.getById(getPara("id"))));
    }
}
