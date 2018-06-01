/**
 * project name: ns-crm
 * file name:TldCouponGrantService
 * package name:com.ns.tld.service
 * date:2018-03-06 10:55
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.service;

import com.ns.common.model.TldCouponGrant;
import com.ns.common.utils.DateUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Page;

/**
 * description: //TODO <br>
 * date: 2018-03-06 10:55
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldCouponGrantService {
    private final TldCouponGrant dao = new TldCouponGrant();
    public static final TldCouponGrantService me = new TldCouponGrantService();
    private final String COLUMN = "ID,COUPON_ID,COUPON_NAME,CON_ID,CON_NO,CON_NAME,DESCRIPTION,SAFETY_AMOUNT,DISCOUNT_AMOUNT,COUPON_TYPE,IMAGE_URL,START_DT,END_DT,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public boolean updateTldCouponGrant(TldCouponGrant couponGrant) {
        couponGrant.setUpdateDt(DateUtil.getNow());
        return couponGrant.update();
    }

    public TldCouponGrant getById(String id) {
        return dao.findFirst("select " + COLUMN + " from tld_coupon_grant where id = ?", id);
    }

    public boolean deleteById(String id) {
        return dao.deleteById(id);
    }

    public Page<TldCouponGrant> getTldCouponGrantList(int pageNumber, int pageSize, String conNo, String name, String startDt, String endDt, String couponType, String status) {
        StringBuffer sqlExceptSelect = new StringBuffer(" from tld_coupon_grant where 1=1");
        if (StrKit.notBlank(name)) {
            sqlExceptSelect.append(" and COUPON_NAME like " + "'%" + name + "%'");
        }
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and CON_NO = " + "'" + conNo + "'");
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
        if (StrKit.notBlank(status)) {
            sqlExceptSelect.append(" and status = " + status);
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN + " ", sqlExceptSelect.toString());
    }
}
