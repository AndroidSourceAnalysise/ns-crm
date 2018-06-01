/**
 * project name: ns-crm
 * file name:PntProductController
 * package name:com.ns.pnt.controller
 * date:2018-02-08 15:09
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.PntCategories;
import com.ns.common.model.PntProduct;
import com.ns.common.model.PntSku;
import com.ns.common.redis.RedisUtil;
import com.ns.common.utils.Util;
import com.ns.pnt.service.PntCategoriesService;
import com.ns.pnt.service.PntProductService;
import com.ns.pnt.service.PntSkuService;
import com.jfinal.aop.Before;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.redis.Redis;

import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-02-08 15:09
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntProductController extends BaseController {
    static PntProductService pntProductService = PntProductService.me;
    static PntSkuService pntSkuService = PntSkuService.me;
    static PntCategoriesService pntCategoriesService = PntCategoriesService.me;

    //{"PRODUCT_NAME":"商品1","PY_CODE":"拼音码","PRODUCT_CODE":"商品编码","ABB_NAME":"简称","SAL_PRICE":"99","TITLE":"商品标题","IMAGE_URL":"产品主图","THUMB_URL":"缩略图","BRIEF_DESCRIPTION":"商品简介","DISPLAY_SEQ":"1","STATUS":"1"}
    @Before(Tx.class)
    public void addProduct() {
        PntProduct pntProduct = Util.getRequestObject(getRequest(), PntProduct.class);
        renderJson(JsonResult.newJsonResult(pntProductService.addProduct(pntProduct)));
    }

    @Before(Tx.class)
    public void updateProduct() {
        PntProduct pntProduct = Util.getRequestObject(getRequest(), PntProduct.class);
        renderJson(JsonResult.newJsonResult(pntProductService.updateProduct(pntProduct)));
    }

    @Before(Tx.class)
    public void deleteProduct() {
        String pntId = getPara("pntId");
        renderJson(JsonResult.newJsonResult(pntProductService.deleteProduct(pntId)));
    }

    public void getProductList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String status = getPara("status");
        String productCode = getPara("productCode");
        String productName = getPara("productName");
        String pyCode = getPara("pyCode");
        renderJson(JsonResult.newJsonResult(pntProductService.getProductList(pageNumber, pageSize, status, productCode, productName, pyCode)));
    }

    public void getProductDetial() {
        String pntId = getPara("pntId");
        renderJson(JsonResult.newJsonResult(pntProductService.getProductDetial(pntId)));
    }

    public void test() {
        Object object = Redis.use().get("rvsf");
        System.out.println(object);
    }

    public void addProductImages() {
        Map<String, String> map = Util.getRequestObject(getRequest(), Map.class);
        String pntId = map.get("pntId");
        JSONArray array = JSON.parseArray(map.get("images"));
    }

    //{"PRODUCT_ID":"产品id","SKU":"SKU名称","SAL_PRICE":"销售价格","STOCK":"库存","IMAGE_URL":"产品图","THUMB_URL":"缩略图","BRIEF_DESCRIPTION":"sku简介","SEL_DEFAULT":"0：不选中，1：默认选中","INTEGRAL_TAG":"积分兑换开启标记，0，不开启，默认；1，开启","INTEGRAL_SELF":"返自身积分","INTEGRAL_SUP":"返上级积分","WEIGHT":"重量","UNIT":"单位","DISPLAY_SEQ":"排序"}
    @Before(Tx.class)
    public void addPntSku() {
        PntSku sku = Util.getRequestObject(getRequest(), PntSku.class);
        renderJson(JsonResult.newJsonResult(pntSkuService.addPntSku(sku)));
    }

    @Before(Tx.class)
    public void updatePntSku() {
        PntSku sku = Util.getRequestObject(getRequest(), PntSku.class);
        renderJson(JsonResult.newJsonResult(pntSkuService.updatePntSku(sku)));
    }

    @Before(Tx.class)
    public void deletePntSku() {
        String skuId = getPara("skuId");
        renderJson(JsonResult.newJsonResult(pntSkuService.deletePntSku(skuId)));
    }

    public void insertCategories() {
        PntCategories pntCategories = Util.getRequestObject(getRequest(), PntCategories.class);
        renderJson(JsonResult.newJsonResult(pntCategoriesService.insert(pntCategories)));
    }

    public void updateCategories() {
        PntCategories pntCategories = Util.getRequestObject(getRequest(), PntCategories.class);
        renderJson(JsonResult.newJsonResult(pntCategoriesService.update(pntCategories)));
    }

    public void deleteCategories() {
        renderJson(JsonResult.newJsonResult(pntCategoriesService.delete(getPara("id"))));
    }

}
