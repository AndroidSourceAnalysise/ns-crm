/**
 * project name: ns-crm
 * file name:PntProductService
 * package name:com.ns.pnt.service
 * date:2018-02-08 15:11
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.service;

import com.alibaba.fastjson.JSONArray;
import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.model.PntProduct;
import com.ns.common.model.TldPhotos;
import com.ns.common.redis.RedisUtil;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.ns.tld.service.TldPhotosService;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;

import java.util.HashMap;
import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-02-08 15:11
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntProductService {
    public static final PntProductService me = new PntProductService();
    private final PntProduct dao = new PntProduct();
    private static final String COLUMN = "ID,PRODUCT_NAME,PY_CODE,PRODUCT_CODE,SAL_PRICE,ABB_NAME,TITLE,IMAGE_URL,THUMB_URL,BRIEF_DESCRIPTION,INTEGRAL_TAG,INTEGRAL_SELF,INTEGRAL_SUP,DISPLAY_SEQ,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";
    static TldPhotosService tldPhotosService = TldPhotosService.me;
    static PntSkuService pntSkuService = PntSkuService.me;

    public String addProduct(PntProduct pntProduct) {
        String pntId = GUIDUtil.getGUID();
        pntProduct.setID(pntId);
        pntProduct.setCreateDt(DateUtil.getNow());
        pntProduct.setUpdateDt(DateUtil.getNow());
        boolean result = pntProduct.save();
        if (result) {
            Redis.use().del(RedisKeyDetail.PRODUCT_ALL);
        }
        return pntId;
    }

    public boolean updateProduct(PntProduct pntProduct) {
        pntProduct.setUpdateDt(DateUtil.getNow());
        boolean result = pntProduct.update();
        if (result) {
            Redis.use().del(RedisKeyDetail.PRODUCT_ID + pntProduct.getID());
            Redis.use().del(RedisKeyDetail.PRODUCT_ALL);
        }
        return result;
    }

    public boolean deleteProduct(String id) {
        if (dao.deleteById(id)) {
            Redis.use().del(RedisKeyDetail.PRODUCT_ALL);
            Redis.use().del(RedisKeyDetail.PRODUCT_ID + id);
            return true;
        }
        return false;
    }

    public Page<PntProduct> getProductList(int pageNumber, int pageSize, String status, String productCode, String productName, String pyCode) {
        StringBuffer sqlExceptSelect = new StringBuffer(" from pnt_product where 1=1");
        if (StrKit.notBlank(productCode)) {
            sqlExceptSelect.append(" and PRODUCT_CODE like " + "'%" + productCode + "%'");
        }
        if (StrKit.notBlank(productName)) {
            sqlExceptSelect.append(" and PRODUCT_NAME like " + "'%" + productName + "%'");
        }
        if (StrKit.notBlank(pyCode)) {
            sqlExceptSelect.append(" and PY_CODE like " + "'%" + pyCode + "%'");
        }
        if (StrKit.notBlank(status)) {
            sqlExceptSelect.append(" and STATUS = " + status);
        }
        sqlExceptSelect.append(" and enabled = 1 order by CREATE_DT desc");
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN + " ", sqlExceptSelect.toString());
    }


    public Object getProductDetial(String id) {
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("product", getById(id));
        resultMap.put("photos", tldPhotosService.getByProduct(id));
        resultMap.put("skus", pntSkuService.getByProduct(id));
        return resultMap;
    }

    public PntProduct getById(String id) {
        return dao.findFirst("select " + COLUMN + " from pnt_product where enabled = 1 and id = ?", id);
    }

    public void addProductImages(String pntId, JSONArray images) {
        TldPhotos tldPhotos = new TldPhotos();
    }
}
