package com.ns.customer.controller;

import com.ns.common.base.BaseController;
import com.ns.common.json.JsonResult;
import com.ns.common.model.BasCustAddress;
import com.ns.common.utils.Util;
import com.ns.customer.service.BasCustAddressService;

public class BasCustAddressController extends BaseController {
    static BasCustAddressService service = BasCustAddressService.me;

    public void createAddress() {
        BasCustAddress address = Util.getRequestObject(getRequest(), BasCustAddress.class);
        renderJson(JsonResult.newJsonResult(service.createAddress(address)));
    }

    public void getAddressList() {
        renderJson(JsonResult.newJsonResult(service.getAddressList(getPara("conId"))));
    }

    public void getDefault() {
        renderJson(JsonResult.newJsonResult(service.getDefault(getPara("conId"))));
    }

    public void updateAddress() {
        BasCustAddress address = Util.getRequestObject(getRequest(), BasCustAddress.class);
        renderJson(JsonResult.newJsonResult(service.updateAddress(address)));
    }

    public void deleteAddress() {
        renderJson(JsonResult.newJsonResult(service.deleteAddress(getPara("id"))));
    }
}
