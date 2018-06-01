/**
 * project name: ns-crm
 * file name:PntSkuService
 * package name:com.ns.pnt.service
 * date:2018-02-11 10:39
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.service;

import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.model.PntSku;
import com.ns.common.redis.RedisUtil;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;
import redis.clients.jedis.Jedis;

import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-02-11 10:39
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntSkuService {
    public static final PntSkuService me = new PntSkuService();
    private final PntSku dao = PntSku.dao;
    private final String COLUMN = "ID,PRODUCT_ID,SKU,SAL_PRICE,STOCK,IMAGE_URL,THUMB_URL,BRIEF_DESCRIPTION,SEL_DEFAULT,INTEGRAL_TAG,INTEGRAL_SELF,INTEGRAL_SUP,WEIGHT,UNIT,DISPLAY_SEQ,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public boolean addPntSku(PntSku sku) {
        sku.setID(GUIDUtil.getGUID());
        sku.setUpdateDt(DateUtil.getNow());
        sku.setCreateDt(DateUtil.getNow());
        boolean result = sku.save();
        if (result) {
            Cache cache = Redis.use();
            cache.del(RedisKeyDetail.PRODUCT_ID + sku.getProductId());
            setStock(sku.getID(), sku.getSTOCK());
        }
        return result;
    }

    public boolean updatePntSku(PntSku sku) {
        sku.setUpdateDt(DateUtil.getNow());
        boolean result = sku.update();
        if (result) {
            Cache cache = Redis.use();
            cache.del(RedisKeyDetail.PRODUCT_ID + sku.getProductId());
            setStock(sku.getID(), sku.getSTOCK());
        }
        return result;
    }

    private void setStock(String id, int stock) {
        Jedis jedis = Redis.use().getJedis();
        try {
            jedis.set(RedisKeyDetail.SKU_STOCK_ID + id, String.valueOf(stock));
        } finally {
            jedis.close();
        }
    }

    public boolean deletePntSku(String id) {
        PntSku sku = dao.findById(id);
        if (sku != null) {
            boolean result = dao.deleteById(id);
            if (result) {
                Cache cache = Redis.use();
                cache.del(RedisKeyDetail.PRODUCT_ID + sku.getProductId());
                cache.del(RedisKeyDetail.SKU_STOCK_ID + id);
            }
            return result;
        }
        return true;
    }

    public List<PntSku> getByProduct(String pntId) {
        List<PntSku> skuList = dao.find("select " + COLUMN + " from pnt_sku where  PRODUCT_ID = ? ", pntId);
        for (PntSku sku : skuList) {
            Long stock = Redis.use().getCounter(RedisKeyDetail.SKU_STOCK_ID + sku.getID());
            sku.setSTOCK(stock.intValue());
        }
        return skuList;
    }
}
