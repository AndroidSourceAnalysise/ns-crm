/**
 * project name: ns-crm
 * file name:SysDictService
 * package name:com.ns.sys.service
 * date:2018-02-12 16:04
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.sys.service;

import com.ns.common.model.SysDict;
import com.ns.common.redis.RedisUtil;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.redis.Redis;

import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-02-12 16:04
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class SysDictService {
    public static final SysDictService me = new SysDictService();
    private final SysDict dao = SysDict.dao;
    private static final String COLUMN = "ID,GROUP_NAME,GROUP_CODE,PARAM_KEY,PARAM_VALUE,ENABLED,VERSION,STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public boolean addSysDict(SysDict sysDict) {
        sysDict.setID(GUIDUtil.getGUID());
        sysDict.setUpdateDt(DateUtil.getNow());
        sysDict.setCreateDt(DateUtil.getNow());
        return sysDict.save();
    }

    public boolean updateSysDict(SysDict sysDict) {
        String key = dao.findById(sysDict.getID()).getParamKey();
        sysDict.setUpdateDt(DateUtil.getNow());
        if (sysDict.update()) {
            Redis.use().del(key);
        }
        return true;
    }

    public boolean addSysDict(String groupName, String groupCode, String paramKey, String paramValue) {
        SysDict sysDict = new SysDict();
        sysDict.setID(GUIDUtil.getGUID());
        sysDict.setGroupName(groupName);
        sysDict.setGroupCode(groupCode);
        sysDict.setParamKey(paramKey);
        sysDict.setParamValue(paramValue);
        sysDict.setUpdateDt(DateUtil.getNow());
        sysDict.setCreateDt(DateUtil.getNow());
        return sysDict.save();
    }

    public Page<SysDict> getSysDict(int pageNumber, int pageSize, String remark) {
        StringBuffer sqlExceptSelect = new StringBuffer("from sys_dict where 1=1 ");
        if (StrKit.notBlank(remark)) {
            sqlExceptSelect.append(" and remark like '%" + remark + "%'");
        }
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN, sqlExceptSelect.toString());
    }

    public SysDict getByParamKey(String paramKey) {
        return dao.findFirst("select " + COLUMN + " from sys_dict where PARAM_KEY = ?", paramKey);
    }
}
