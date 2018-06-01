package com.ns.sys.service;

import com.ns.common.exception.CustException;
import com.ns.common.model.SysUser;
import com.jfinal.plugin.redis.Cache;
import com.jfinal.plugin.redis.Redis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class SysUserService {
    public static final SysUserService me = new SysUserService();
    private final SysUser dao = new SysUser();
    private static final String COLUMN = "ID,USER_NAME,PWD,MOBILE,MAC,ENABLED,VERSION,STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT ";

    public SysUser getUserByUserNameAndPassword(String userName, String password) {
        return dao.findFirst("select " + COLUMN + " from sys_user where USER_NAME = ? and PWD = ?", userName, password);
    }

    public String login(String userName, String password, HttpServletResponse response) {
        SysUser user = getUserByUserNameAndPassword(userName, password);
        if (user == null) {
            throw new CustException("用户名密码错误！");
        }
        String uuid = setCustomerCookie(response, user.getID());
        return uuid;
    }

    /**
     * 设置cookie
     *
     * @param response
     * @param userId
     * @return
     */
    public static String setCustomerCookie(HttpServletResponse response, String userId) {
        String uuid = generateSessionId(userId);
        Cookie cookie = new Cookie("a", uuid);
        cookie.setPath("/");
        cookie.setMaxAge(60 * 30);
        response.addCookie(cookie);
        return uuid;
    }

    public static String generateSessionId(String userId) {
        String uuid = UUID.randomUUID().toString();
        Cache cache = Redis.use();
        cache.set(uuid, userId);
        cache.expire(uuid, 1800);//设置过期时间为30分钟
        return uuid;
    }
}
