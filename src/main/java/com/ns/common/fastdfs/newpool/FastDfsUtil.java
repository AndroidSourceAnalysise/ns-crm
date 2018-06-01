/**
 * project name:distribution
 * file name:fastdfsutil
 * package name:com.cdkj.fastdfs
 * date:2016-09-20 11:57
 * author:Administrator
 * Copyright (c) CD Technology Co.,Ltd. All rights reserved.
 */
package com.ns.common.fastdfs.newpool;

import com.ns.common.fastdfs.FastDfsConfig;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.StorageClient1;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * @author mr_smile2014 mr_smile2014@xxxx.com
 * @ClassName: FastDfsUtil
 * @Description: fastdfs文件操作工具类
 * 1).初始化连接池；
 * 2).实现文件的上传与下载;
 * @date 2015年11月25日 上午10:21:46
 */
public class FastDfsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(FastDfsUtil.class);

    /*私有构造函数*/
    private FastDfsUtil() {

    }

    /*单例*/
    private static FastDfsUtil utilObj = null;

    public static FastDfsUtil getInstance() {
        if (null == utilObj)
            return utilObj = new FastDfsUtil();

        return utilObj;
    }

    /*nginx访问地址*/
    private static String nginxAddr;
    /*文件返回地址组*/
    private static String[] accessServers;
    /* 文件返回地址组 - 当前访问序列*/
    private static int serverIndex = -1;

    /*加载配置文件*/
    static {
        Prop propKit = new Prop("fdfs.properties");
        accessServers = propKit.get("http.access_servers").split(",");
        nginxAddr = propKit.get("http.access_nginx", "114.55.42.56:8080");
    }

    /**
     * 连接池
     */
    private ConnectionPool connectionPool = null;
    /**
     * 连接池默认最小连接数
     */
    private long minPoolSize = PropKit.getInt("fdfs.maxIdle", 10);
    /**
     * 连接池默认最大连接数
     */
    private long maxPoolSize = PropKit.getInt("fdfs.maxTotal", 30);
    /**
     * 当前创建的连接数
     */
    private volatile long nowPoolSize = 0;
    /**
     * 默认等待时间（单位：秒）
     */
    private long waitTimes = PropKit.getInt("fdfs.waitTimes", 200);

    /**
     * 初始化线程池
     *
     * @Description:
     */
    public void init() {
        String logId = UUID.randomUUID().toString();
        LOGGER.info("[初始化线程池(Init)][" + logId + "][默认参数：minPoolSize="
                + minPoolSize + ",maxPoolSize=" + maxPoolSize + ",waitTimes="
                + waitTimes + "]");
        connectionPool = new ConnectionPool(minPoolSize, maxPoolSize, waitTimes);
    }

    /**
     * @param groupName 组名如group0
     * @param fileBytes 文件字节数组
     * @param extName   文件扩展名：如png
     *                  //     * @param linkUrl   访问地址：http://image.xxx.com
     * @return 图片上传成功后地址
     * @throws Exception
     * @Description: 文件上传
     */
    public String upload(String groupName, byte[] fileBytes, String extName) throws Exception {
        String logId = UUID.randomUUID().toString();
        /* 封装文件信息参数 */
        NameValuePair[] metaList = new NameValuePair[]{new NameValuePair(
                "fileName", "")};
        TrackerServer trackerServer = null;
        try {

            /* 获取fastdfs服务器连接 */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 client1 = new StorageClient1(trackerServer,
                    storageServer);

            /* 以文件字节的方式上传 */
            String[] results = client1.upload_file(groupName, fileBytes,
                    extName, metaList);

            /* 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            LOGGER.info("[上传文件（upload）-fastdfs服务器相应结果][" + logId
                    + "][result：results=" + results + "]");

            /* results[0]:组名，results[1]:远程文件名 */
            if (results != null && results.length == 2) {
                String group = results[0];
                String remoteFileName = results[1];
                /*轮询手动指定访问地址:*/
                String serverName = "";//ip:8080
                if ("group1".equals(groupName)) {
                    serverName = getAceServer();
                } else {
//                    String hostName = trackerServer.getInetSocketAddress().getHostName();
//                    serverName = hostName + ":" + FastDfsConfig.TRACKER_NGNIX_PORT;
                    serverName = nginxAddr;
                }
                String fileAbsolutePath = FastDfsConfig.PROTOCOL +
                        serverName +
                        FastDfsConfig.SEPARATOR +
                        group +
                        FastDfsConfig.SEPARATOR +
                        remoteFileName;
//                return linkUrl + "/" + results[0] + "/" + results[1];
                System.out.println("fileAbsolutePath:" + fileAbsolutePath);
                return fileAbsolutePath;

            } else {
                /* 文件系统上传返回结果错误 */
//                throw ERRORS.UPLOAD_RESULT_ERROR.ERROR();
            }
        } catch (Exception e) {

            LOGGER.error("[上传文件（upload)][" + logId + "][异常：" + e + "]");
            connectionPool.drop(trackerServer, logId);
//            throw ERRORS.SYS_ERROR.ERROR();
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 获取文件访问 ip+端口号
     *
     * @return ip+端口号
     */
    private String getAceServer() {
        String defaultServer = "127.0.0.1:8080";

        int index = ++serverIndex;
        try {
            if (index > (accessServers.length - 1)) {
                serverIndex = 0;
            }
            defaultServer = accessServers[serverIndex];
        } catch (Exception e) {
            LOGGER.error("FastDfsUtil getAceServer():{}" + e);
        }

        return defaultServer;
    }


    /**
     * @param group_name      组名
     * @param remote_filename 远程文件名称
     * @throws Exception
     * @Description: 删除fastdfs服务器中文件
     */
    public int deleteFile(String group_name, String remote_filename)
            throws Exception {

        String logId = UUID.randomUUID().toString();
        LOGGER.info("[ 删除文件（deleteFile）][" + logId + "][parms：group_name="
                + group_name + ",remote_filename=" + remote_filename + "]");
        TrackerServer trackerServer = null;

        try {
            /* 获取可用的tracker,并创建存储server */
            trackerServer = connectionPool.checkout(logId);
            StorageServer storageServer = null;
            StorageClient1 client1 = new StorageClient1(trackerServer,
                    storageServer);
            /* 删除文件,并释放 trackerServer */
            int result = client1.delete_file(group_name, remote_filename);

            /* 上传完毕及时释放连接 */
            connectionPool.checkin(trackerServer, logId);

            LOGGER.info("[ 删除文件（deleteFile）--调用fastdfs客户端返回结果][" + logId
                    + "][results：result=" + result + "]");

            /* 0:文件删除成功，2：文件不存在 ，其它：文件删除出错 */
            if (result == 2) {
//                throw ERRORS.NOT_EXIST_FILE.ERROR();

            } else if (result != 0) {

//                throw ERRORS.DELETE_RESULT_ERROR.ERROR();
            }

            return result;
        } catch (Exception e) {

            LOGGER.error("[ 删除文件（deleteFile）][" + logId + "][异常：" + e + "]");
            connectionPool.drop(trackerServer, logId);
//            throw ERRORS.SYS_ERROR.ERROR();

        }

        return -1;
    }

    public ConnectionPool getConnectionPool() {
        return connectionPool;
    }

    public void setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    public long getMinPoolSize() {
        return minPoolSize;
    }

    public void setMinPoolSize(long minPoolSize) {
        this.minPoolSize = minPoolSize;
    }

    public long getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(long maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    public long getNowPoolSize() {
        return nowPoolSize;
    }

    public void setNowPoolSize(long nowPoolSize) {
        this.nowPoolSize = nowPoolSize;
    }

    public long getWaitTimes() {
        return waitTimes;
    }

    public void setWaitTimes(long waitTimes) {
        this.waitTimes = waitTimes;
    }
}