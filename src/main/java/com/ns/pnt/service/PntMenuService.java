/**
 * project name: ns-api
 * file name:PntMenuService
 * package name:com.ns.pnt.service
 * date:2018-04-03 10:37
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.service;

import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.model.PntMenu;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-04-03 10:37
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntMenuService {
    public static final PntMenuService me = new PntMenuService();
    private PntMenu dao = new PntMenu();
    private static final String COLUMN = "ID,MENU_NAME,MENU_DESC,MENU_TYPE,MENU_VALUE,ICON_URL,DISPLAY_SEQ,ENABLED,VERSION,STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT";

    public Page<PntMenu> getMenu(Integer pageNumber, Integer pageSize) {
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN, " from pnt_menu order by DISPLAY_SEQ");
    }

    public boolean insert(PntMenu pntMenu) {
        pntMenu.setID(GUIDUtil.getGUID());
        pntMenu.setCreateDt(DateUtil.getNow());
        pntMenu.setUpdateDt(DateUtil.getNow());
        Redis.use().del(RedisKeyDetail.PNT_MENU);
        return pntMenu.save();
    }

    public boolean update(PntMenu pntMenu) {
        pntMenu.setUpdateDt(DateUtil.getNow());
        Redis.use().del(RedisKeyDetail.PNT_MENU);
        return pntMenu.update();
    }

    public boolean deleteById(String id) {
        Redis.use().del(RedisKeyDetail.PNT_MENU);
        return dao.deleteById(id);
    }

    public PntMenu getById(String id) {
        return dao.findFirst("select " + COLUMN + " from pnt_menu where id = ?", id);
    }
}
