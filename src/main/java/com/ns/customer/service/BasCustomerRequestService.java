/**
 * project name: ns-api
 * file name:BasCustomerRequestService
 * package name:com.ns.customer.service
 * date:2018-03-20 15:19
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.customer.service;

import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.exception.CustException;
import com.ns.common.model.BasCustPointTrans;
import com.ns.common.model.BasCustomerRequest;
import com.ns.common.model.TldOrders;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.ns.tld.service.TldOrdersService;
import com.ns.weixin.service.NoticeService;
import com.ns.weixin.service.WeixinPayService;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.util.List;

/**
 * description: //TODO <br>
 * date: 2018-03-20 15:19
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class BasCustomerRequestService {
    public static BasCustomerRequestService me = new BasCustomerRequestService();
    private static final BasCustomerRequest dao = new BasCustomerRequest().dao();
    private static final String COLUMN = "ID,CON_ID,CON_NO,CON_NAME,ORDER_ID,ORDER_NO,AMOUNT,REQUEST_TYPE,REQUEST_STATUS,REFUND_TYPE," +
            "CUST_SERVICE_CHECKTIME,CUST_SERVICE_CHECKRESULT," +
            "CUST_SERVICE_CHECK_REMARK,FIN_CHECK_DT,FIN_CHECK_RESULT,FIN_CHECK_REMARK,RETURN_REASON,PNT_PHOTO_URL1,PNT_PHOTO_URL2," +
            "PNT_PHOTO_URL3,EXP_COMPANY_ID,EXP_WAYBILL,IS_RETURN,REFUND_STATUS,REFUND_TRANSFER,ENABLED,VERSION,STATUS,REMARK," +
            "CREATE_BY,CREATE_DT,UPDATE_DT ";
    static TldOrdersService ordersService = TldOrdersService.me;
    static BasCustPointsService pointsService = BasCustPointsService.me;
    static NoticeService noticeService = NoticeService.me;

    public BasCustomerRequest getById(String id) {
        return dao.findFirst("select " + COLUMN + " from bas_customer_request where id = ?", id);
    }

    public BasCustomerRequest getByOrderId(String id) {
        return dao.findFirst("select " + COLUMN + " from bas_customer_request where ENABLED = 1 and ORDER_ID = ?", id);
    }

    public boolean custServiceCheck(String id, BigDecimal amount, Integer checkResult, String checkRemark) {
        String dateTime = DateUtil.getNow();
        BasCustomerRequest request = getById(id);
        if (request.getRequestStatus() != 0) {
            throw new CustException("不是申请状态");
        }
        if (request.getCustServiceCheckresult() != 0) {
            throw new CustException("客服已审核!");
        }
        //客服审核
        request.setCustServiceCheckresult(checkResult);
        request.setCustServiceCheckRemark(checkRemark);
        request.setCustServiceChecktime(dateTime);
        request.setAMOUNT(amount);
        if (checkResult == 2) {
            request.setRequestStatus(2);
        }
        Db.update("update tld_orders set STATUS = 6,VERSION = VERSION +1,UPDATE_DT = ?,REMARK = ? where id = ?", DateUtil.getNow(), checkRemark, request.getOrderId());
        Db.update("update tld_order_split set STATUS = 6,VERSION = VERSION +1,UPDATE_DT = ?,REMARK = ? where order_id = ?", DateUtil.getNow(), checkRemark, request.getOrderId());

        Integer isReorder = Db.queryInt("select is_reorder from tld_orders where id = ?", request.getOrderId());
        if (isReorder == 0) {
            Db.update("update bas_customer set con_type = 0 where id = ?", request.getConId());
        }
        return request.update();
    }

    public boolean finCheck(String id, BigDecimal amount, Integer checkResult, String checkRemark) {
        String dateTime = DateUtil.getNow();
        BasCustomerRequest request = getById(id);
        if (request.getRequestStatus() != 0) {
            throw new CustException("不是申请中状态");
        }
        if (request.getCustServiceCheckresult() != 1) {
            throw new CustException("客服未审核或已拒绝!");
        }
        if (request.getFinCheckResult() != 0) {
            throw new CustException("财务已审核!");
        }
        request.setFinCheckRemark(checkRemark);
        request.setFinCheckResult(checkResult);
        request.setFinCheckDt(dateTime);
        request.setRequestStatus(checkResult);

        if (checkResult == 1) {
            if (updatePass(request.getOrderId(), amount)) {
                return request.update();
            }
        } else {
            Db.update("update tld_orders set STATUS = 6,VERSION = VERSION +1,UPDATE_DT = ?,REMARK = ? where id = ?", DateUtil.getNow(), checkRemark, request.getOrderId());
            Db.update("update tld_order_split set STATUS = 6,VERSION = VERSION +1,UPDATE_DT = ?,REMARK = ? where order_id = ?", DateUtil.getNow(), checkRemark, request.getOrderId());

            return request.update();
        }
        return false;
    }

    public boolean updatePass(String orderId, BigDecimal amount) {
        TldOrders orders = ordersService.getOrderById(orderId);
        String couponGrantId = orders.getCouponGrantId();
        String conId = orders.getConId();
        if (StrKit.notBlank(couponGrantId)) {
            Db.update("update tld_coupon_grant set STATUS = 0 where id = ?", couponGrantId);
        }
        //查询该订单是否使用积分抵扣
        Record pointGrans = Db.findFirst("select ID,POINTS_QTY from bas_cust_point_trans where POINTS_TYPE = 3 and FROM_ORDER_ID = ?", orderId);
        if (pointGrans != null) {
            int point = pointGrans.getInt("POINTS_QTY");
            Db.update("update bas_customer_ext set POINTS_ENABLED=POINTS_ENABLED+? where con_id = ?", point, conId);
            Db.delete("delete FROM  bas_cust_point_trans where id = ?", pointGrans.getStr("ID"));
        }

        List<BasCustPointTrans> list = pointsService.getByOrderIdAndType(orderId);
        int selfIntegral = 0;
        int up1Integral = 0;
        for (BasCustPointTrans pointTrans : list) {
            if (pointTrans.getPointsType() == 1 || pointTrans.getPointsType() == 2) {
                //自己获得的积分
                if (pointTrans.getConId().equals(orders.getConId())) {
                    selfIntegral += pointTrans.getPointsQty();
                }
                //上级获得的积分
                if (pointTrans.getConId().equals(orders.getRpId())) {
                    up1Integral += pointTrans.getPointsQty();
                }
                insertPointTrans(pointTrans.getConId(), pointTrans.getConNo(), pointTrans.getConName(), pointTrans.getFromConId(), pointTrans.getFromConNo(), pointTrans.getFromConName(), orderId, orders.getOrderNo(), -pointTrans.getPointsQty(), pointTrans.getPointsType(), "订单退款,关注推荐积分红冲");

            }
        }

        if (selfIntegral > 0) {
            //-总积分和未确认积分
            Db.update("update bas_customer_ext set POINTS_TOTAL=POINTS_TOTAL-?,POINTS_UNCFMD=POINTS_UNCFMD-? where con_id = ?", selfIntegral, selfIntegral, orders.getConId());
        }
        if (up1Integral > 0) {
            //-总积分和未确认积分
            Db.update("update bas_customer_ext set POINTS_TOTAL=POINTS_TOTAL-?,POINTS_UNCFMD=POINTS_UNCFMD-? where con_id = ?", up1Integral, up1Integral, orders.getRpId());
        }

        String remark = "申请退货成功";
        Db.update("update tld_orders set STATUS = 9,VERSION = VERSION +1,UPDATE_DT = ?,REMARK = ? where id = ?", DateUtil.getNow(), remark, orderId);
        Db.update("update tld_order_split set STATUS = 9,VERSION = VERSION +1,UPDATE_DT = ?,REMARK = ? where order_id = ?", DateUtil.getNow(), remark, orderId);

        String orderTotal = String.valueOf(amount.multiply(new BigDecimal(100)).intValue());
        if (orders.getPaymentTypeid().equals("0")) {
            WeixinPayService.refund(orderId, orderTotal, orderTotal);
        }
        //还原库存
        List<Record> itemsList = Db.find("select SKU_ID,QUANTITY from tld_order_items where order_id = ?", orderId);
        Jedis cache = Redis.use().getJedis();
        try {
            for (Record items : itemsList) {
                cache.incrBy(RedisKeyDetail.SKU_STOCK_ID + items.getStr("SKU_ID"), Long.valueOf(items.getInt("QUANTITY")));
            }
        } finally {
            cache.close();
        }
        String opneId = Db.queryStr("select OPENID from bas_customer where ID = ?", orders.getConId());
        noticeService.getRefundNotice(orders.getConId(),opneId, orders.getOrderNo());
        return true;
    }

    private void insertPointTrans(String conId, String conNo, String conName, String fromConId, String fromConNo, String fromConName, String orderId, String orderNo, Integer pointsQty, Integer pointsType, String remark) {
        BasCustPointTrans trans = new BasCustPointTrans();
        trans.setID(GUIDUtil.getGUID());
        trans.setConId(conId);
        trans.setConNo(conNo);
        trans.setConName(conName);
        trans.setFromConId(fromConId);
        trans.setFromConNo(fromConNo);
        trans.setFromConName(fromConName);
        trans.setFromOrderId(orderId);
        trans.setFromOrderNo(orderNo);
        trans.setPointsQty(pointsQty);
        trans.setPointsType(pointsType);
        trans.setREMARK(remark);
        trans.setCreateDt(DateUtil.getNow());
        trans.setUpdateDt(DateUtil.getNow());

        trans.save();
    }

    public boolean updateRequest(BasCustomerRequest request) {
        request.setUpdateDt(DateUtil.getNow());
        return request.update();
    }

    public Page<BasCustomerRequest> getRequestList(int pageNumber, int pageSize, String conNo, String orderNo, String startDt, String endDt, Integer requestStatus) {
        StringBuffer sqlExceptSelect = new StringBuffer("from bas_customer_request where 1=1");
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and CON_NO = '" + conNo + "'");
        }
        if (StrKit.notBlank(orderNo)) {
            sqlExceptSelect.append(" and ORDER_NO = '" + orderNo + "'");
        }
        if (requestStatus != null) {
            sqlExceptSelect.append(" and REQUEST_STATUS = " + requestStatus);
        }
        if (StrKit.notBlank(startDt)) {
            sqlExceptSelect.append(" and CREATE_DT >= " + "'" + startDt + "'");
        }
        if (StrKit.notBlank(endDt)) {
            sqlExceptSelect.append(" and CREATE_DT <= " + "'" + endDt + "'");
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN + "", sqlExceptSelect.toString());
    }

}
