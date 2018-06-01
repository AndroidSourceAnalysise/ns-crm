/**
 * project name: ns-crm
 * file name:TldCouponService
 * package name:com.ns.tld.service
 * date:2018-03-06 10:11
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.service;

import com.ns.common.model.TldCoupon;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * description: //TODO <br>
 * date: 2018-03-06 10:11
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldCouponService {
    private final TldCoupon dao = new TldCoupon();
    public static final TldCouponService me = new TldCouponService();

    private final String COLUMN = "ID,NAME,DESCRIPTION,SAFETY_AMOUNT,DISCOUNT_AMOUNT,TOTAL_NUMBER,REMAIN_NUMBER,COUPON_TYPE,IMAGE_URL,START_DT,END_DT,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public boolean addTldCoupon(TldCoupon coupon) {
        coupon.setID(GUIDUtil.getGUID());
        coupon.setCreateDt(DateUtil.getNow());
        coupon.setUpdateDt(DateUtil.getNow());
        return coupon.save();
    }

    public boolean updateTldCoupon(TldCoupon coupon) {
        coupon.setUpdateDt(DateUtil.getNow());

        return coupon.update();
    }

    public boolean deleteTldCoupon(String id) {
        return dao.deleteById(id);
    }

    public TldCoupon getById(String id) {
        return dao.findFirst("select " + COLUMN + " from tld_coupon where id = ?", id);
    }

    public Page<TldCoupon> getTldCouponList(int pageNumber, int pageSize, String name, String startDt, String endDt, String couponType) {
        StringBuffer sqlExceptSelect = new StringBuffer(" from tld_coupon where 1=1");
        if (StrKit.notBlank(name)) {
            sqlExceptSelect.append(" and NAME like " + "'%" + name + "%'");
        }
        if (StrKit.notBlank(startDt)) {
            sqlExceptSelect.append(" and START_DT >= " + "'" + startDt + "'");
        }
        if (StrKit.notBlank(endDt)) {
            sqlExceptSelect.append(" and END_DT <= " + "'" + endDt + "'");
        }
        if (StrKit.notBlank(couponType)) {
            sqlExceptSelect.append(" and COUPON_TYPE = " + couponType);
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN + " ", sqlExceptSelect.toString());
    }
}
