package com.ns.file.controller;

import com.ns.common.base.BaseController;
import com.ns.common.fastdfs.newpool.FastDfsUtil;
import com.ns.common.json.JsonResult;
import com.jfinal.kit.PropKit;
import com.jfinal.upload.UploadFile;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileController extends BaseController {
    private static String baseUploadPath = PropKit.get("baseUploadPath");
    /**
     * 上传图片允许的最大尺寸，目前只允许 200KB
     */
    public static final int imageMaxSize = 200 * 1024;

    public void upload() throws Exception {
        List list = new ArrayList();
        List<UploadFile> uploadFiles = getFiles();//文件上传
        String name = "";
        String fileAbsolutePath = "";
        for (UploadFile upFile : uploadFiles) {
            HashMap<String, String> map = new HashMap<String, String>();
            byte[] bytes = File2byte(upFile.getFile());
            FastDfsUtil util = FastDfsUtil.getInstance();
            fileAbsolutePath = util.upload("group2", bytes, "jpg");
            name = upFile.getParameterName();
            map.put("name", name);
            map.put("url", fileAbsolutePath);
            list.add(map);
        }
        this.getResponse().setHeader("Access-Control-Allow-Origin", "*");
        if (uploadFiles.size() > 1) {
            renderJson(JsonResult.newJsonResult(list));
        } else {
            renderJson(JsonResult.newJsonResult(fileAbsolutePath));//单张图片上传，bug修复
        }
    }

    public static byte[] File2byte(File file) {
        byte[] buffer = null;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

}
