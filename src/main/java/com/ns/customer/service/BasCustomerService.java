/**
 * project name: hdy_project
 * file name:BasCustomerService
 * package name:com.ns.customer.service
 * date:2018-02-01 17:22
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.customer.service;

import com.ns.common.constant.RedisKeyDetail;
import com.ns.common.exception.CustException;
import com.ns.common.model.BasCustomer;
import com.ns.common.model.Blog;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.GUIDUtil;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.jfinal.plugin.redis.Redis;
import com.jfinal.weixin.sdk.api.ApiResult;
import com.jfinal.weixin.sdk.api.CustomServiceApi;
import com.jfinal.weixin.sdk.api.UserApi;

import java.util.Objects;

/**
 * description: //TODO <br>
 * date: 2018-02-01 17:22
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class BasCustomerService {
    public static final BasCustomerService me = new BasCustomerService();
    private static final String COLUMN = "ID,CON_NO,CON_NAME,REAL_NAME,CON_TYPE,PIC,SEX,BIRTHDAY,COUNTRY,PROVINCE,CITY,DISTRICT,ADDRESS,MOBILE,UNION_ID,OPENID," +
            "IS_LOCKOUT,RP_ID,RP_NO,RP_NAME,IS_SUBSCRIBE,ENABLED,VERSION,STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    private final BasCustomer dao = new BasCustomer().dao();
    static BasCustomerExtService extService = BasCustomerExtService.me;

    /**
     * 关注新增会员
     *
     * @param refNo
     * @param openId
     */
    public void addCustomer(String refNo, String openId) {
        String str = "hi，你终于找到我了！\n" +
                "\n" +
                "教育孩子，如此简单；\n" +
                "在孩子最美好的年华 ，给予最美好的精神滋养！\n" +
                " \n" +
                "0-7岁，经典陪你长大\n" +
                "\n" +
                "\n" +
                "http://mp.weixin.qq.com/s/DgllyrRCI9eyI-ShdSw3cA";
        BasCustomer customer = getCustomerByOpenId(openId);
        BasCustomer refCustomer = getCustomerByConNo(refNo);
        String rpId = refCustomer == null ? "" : refCustomer.getID();
        String rpNo = refCustomer == null ? "" : refCustomer.getRpNo();
        String rpName = refCustomer == null ? "" : refCustomer.getRpName();
        if (customer != null) {
            //判断是否已经关注公众号
            if (customer.getIsSubscribe() == 1) {
                //发送重复关注消息
                CustomServiceApi.sendText(openId, "请不要重复关注");
            } else {
                //如果之前取消关注,现在重新关注.
                if (refCustomer != null) {
                    //自己扫自己
                    if (openId.equals(refCustomer.getOPENID())) {
                        CustomServiceApi.sendText(openId, "重复关注");
                        return;
                    }
                }
                customer.setIsSubscribe(1);
                customer.setRpId(rpId);
                customer.setRpName(rpNo);
                customer.setRpNo(rpName);
                customer.setUpdateDt(DateUtil.getNow());
                customer.update();
                CustomServiceApi.sendText(openId, str);
                //给上级发
            }
        } else {
            ApiResult result = UserApi.getUserInfo(openId);
            customer = setCustomerAttr(result.getStr("nickname"), result.getStr("headimgurl"), result.getInt("sex"), 0, "", result.getStr("country"), result.getStr("province"),
                    result.getStr("city"), "", "", result.getStr("unionid"), openId, rpId, rpNo, rpName);
            if (customer.save() && extService.addBasCustomerExt(customer.getID(), customer.getConNo(), customer.getConName())) {
                CustomServiceApi.sendText(openId, str);
                //给上级发
            } else {
                throw new CustException("新增会员异常!");
            }
        }

        //setCustomerAttr();

        //return customer.save() && extService.addBasCustomerExt(customer.getID(), customer.getConNo(), customer.getConName());
    }

    public BasCustomer setCustomerAttr(String conName, String pic, int sex, int conType, String birthDay, String country, String province, String city, String district,
                                       String address, String unionId, String openId, String rpId, String rpNo, String rpName) {
        BasCustomer customer = new BasCustomer();
        customer.setID(GUIDUtil.getGUID());
        customer.setConNo(String.valueOf(Redis.use().incr(RedisKeyDetail.CON_NO_SEQ)));
        customer.setConName(conName);
        customer.setConType(conType);
        customer.setPIC(pic);
        customer.setSEX(sex);
        customer.setBIRTHDAY(birthDay);
        customer.setCOUNTRY(country);
        customer.setPROVINCE(province);
        customer.setCITY(city);
        customer.setDISTRICT(district);
        customer.setADDRESS(address);
        customer.setUnionId(unionId);
        customer.setOPENID(openId);
        customer.setRpId(rpId);
        customer.setRpNo(rpNo);
        customer.setRpName(rpName);
        customer.setIsSubscribe(1);
        customer.setCreateDt(DateUtil.getNow());
        customer.setUpdateDt(DateUtil.getNow());
        return customer;
    }

    /**
     * 根据Id找会员信息
     *
     * @param id
     * @return
     */
    public BasCustomer getCustomerById(String id) {
        return dao.findById(id);
    }

    public BasCustomer getCustomerByIdNotNull(String id) {
        BasCustomer customer = dao.findById(id);
        if (customer == null) {
            throw new CustException("找不到会员信息!");
        }
        return customer;
    }

    /**
     * 根据会员号查找
     *
     * @param conNo
     * @param enabled 是否有效
     * @return
     */
    public BasCustomer getCustomerByConNo(String conNo, int enabled) {
        return dao.findFirst("select " + COLUMN + " from bas_customer where con_no = ? and enabled = ?", conNo, enabled);
    }

    public BasCustomer getCustomerByConNo(String conNo) {
        return dao.findFirst("select " + COLUMN + " from bas_customer where con_no = ? and enabled = 1", conNo);
    }


    public BasCustomer getCustomerByConNoNotNull(String conNo) {
        BasCustomer customer = dao.findFirst("select " + COLUMN + " from bas_customer where con_no = ? and enabled = 1", conNo);
        if (customer == null) {
            throw new CustException("找不到会员信息!");
        }
        return customer;
    }

    /**
     * 取消关注
     *
     * @param openId
     */
    public void cancelSubscribe(String openId) {
        BasCustomer customer = getCustomerByOpenId(openId);
        customer.setIsSubscribe(0);
        customer.setUpdateDt(DateUtil.getNow());
        customer.update();
    }

    /**
     * 根据openId查询会员信息
     *
     * @param openId
     */
    public BasCustomer getCustomerByOpenId(String openId) {
        return dao.findFirst("select " + COLUMN + " from bas_customer where openid = ? and enabled = 1", openId);
    }

    public BasCustomer getCustomerByOpenIdNotNull(String openId) {
        BasCustomer customer = dao.findFirst("select " + COLUMN + " from bas_customer where openid = ? and enabled = 1", openId);
        if (customer == null) {
            throw new CustException("找不到会员信息");
        }
        return customer;
    }

    /**
     * 分页查询会员列表
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Page<BasCustomer> getCustomerList(int pageNumber, int pageSize, String conNo, String mobile) {
        StringBuffer sqlExceptSelect = new StringBuffer("from bas_customer where 1=1");
        if (StrKit.notBlank(conNo)) {
            sqlExceptSelect.append(" and CON_NO = " + conNo);
        }
        if (StrKit.notBlank(mobile)) {
            sqlExceptSelect.append(" and MOBILE = " + mobile);
        }
        sqlExceptSelect.append(" and enabled = 1 order by CREATE_DT desc");
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN + "", sqlExceptSelect.toString());
    }

    /**
     * 分页查询我的会员
     *
     * @param pageNumber
     * @param pageSize
     * @return
     */
    public Page<BasCustomer> getMyCustomerList(int pageNumber, int pageSize, String conId) {
        String sqlExceptSelect = "from bas_customer where 1=1 and rp_id = ? and enabled = 1 order by CREATE_DT desc";
        return dao.paginate(pageNumber, pageSize, "select " + COLUMN + "", sqlExceptSelect, conId);
    }

    public boolean updateCustomer(BasCustomer customer) {
        if (StrKit.notBlank(customer.getRpNo())) {
            BasCustomer rpCustomer = getCustomerByConNo(customer.getRpNo());
            if (rpCustomer == null || rpCustomer.getConType() == 0) {
                throw new CustException("找不到上级会员，或上级不是推客！");
            }
            customer.setRpId(rpCustomer.getID());
            customer.setRpName(rpCustomer.getConName());
        }
        customer.setUpdateDt(DateUtil.getNow());
        return customer.update();
    }
}
