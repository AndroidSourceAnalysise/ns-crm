/**
 * project name: ns-crm
 * file name:TldPhotosService
 * package name:com.ns.tld.service
 * date:2018-02-09 16:31
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.model.TldPhotos;
import com.ns.common.redis.RedisUtil;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.ArrayList;
import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-02-09 16:31
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldPhotosService {
    public static final TldPhotosService me = new TldPhotosService();
    private final TldPhotos dao = new TldPhotos();
    private final String COLUMN = "ID,RELATION_ID,SYS_ID,TYPE,URL,HREF_URL,DISPLAY_SEQ,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public boolean addPotos(List<JSONObject> photosList) {
        for (int i = 0; i < photosList.size(); i++) {

            TldPhotos tldPhotos = photosList.get(i).toJavaObject(TldPhotos.class);
            tldPhotos.setID(GUIDUtil.getGUID());
            tldPhotos.setUpdateDt(DateUtil.getNow());
            tldPhotos.setCreateDt(DateUtil.getNow());
            boolean result = tldPhotos.save();
            if (result) {
                if (StrKit.notBlank(tldPhotos.getRelationId())) {
                    Redis.use().del(RedisKeyDetail.PRODUCT_ID + tldPhotos.getRelationId());
                }
                Redis.use().del(RedisKeyDetail.PHOTOS_KEY + tldPhotos.getSysId() + "-" + tldPhotos.getTYPE());
            }
        }
        return true;
    }

    public boolean updatePotos(List<JSONObject> photosList) {
        for (int i = 0; i < photosList.size(); i++) {
            TldPhotos tldPhotos = photosList.get(i).toJavaObject(TldPhotos.class);
            tldPhotos.setUpdateDt(DateUtil.getNow());
            boolean result = tldPhotos.update();
            if (result) {
                if (StrKit.notBlank(tldPhotos.getRelationId())) {
                    Redis.use().del(RedisKeyDetail.PRODUCT_ID + tldPhotos.getRelationId());
                }
                Redis.use().del(RedisKeyDetail.PHOTOS_KEY + tldPhotos.getSysId() + "-" + tldPhotos.getTYPE());
            }
        }
        return true;
    }

    public boolean deletePotos(String id) {
        TldPhotos photos = dao.findById(id);
        if (photos != null) {
            boolean result = dao.deleteById(id);
            if (result) {
                if (StrKit.notBlank(photos.getRelationId())) {
                    Redis.use().del(RedisKeyDetail.PRODUCT_ID + photos.getRelationId());
                }
                Redis.use().del(RedisKeyDetail.PHOTOS_KEY + photos.getSysId() + "-" + photos.getTYPE());
            }
            return result;
        }
        return false;
    }

    public Page<TldPhotos> getPhotos(int sysId, int type, Integer pageNumber, Integer pageSize) {
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN, " from tld_photos where SYS_ID = ? and TYPE = ? order by DISPLAY_SEQ desc", sysId, type);
    }

    public List<TldPhotos> getByProduct(String pntId) {
        return dao.find("select " + COLUMN + " from tld_photos where  RELATION_ID = ? order by DISPLAY_SEQ desc", pntId);
    }
}
