/**
 * project name: api
 * file name:UserValidatorInterceptor
 * package name:com.vellgo.common
 * date:2017/8/2 0002 下午 5:26
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */

package com.ns.common.interceptor;

import com.jfinal.aop.Interceptor;
import com.jfinal.aop.Invocation;
import com.jfinal.core.Controller;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import java.util.HashMap;
import java.util.Map;

/**
 * description: 用户验证拦截器//TODO <br>
 * date: 2017/8/2 0002 下午 5:26
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class UserValidatorInterceptor implements Interceptor {
    @Override
    public void intercept(Invocation inv) {
        Map map = new HashMap<>();
        try {
            Controller controller = inv.getController();
            String userInfo = controller.getCookie("a");
            if (StrKit.notBlank(userInfo)) {
                System.out.println("拦截器=============================================================userInfo:" + userInfo);
                Cache jedis = Redis.use();
                if (!jedis.exists(userInfo)) {
                    map.put("result", "-2");
                    map.put("data", "未登录");
                    controller.renderJson(map);
                    //inv.invoke();
                } else {
                    controller.setAttr("con_id", jedis.get(userInfo));
                    jedis.expire(userInfo, 1800);//设置过期时间为30分钟
                    inv.invoke();
                }
            } else {
                System.out.println("拦截器=============================================================null:" + userInfo);
                map.put("result", "-2");
                map.put("data", "未登录");
                controller.renderJson(map);
            }
        } catch (Exception e) {
            throw e;
        }


    }
}