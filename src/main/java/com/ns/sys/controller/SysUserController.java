package com.ns.sys.controller;

import com.ns.common.base.BaseController;
import com.ns.common.exception.CustException;
import com.ns.common.interceptor.UserValidatorInterceptor;
import com.ns.common.json.JsonResult;
import com.ns.sys.service.SysUserService;
import com.jfinal.aop.Clear;
import com.jfinal.kit.StrKit;

public class SysUserController extends BaseController {
    static SysUserService sysUserService = SysUserService.me;

    @Clear(UserValidatorInterceptor.class)
    public void login() {
        String userName = getPara("userName");
        String password = getPara("password");
        if (!StrKit.notBlank(userName, password)) {
            throw new CustException("用户名密码不能为空!");
        }
        renderJson(JsonResult.newJsonResult(sysUserService.login(userName, password, getResponse())));
    }
}
