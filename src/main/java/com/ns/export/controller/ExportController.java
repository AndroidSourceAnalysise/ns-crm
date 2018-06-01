package com.ns.export.controller;

import com.alibaba.fastjson.JSON;
import com.ns.common.json.JsonResult;
import com.ns.common.utils.DateUtil;
import com.ns.common.utils.Util;
import com.ns.export.service.ExportService;
import com.jfinal.aop.Before;
import com.jfinal.core.Controller;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.upload.UploadFile;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;

/**
 * description: //TODO <br>
 * date: 2018-04-26 16:12
 *
 * @author wq
 * @version 1.0
 * @since JDK 1.8
 */
public class ExportController extends Controller {
    static ExportService service = ExportService.me;

    public void export() {
        //String[] orderIds = Util.getRequestObject(getRequest(), String[].class);
        String ids = getPara("orderIds");
        String[] orderIds = JSON.parseObject(ids, String[].class);
        //String[] orderIds = {"4510B5778B874CF49BFB824B064AE086", "36DC426AEBE94B2BB933D224143DFD7B", "4D295D119DF14A6783DF9F9A17BA84B5", "2274EE3CA342480291FD48678859EEFD"};//Util.getRequestObject(getRequest(), String[].class);
        writeStream("发货单_" + DateUtil.getNow("yyyyMMddHHmmss"), service.export(orderIds), getResponse(), getRequest());
        renderNull();
    }
    public void exportOrder() {
        String ids = getPara("orderIds");
        String[] orderIds = JSON.parseObject(ids, String[].class);
        writeStream("订单报表_" + DateUtil.getNow("yyyyMMddHHmmss"), service.exportOrder(orderIds), getResponse(), getRequest());
        renderNull();
    }

    @Before(Tx.class)
    public void importExcel() throws IOException {
        UploadFile files = getFile("excel");
        renderJson(JsonResult.newJsonResult(service.importExcel(files)));
    }

    private static void writeStream(String filename, HSSFWorkbook wb, HttpServletResponse response, HttpServletRequest request) {
        try {
            String agent = request.getHeader("USER-AGENT");
            filename += ".xls";
            filename.replaceAll("/", "-");
            if (agent.toLowerCase().indexOf("firefox") > 0) {
                filename = new String(filename.getBytes("utf-8"), "iso-8859-1");
            } else {
                filename = URLEncoder.encode(filename, "UTF-8");
            }
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setHeader("Content-Disposition", "attachment; filename=" + filename);
            response.setContentType("application/octet-stream;charset=UTF-8");
            OutputStream outputStream = new BufferedOutputStream(response.getOutputStream());
            wb.write(outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
