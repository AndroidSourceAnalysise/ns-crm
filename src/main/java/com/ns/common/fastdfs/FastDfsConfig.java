package com.ns.common.fastdfs;

import com.ns.common.fastdfs.pool.FastdfsClient;
import com.ns.common.fastdfs.pool.FastdfsPool;
import com.ns.common.fastdfs.pool.FastdfsPoolConfig;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.StorageServer;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;

/**
 * Created by Administrator on 2016-05-14.
 */
public class FastDfsConfig {

    //懒汉式单例类.在第一次调用的时候实例化自己
    private FastDfsConfig() {
        init();
    }

//    private static int fileUpPool = PropKit.getInt("fileUpPool");

    public static final String FILE_DEFAULT_WIDTH = "120";
    public static final String FILE_DEFAULT_HEIGHT = "120";
    public static final String FILE_DEFAULT_AUTHOR = "system";
    public static final String PROTOCOL = "http://";
    public static final String SEPARATOR = "/";
    public static final String TRACKER_NGNIX_PORT = "8080";
    public static final String CLIENT_CONFIG_FILE = "fdfs.properties";

    public static TrackerClient trackerClient;
    public static TrackerServer trackerServer;
    public static StorageServer storageServer;
    public static StorageClient storageClient;

    public static FastdfsPool pool;

    public void init() {
        try {
//            File pathFile = new File(FileManager.class.getResource("/").getFile());
//            String classPath = pathFile.getCanonicalPath();
//            String configFilePath = classPath + File.separator + CLIENT_CONFIG_FILE;
//            ClientGlobal.init(configFilePath);
//
//            trackerClient = new TrackerClient();
//            trackerServer = trackerClient.getConnection();
//
//            storageServer = trackerClient.getStoreStorage(trackerServer);
//            storageClient = new StorageClient(trackerServer, storageServer);
            pool = new FastdfsPool(new FastdfsPoolConfig(), 15);//连接池
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public static void initClient() throws IOException {
//        if (null !=trackerServer){
//            trackerServer.close();
//        }
//        if (null !=storageServer){
//            storageServer.close();
//        }
//        trackerClient = new TrackerClient();
//        trackerServer = trackerClient.getConnection();
//        storageClient = new StorageClient(trackerServer, storageServer);
//    }


    private static FastDfsConfig config = null;

    //静态工厂方法
    public static FastDfsConfig getInstance() {
        if (config == null) {
            config = new FastDfsConfig();
        }
        return config;
    }

    public static void returnResource(FastdfsClient fastdfsClient) {
        pool.returnResource(fastdfsClient);
    }

}