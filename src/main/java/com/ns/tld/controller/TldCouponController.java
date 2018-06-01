/**
 * project name: ns-crm
 * file name:TldCouponController
 * package name:com.ns.tld.controller
 * date:2018-03-06 13:52
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.controller;

import com.ns.common.base.BaseController;
import com.ns.common.exception.CustException;
import com.ns.common.json.JsonResult;
import com.ns.common.model.TldCoupon;
import com.ns.common.model.TldCouponGrant;
import com.ns.common.utils.Util;
import com.ns.tld.service.TldCouponGrantService;
import com.ns.tld.service.TldCouponService;
import com.ns.tld.service.TldPhotosService;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Record;

/**
 * description: //TODO <br>
 * date: 2018-03-06 13:52
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldCouponController extends BaseController {
    static TldCouponGrantService tldCouponGrantService = TldCouponGrantService.me;
    static TldCouponService tldCouponService = TldCouponService.me;
    static TldPhotosService tldPhotosService = TldPhotosService.me;

    //{"NAME":"优惠券1","DESCRIPTION":"描述","SAFETY_AMOUNT":"188.88(满足多少金额可使用该优惠券)","DISCOUNT_AMOUNT":"5(可抵扣金额)","TOTAL_NUMBER":"500(总数量)","REMAIN_NUMBER":"50(剩余数量)","COUPON_TYPE":"0-直减 1-满减","IMAGE_URL":"优惠券图片","START_DT":"2018-01-01","END_DT":"2019-01-01","STATUS":"0 未启用, 1 已启用"}
    public void addTldCoupon() {
        TldCoupon coupon = Util.getRequestObject(getRequest(), TldCoupon.class);
        renderJson(JsonResult.newJsonResult(tldCouponService.addTldCoupon(coupon)));
    }

    //{"ID":"1","NAME":"优惠券1","DESCRIPTION":"描述","SAFETY_AMOUNT":"188.88(满足多少金额可使用该优惠券)","DISCOUNT_AMOUNT":"5(可抵扣金额)","TOTAL_NUMBER":"500(总数量)","REMAIN_NUMBER":"50(剩余数量)","COUPON_TYPE":"0-直减 1-满减","IMAGE_URL":"优惠券图片","START_DT":"2018-01-01","END_DT":"2019-01-01","STATUS":"0 未启用, 1 已启用"}
    public void updateTldCoupon() {
        TldCoupon coupon = Util.getRequestObject(getRequest(), TldCoupon.class);
        //如果优惠券设置为失效或者是不启用
        if (coupon.getENABLED() != null) {
            if (coupon.getENABLED() == 0 || (coupon.getSTATUS() != null && coupon.getENABLED() == 0)) {
                //判断优惠券是否发放。如果发放。则删除
                Record record = Db.findFirst("select ID from tld_photos where ENABLED = 1 and RELATION_ID = ? ", getPara("id"));
                if (record != null) {
                    tldPhotosService.deletePotos(record.getStr("ID"));
                }
            }
        }
        renderJson(JsonResult.newJsonResult(tldCouponService.updateTldCoupon(coupon)));
    }

    public void deleteTldCoupon() {
        //判断优惠券是否发放。如果发放。则删除
        Record record = Db.findFirst("select ID from tld_photos where ENABLED = 1 and RELATION_ID = ? ", getPara("id"));
        if (record != null) {
            tldPhotosService.deletePotos(record.getStr("ID"));
        }
        renderJson(JsonResult.newJsonResult(tldCouponService.deleteTldCoupon(getPara("id"))));
    }

    public void isGrantCoupon() {
        String id = getPara("id");
        Record record = Db.findFirst("select ID from tld_photos where ENABLED = 1 and RELATION_ID = ? ", id);
        if (record != null) {
            throw new CustException("该优惠券已发放!");
        }
        renderJson(JsonResult.newJsonResult(true));
    }

    public void getTldCouponById() {
        renderJson(JsonResult.newJsonResult(tldCouponService.getById(getPara("id"))));
    }

    public void getTldCouponList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String name = getPara("name");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        String couponType = getPara("couponType");
        renderJson(JsonResult.newJsonResult(tldCouponService.getTldCouponList(pageNumber, pageSize, name, startDt, endDt, couponType)));
    }

    public void getTldCouponGrantList() {
        int pageNumber = getParaToInt("pageNumber", 1);
        int pageSize = getParaToInt("pageSize", 10);
        String conNo = getPara("conNo");
        String name = getPara("name");
        String startDt = getPara("startDt");
        String endDt = getPara("endDt");
        String couponType = getPara("couponType");
        String status = getPara("status");
        renderJson(JsonResult.newJsonResult(tldCouponGrantService.getTldCouponGrantList(pageNumber, pageSize, conNo, name, startDt, endDt, couponType, status)));
    }

    public void getTldCouponGrantById() {
        renderJson(JsonResult.newJsonResult(tldCouponGrantService.getById(getPara("id"))));
    }

    public void deleteTldCouponGrantById() {
        renderJson(JsonResult.newJsonResult(tldCouponGrantService.deleteById(getPara("id"))));
    }

    ////{"ID":"1","STATUS":"优惠券状态【0 未使用 1 已使用 2 已过期】","ENABLED":"0,失效，1有效"}
    public void updateTldCouponGrant() {
        TldCouponGrant tldCouponGrant = Util.getRequestObject(getRequest(), TldCouponGrant.class);
        renderJson(JsonResult.newJsonResult(tldCouponGrantService.updateTldCouponGrant(tldCouponGrant)));
    }
}
