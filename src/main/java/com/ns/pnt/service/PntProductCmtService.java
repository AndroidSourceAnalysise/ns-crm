/**
 * project name: ns-api
 * file name:PntProductCmtService
 * package name:com.ns.pnt.service
 * date:2018-02-08 16:26
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.pnt.service;

import com.alibaba.fastjson.JSONObject;
import com.ns.common.exception.CustException;
import com.ns.common.model.BasCustLike;
import com.ns.common.model.BasCustomer;
import com.ns.common.model.PntProductCmt;
import com.ns.common.model.TldPhotos;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.ns.customer.service.BasCustomerService;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;

import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-02-08 16:26
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class PntProductCmtService {
    public static final PntProductCmtService me = new PntProductCmtService();
    private PntProductCmt dao = new PntProductCmt();
    static BasCustomerService basCustomerService = BasCustomerService.me;
    private static final String COLUMN = "ID,PARENT_ID,SOURCE_ID,CON_ID,CON_NO,CON_NAME,PIC,PHOTO_URL1,PHOTO_URL2,PHOTO_URL3,CONTENT,LIKE_QTY,TO_CON_ID,TO_CON_NO,TO_CON_NAME,ENABLED,VERSION,STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT";

    /**
     * 插入主评论
     *
     * @param pntProductCmt
     * @return
     */
    public boolean inertCMT(PntProductCmt pntProductCmt) {
        pntProductCmt.setID(GUIDUtil.getGUID());
        pntProductCmt.setConId(pntProductCmt.getConId());
        pntProductCmt.setConName("系统回复");
        pntProductCmt.setConNo("0");
        pntProductCmt.setPIC("");
        //0是系统回复
        pntProductCmt.setUpdateDt(DateUtil.getNow());
        pntProductCmt.setCreateDt(DateUtil.getNow());
        return pntProductCmt.save();
    }

    public boolean updateProductCmt(List<JSONObject> photosList) {
        for (int i = 0; i < photosList.size(); i++) {
            PntProductCmt cmt = photosList.get(i).toJavaObject(PntProductCmt.class);
            cmt.setUpdateDt(DateUtil.getNow());
            cmt.update();
        }
        return true;
    }

    public boolean delete(String[] ids) {
        for (int i = 0; i < ids.length; i++) {
            dao.deleteById(ids[i]);
        }
        return true;
    }

    public Page<Record> getPntCmtList(int pageNumber, int pageSize, String pntId) {
        StringBuffer sqlExceptSelect = new StringBuffer(" from pnt_product_cmt where PARENT_ID = '0'");
        if (StrKit.notBlank(pntId)) {
            sqlExceptSelect.append(" and SOURCE_ID = '" + pntId + "'");
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        Page<Record> page = Db.paginate(pageNumber, pageSize, "select " + COLUMN + " ", sqlExceptSelect.toString());
        return page;
    }

    public Page<Record> getPntCmtChildren(int pageNumber, int pageSize, String id) {
        return Db.paginate(pageNumber, pageSize, "select " + COLUMN + " ", "from pnt_product_cmt where  SOURCE_ID = ? order by CREATE_DT desc", id);
    }
}
