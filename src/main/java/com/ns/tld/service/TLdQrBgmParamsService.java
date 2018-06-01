/**
 * project name: hdy_project
 * file name:TLdQrBgmParamsService
 * package name:com.ns.tld.service
 * date:2018-02-02 14:57
 * author: wq
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.tld.service;

import com.ns.common.model.BasCustomer;
import com.ns.common.model.TldQrbgmParams;
import com.ns.customer.service.BasCustomerService;

/**
 * description: //TODO <br>
 * date: 2018-02-02 14:57
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class TLdQrBgmParamsService {
    private static TldQrbgmParams dao = new TldQrbgmParams().dao();
    private static final String COLUMN = "ID,QR_X,QR_Y,QR_W,QR_H,ICON_X,ICON_Y,ICON_W,ICON_H,CODE_URL,FONT_CHARSET,FONT_SIZE,CON_NAME_X,CON_NAME_Y,SHOW_CON_NAME,SHOW_ICON," +
            "ENABLED,VERSION,STATUS,REMARK,CREATE_BY,CREATE_DT,UPDATE_DT";
    static BasCustomerService service = new BasCustomerService();

    public TldQrbgmParams getById(String id) {
        return dao.findById(id);
    }

    /**
     * 获取默认二维码
     *
     * @return
     */
    public TldQrbgmParams getDefault() {
        return dao.findFirst("select " + COLUMN + " from tld_qrbgm_params WHERE STATUS=1");
    }
}
