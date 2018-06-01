/**
 * project name: hdy_project
 * file name:BasCustomerExtService
 * package name:com.ns.customer.service
 * date:2018-02-01 21:48
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.customer.service;

import com.ns.common.model.BasCustPointTrans;
import com.ns.common.model.BasCustomerExt;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

/**
 * description: //TODO <br>
 * date: 2018-02-01 21:48
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class BasCustomerExtService {
    public static BasCustomerExtService me = new BasCustomerExtService();
    private static final BasCustomerExt dao = new BasCustomerExt().dao();

    /**
     * 创建会员拓展信息
     *
     * @param conId
     * @param conNo
     * @param conName
     * @return
     */
    public boolean addBasCustomerExt(String conId, String conNo, String conName) {
        BasCustomerExt ext = new BasCustomerExt();
        ext.setID(GUIDUtil.getGUID());
        ext.setConId(conId);
        ext.setConNo(conNo);
        ext.setConName(conName);
        ext.setCreateDt(DateUtil.getNow());
        ext.setUpdateDt(DateUtil.getNow());
        return ext.save();
    }

    public boolean update(BasCustomerExt ext) {
        ext.setUpdateDt(DateUtil.getNow());
        return ext.update();
    }

    /**
     * 根据会员ID查询扩展数据
     *
     * @param conId
     * @return
     */
    public BasCustomerExt getByConId(String conId) {
        return dao.findFirst("select * from bas_customer_ext where con_id = ? and enabled = 1", conId);
    }

    public Page<BasCustomerExt> getList(String conNo, int pageNum, int pageSize) {
        StringBuffer sqlExceptSelect = new StringBuffer("from bas_customer_ext where 1 = 1");
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and con_no = '" + conNo + "'");
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        return dao.paginate(pageNum, pageSize, "select * ", sqlExceptSelect.toString());
    }

    public BasCustomerExt getById(String id) {
        return dao.findById(id);
    }
}
