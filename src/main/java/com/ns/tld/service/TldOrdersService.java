/**
 * project name: ns-api
 * file name:TldOrdersService
 * package name:com.ns.tld.service
 * date:2018-03-10 14:55
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.service;

import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.exception.CustException;
import com.ns.common.model.*;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.ns.customer.service.BasCustomerExtService;
import com.ns.customer.service.BasCustomerService;
import com.ns.pnt.service.PntProductService;
import com.ns.pnt.service.PntSkuService;
import com.ns.sys.service.SysDictService;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;
import com.sun.org.apache.regexp.internal.RE;
import redis.clients.jedis.Jedis;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * description: //TODO <br>
 * date: 2018-03-10 14:55
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TldOrdersService {
    private final TldOrders dao = new TldOrders();
    public static final TldOrdersService me = new TldOrdersService();

    static TldCouponGrantService couponService = TldCouponGrantService.me;
    static BasCustomerExtService extService = BasCustomerExtService.me;
    private final String COLUMN = "ID,ORDER_NO,CON_ID,CON_NO,CON_NAME,PIC,PAY_DT,SHIPPING_DT,CONFIRM_DT,COUNTRY,PROVINCE,CITY,DISTRICT,ADDRESS,POSTAL_CODE,MOBILE,RECIPIENTS,FREIGHT,WEIGHT,PAYMENT_TYPEID,PAYMENT_TYPE,ORDER_SOURCE,ORDER_TYPE,ORDER_TOTAL,COUPON_AMOUNT,INTEGRAL_AMOUNT,PNT_AMOUNT,IS_DISCOUNT,IS_REORDER,SELF_INTEGRAL,UP1_INTEGRAL,RP_ID,RP_NO,RP_NAME,ENABLED,VERSION," +
            "STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    //{"CON_ID":"1","COUNTRY":"中国","PROVINCE":"湖南省","CITY":"长沙市","DISTRICT":"岳麓区","ADDRESS":"雷锋大道","POSTAL_CODE":"412000","MOBILE":"13874133322","RECIPIENTS":"张三","FREIGHT":"0","PAYMENT_TYPEID":"0","PAYMENT_TYPE":"微信支付","ORDER_SOURCE":"1","ORDER_TYPE":"1"}

    private TldOrderItems setItems(BasCustomer customer, String orderId, String orderNo, PntSku sku, PntProduct pntProduct, int quantity, BigDecimal amt) {
        TldOrderItems orderItems = new TldOrderItems();
        orderItems.setID(GUIDUtil.getGUID());
        orderItems.setConId(customer.getID());
        orderItems.setConNo(customer.getConNo());
        orderItems.setConName(customer.getConName());
        orderItems.setCreateDt(DateUtil.getNow());
        orderItems.setUpdateDt(DateUtil.getNow());
        orderItems.setOrderId(orderId);
        orderItems.setOrderNo(orderNo);
        orderItems.setPntId(sku.getProductId());
        orderItems.setPntName(pntProduct.getProductName());
        orderItems.setSkuId(sku.getID());
        orderItems.setSkuName(sku.getSKU());
        orderItems.setSalePrice(sku.getSalPrice());
        orderItems.setQUANTITY(quantity);
        orderItems.setAMOUNT(amt);
        return orderItems;
    }

    private TldOrders setOrders(TldOrders orders, String orderId, String orderNo, BasCustomer customer, int integralSelf, int integralSup, BigDecimal pntAmountSum, BigDecimal couponAmount, BigDecimal pointAmount) {
        orders.setID(orderId);
        orders.setOrderNo(orderNo);
        orders.setConNo(customer.getConNo());
        orders.setConName(customer.getConName());
        orders.setPIC(customer.getPIC());
        orders.setRpId(customer.getRpId());
        orders.setRpNo(customer.getRpNo());
        orders.setRpName(customer.getRpName());
        orders.setIsReorder(customer.getConType());
        orders.setCreateDt(DateUtil.getNow());
        orders.setUpdateDt(DateUtil.getNow());
        orders.setSTATUS(1);
        orders.setSelfIntegral(integralSelf);
        orders.setUp1Integral(integralSup);
        orders.setOrderTotal(pntAmountSum.subtract(couponAmount).subtract(pointAmount));
        orders.setPntAmount(pntAmountSum);
        return orders;
    }

    public void subtractStock(String[] items) {
        Jedis cache = Redis.use().getJedis();
        try {
            for (String item : items) {
                String[] str = item.split("&");
                String skuId = str[1];
                int quantity = Integer.valueOf(str[2]);
                long rs = cache.decrBy(RedisKeyDetail.SKU_STOCK_ID + skuId, Long.valueOf(quantity));
                if (rs < 0) {
                    cache.incrBy(RedisKeyDetail.SKU_STOCK_ID + skuId, Long.valueOf(quantity));
                    throw new CustException("库存不足！");
                }
            }
        } finally {
            cache.close();
        }
    }

    public BigDecimal computeCouponAmount(String couponId, BigDecimal amount) {
        BigDecimal couponAmount = BigDecimal.ZERO;
        TldCouponGrant coupon = couponService.getById(couponId);
        if (coupon != null && coupon.getSTATUS() == 0) {
            String dateTime = DateUtil.getNow(DateUtil.DEFAULT_DATE_TIME_RFGFX);
            if (DateUtil.isTween(dateTime, coupon.getStartDt(), coupon.getEndDt(), DateUtil.DEFAULT_DATE_TIME_RFGFX)) {
                //直减
                if (coupon.getCouponType() == 1 && amount.compareTo(coupon.getSafetyAmount()) == -1) {
                    throw new CustException("该优惠券异常!");
                }
                couponAmount = coupon.getDiscountAmount();
            } else {
                throw new CustException("优惠券已过期!");
            }
        } else {
            throw new CustException("优惠券不存在!");
        }
        //设置优惠券为已使用
        coupon.setSTATUS(1);
        coupon.setUpdateDt(DateUtil.getNow());
        coupon.update();
        return couponAmount;
    }

    public double computeFreight(String province, int num) {
        double expFee = 0.00;
        if ("新疆".equals(province) || "西藏".equals(province)) {
            expFee = 15 * num;

        } else if ("内蒙古".equals(province) || "海南".equals(province)) {
            expFee = 10 * num;
        }
        return expFee;
    }


    public TldOrders getOrderById(String id) {
        return dao.findFirst("select " + COLUMN + " from tld_orders where ENABLED = 1 and id = ?", id);
    }

    /**
     * 收到付款逻辑
     *
     * @param orderId
     * @param payType 0微信支付,1线下支付
     */
    public void orderPay(String orderId, int payType) {
        TldOrders orders = getOrderById(orderId);
        if (orders.getSTATUS() == 1) {
            orders.setPaymentTypeid(String.valueOf(payType));
            orders.setPaymentType(payType == 0 ? "微信支付" : "线下支付");
            BasCustomerExt selfExt = extService.getByConId(orders.getConId());
            BasCustomerExt up1Ext = extService.getByConId(orders.getRpId());
            orders.setPayDt(DateUtil.getNow());
            //自己加积分
            int selfIntegral = orders.getSelfIntegral();
            if (selfIntegral > 0) {
                selfExt.setPointsTotal(selfExt.getPointsTotal() + selfIntegral);
                selfExt.setPointsUncfmd(selfExt.getPointsUncfmd() + selfIntegral);
            }
            //重消加自己营业总额,重消总额
            if (orders.getIsReorder() == 1) {
                selfExt.setREVENUES(selfExt.getREVENUES().add(orders.getOrderTotal()));
                selfExt.setReConsumptions(selfExt.getReConsumptions().add(orders.getOrderTotal()));
            }
            //首单+上级已购买
            if (orders.getIsReorder() == 0) {
                up1Ext.setPuredCustQty(up1Ext.getPuredCustQty() + 1);
            }
            //自己消费总额
            selfExt.setCONSUMPTIONS(selfExt.getCONSUMPTIONS().add(orders.getOrderTotal()));
            //自己订单总数量
            selfExt.setOrdersTotal(selfExt.getOrdersTotal() + 1);
            //上级+积分
            int up1Integral = orders.getUp1Integral();
            if (up1Integral > 0) {
                up1Ext.setPointsTotal(up1Ext.getPointsTotal() + up1Integral);
                up1Ext.setPointsUncfmd(up1Ext.getPointsUncfmd() + up1Integral);
            }
            //上级+营业总额
            up1Ext.setREVENUES(selfExt.getREVENUES().add(orders.getOrderTotal()));
            //上级+推广订单总数
            up1Ext.setOrdersProm(up1Ext.getOrdersProm() + 1);

            //变更订单状态
            orders.setSTATUS(2);
            orders.update();
            split(orders);
            //7:加积分流水
            if (selfIntegral > 0) {
                insertPointTrans(selfExt.getConId(), selfExt.getConNo(), selfExt.getConId(), selfExt.getConNo(), orderId, selfIntegral);
            }
            if (up1Integral > 0) {
                insertPointTrans(up1Ext.getConId(), up1Ext.getConNo(), selfExt.getConId(), selfExt.getConNo(), orderId, up1Integral);
            }
            selfExt.update();
            up1Ext.update();
        }
    }

    private void split(TldOrders orders) {
        List<Record> orderItems = Db.find("select QUANTITY,PNT_ID,PNT_NAME,SKU_ID,SKU_NAME from tld_order_items where order_id = ?", orders.getID());
        int splitNum = 1;//分单数量
        for (Record items : orderItems) {
            Integer quantity = items.getInt("QUANTITY");
            Integer tempNum = 0;
            if (quantity < splitNum) {
                inertOrderSplit(orders, items.getStr("PNT_ID"), items.getStr("PNT_NAME"), items.getStr("SKU_ID"), items.getStr("SKU_NAME"), splitNum);
            } else {
                while (quantity > tempNum) {
                    if (quantity - tempNum > splitNum) {
                        inertOrderSplit(orders, items.getStr("PNT_ID"), items.getStr("PNT_NAME"), items.getStr("SKU_ID"), items.getStr("SKU_NAME"), splitNum);
                    } else {
                        inertOrderSplit(orders, items.getStr("PNT_ID"), items.getStr("PNT_NAME"), items.getStr("SKU_ID"), items.getStr("SKU_NAME"), splitNum);
                    }
                    tempNum += splitNum;
                }
            }
        }
    }

    private void inertOrderSplit(TldOrders orders, String pntId, String pntName, String skuId, String skuName, int splitNum) {
        TldOrderSplit split = new TldOrderSplit();
        split.setID(GUIDUtil.getGUID());
        split.setOrderId(orders.getID());
        split.setOrderNo(orders.getOrderNo());
        split.setConId(orders.getConId());
        split.setConNo(orders.getConNo());
        split.setConName(orders.getConName());
        split.setPntId(pntId);
        split.setPntName(pntName);
        split.setSkuId(skuId);
        split.setSkuName(skuName);
        split.setSplitNumber(splitNum);
        split.setCOUNTRY(orders.getCOUNTRY());
        split.setPROVINCE(orders.getPROVINCE());
        split.setCITY(orders.getCITY());
        split.setDISTRICT(orders.getDISTRICT());
        split.setADDRESS(orders.getADDRESS());
        split.setPostalCode(orders.getPostalCode());
        split.setMOBILE(orders.getMOBILE());
        split.setRECIPIENTS(orders.getRECIPIENTS());
        split.setSTATUS(orders.getSTATUS());
        split.setCreateDt(DateUtil.getNow());
        split.setUpdateDt(DateUtil.getNow());
        split.save();
    }

    private void insertPointTrans(String conId, String conNo, String fromConId, String fromConNo, String orderId, int pointsQty) {
        BasCustPointTrans trans = new BasCustPointTrans();
        trans.setID(GUIDUtil.getGUID());
        trans.setConId(conId);
        trans.setConNo(conNo);
        trans.setFromConId(fromConId);
        trans.setFromConNo(fromConNo);
        trans.setFromOrderId(orderId);
        trans.setPointsQty(pointsQty);
        trans.setPointsType(2);
        trans.setCreateDt(DateUtil.getNow());
        trans.setUpdateDt(DateUtil.getNow());
        trans.save();
    }

    public Page<Record> getOrderList(int pageNumber, int pageSize, String conNo, String orderNo, Integer payType, Integer status, String startDt, String endDt) {
        StringBuffer sqlExceptSelect = new StringBuffer("from tld_orders where 1=1");
        if (status != null) {
            sqlExceptSelect.append(" and status = " + status);
        }
        if (payType != null) {
            sqlExceptSelect.append(" and PAYMENT_TYPEID = " + payType);
        }
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and con_no = '" + conNo + "'");
        }
        if (StrKit.notBlank(orderNo)) {
            sqlExceptSelect.append(" and order_no = '" + orderNo + "'");
        }
        if (StrKit.notBlank(orderNo)) {
            sqlExceptSelect.append(" and order_no = '" + orderNo + "'");
        }
        if (StrKit.notBlank(startDt)) {
            sqlExceptSelect.append(" and CREATE_DT >= " + "'" + startDt + "'");
        }
        if (StrKit.notBlank(endDt)) {
            sqlExceptSelect.append(" and CREATE_DT <= " + "'" + endDt + "'");
        }
        sqlExceptSelect.append(" order by CREATE_DT desc");
        Page<Record> tldOrdersPage = Db.paginate(pageNumber, pageSize, "select " + COLUMN + "", sqlExceptSelect.toString());
        for (Record order : tldOrdersPage.getList()) {
            String orderId = order.get("ID");
            order.set("ITEMS", Db.find("select t1.*,t2.THUMB_URL from tld_order_items t1 left join pnt_sku t2 on t1.sku_id = t2.id where t1.ENABLED = 1 and t1.order_id = ?", orderId));
        }
        return tldOrdersPage;
    }

    public boolean updateOrderAddress(TldOrders orders) {
        orders.setUpdateDt(DateUtil.getNow());
        orders.update();
        Db.update("update tld_order_split set COUNTRY = ?,PROVINCE = ?,CITY = ?,DISTRICT = ?,ADDRESS = ?,POSTAL_CODE= ? ,MOBILE=?,RECIPIENTS=? where  order_id = ?",
                orders.getCOUNTRY(), orders.getPROVINCE(), orders.getCITY(), orders.getDISTRICT(), orders.getADDRESS(), orders.getPostalCode(), orders.getMOBILE(), orders.getRECIPIENTS(), orders.getID());
        return true;
    }


    public void orderPrint(String orderId) {
        List<Record> list = Db.find("select * from tld_order_split where order_id = ?", orderId);

    }

    public Map<String, Object> getOrderStatistics(String startDate) {
        Map<String, Object> resultMap = new HashMap<>();
        BigDecimal pntAmount = BigDecimal.ZERO;
        BigDecimal orderTotal = BigDecimal.ZERO;
        int payNum = 0;
        int notShipments = 0;
        List<TldOrders> orders = dao.find("select " + COLUMN + " from tld_orders where create_dt like '" + startDate + "%'");
        for (TldOrders o : orders) {
            if (StrKit.notBlank(o.getPayDt())) {
                pntAmount = pntAmount.add(o.getPntAmount());
                orderTotal = orderTotal.add(o.getOrderTotal());
                payNum++;
            }
            if (o.getSTATUS() == 2) {
                notShipments++;
            }
        }
        //总数
        resultMap.put("ORDER_NUM", orders.size());
        //已付款
        resultMap.put("PAY_NUM", payNum);
        resultMap.put("NOT_SHIPMENTS", notShipments);
        resultMap.put("PNT_AMOUNT", pntAmount);
        resultMap.put("ORDER_TOTAL", orderTotal);
        return resultMap;
    }
}
