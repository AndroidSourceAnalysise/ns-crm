/**
 * project name: ns-crm
 * file name:PntCategoriesService
 * package name:com.ns.pnt.service
 * date:2018-04-02 14:00
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.service;

import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.model.PntCategories;
import com.ns.common.model.PntMenu;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-04-02 14:00
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntCategoriesService {
    private final PntCategories dao = PntCategories.dao;
    public final static PntCategoriesService me = new PntCategoriesService();

    public boolean insert(PntCategories pntCategories) {
        pntCategories.setID(GUIDUtil.getGUID());
        pntCategories.setCreateDt(DateUtil.getNow());
        pntCategories.setUpdateDt(DateUtil.getNow());
        Redis.use().del(RedisKeyDetail.PNT_CATEGORIES);
        return pntCategories.save();
    }

    public boolean update(PntCategories pntCategories) {
        Redis.use().del(RedisKeyDetail.PNT_CATEGORIES);
        pntCategories.setUpdateDt(DateUtil.getNow());
        return pntCategories.update();
    }

    public boolean delete(String id) {
        Redis.use().del(RedisKeyDetail.PNT_CATEGORIES);
        return dao.deleteById(id);
    }
}
