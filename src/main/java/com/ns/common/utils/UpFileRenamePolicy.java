package com.ns.common.utils;

import com.jfinal.core.JFinal;
import com.jfinal.kit.StrKit;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * JFinal2.0文件上传重命名策略
 *
 * @author L.cm
 * email: 596392912@qq.com
 * site:http://www.dreamlu.net
 * date 2015年7月10日下午11:23:25
 */
//public class UpFileRenamePolicy implements FileRenamePolicy {
public class UpFileRenamePolicy {
    //public rname

    //@Override
    public static String rename(File f) {
        // 获取webRoot目录
        //String webRoot = PathKit.getWebRootPath();
        String webRoot = "";

        // 用户设置的默认上传目录
        String saveDir = JFinal.me().getConstants().getBaseUploadPath();
        //String saveDir = "";
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");
        // 添加时间作为目录
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMdd");

        // /xxx/JFinal2.0-beetl-training/file/20150710/1436542837568.txt
        //StringBuilder newFileName = new StringBuilder(webRoot)
        //        .append(File.separator)
        //        .append(StrKit.isBlank(saveDir) ? "file" : saveDir)
        //        .append(File.separator)
        //        .append(dateFormat.format(new Date()))
        //        .append(File.separator)
        //        .append(System.currentTimeMillis())
        //        .append(getFileExt(f.getName()));

        String sYear = yearFormat.format(new Date());
        String sDate = dateFormat.format(new Date());
        String uuid = UUID.randomUUID().toString();
        String ext = getFileExt(f.getName());
        StringBuilder newFileName = new StringBuilder("")
                .append(StrKit.isBlank(saveDir) ? "file" : saveDir)
                .append(File.separator)
                .append(sYear)
                .append(File.separator)
                .append(sDate)
                .append(File.separator)
                .append(uuid)
                .append(ext);

        StringBuilder newUrl = new StringBuilder("")
                .append(sYear)
                .append("/")
                .append(sDate)
                .append("/")
                .append(uuid)
                .append(ext);

        File dest = new File(newFileName.toString());
        // 创建上层目录
        File dir = dest.getParentFile();
        if (!dir.exists()) {
            dir.mkdirs();
        }
        System.out.println(f.getName());
        if (f.renameTo(dest)){
            return newUrl.toString();
        }
        return "";
    }

    /**
     * 获取文件后缀
     *
     * @param @param fileName
     * @param @return 设定文件
     * @return String 返回类型
     */
    public static String getFileExt(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'), fileName.length());
    }
}