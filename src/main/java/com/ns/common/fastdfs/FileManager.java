package com.ns.common.fastdfs;




import com.ns.common.fastdfs.pool.FastdfsClient;
import com.ns.common.fastdfs.pool.FastdfsPool;
import org.csource.common.MyException;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.FileInfo;
import org.csource.fastdfs.ServerInfo;
import org.csource.fastdfs.StorageServer;

import java.io.IOException;

/**
 * PackageName:com.cdkj.common.plugin.fastdfs<br/>
 * Descript:文件控制器 <br/>
 * Date: 2016-05-12 <br/>
 * User: Bovine
 * version 1.0
 */
public class FileManager {
    private static final long serialVersionUID = -798353461273058906L;

    public static String upload(FastDfsFile file) {
        NameValuePair[] meta_list = new NameValuePair[3];
        meta_list[0] = new NameValuePair("width", file.getWidth());
        meta_list[1] = new NameValuePair("heigth", file.getHeight());
        meta_list[2] = new NameValuePair("author", file.getAuthor());

        long startTime = System.currentTimeMillis();
        FastdfsPool pool = FastDfsConfig.pool;
        FastdfsClient fastdfsClient = pool.getResource();
        String[] uploadResults = null;
        try {
            uploadResults = fastdfsClient.upload_file(file.getContent(), file.getExt(), meta_list);
        } catch (Exception e) {
            return e.getMessage();
        }
        if (uploadResults == null) {
            System.out.println("upload file fail, error code: " + fastdfsClient.getErrorCode());
        }

        String groupName = uploadResults[0];
        String remoteFileName = uploadResults[1];
        // String hostName = fastdfsClient.getStorageServer().getInetSocketAddress().getHostName();
        String fileAbsolutePath = FastDfsConfig.PROTOCOL + "121.43.38.146" //"114.55.138.1"
                + ":"
                + FastDfsConfig.TRACKER_NGNIX_PORT
                + FastDfsConfig.SEPARATOR
                + groupName
                + FastDfsConfig.SEPARATOR
                + remoteFileName;
        System.out.println("upload file successfully!!!  " + "group_name: " + groupName + ", remoteFileName:"
                + " " + remoteFileName);

        FastDfsConfig.returnResource(fastdfsClient);//链接池回收 --重要动作，切勿注释
        return fileAbsolutePath;

    }

    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            return FastDfsConfig.storageClient.get_file_info(groupName, remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static int deleteFile(String groupName, String remoteFileName) {
        FastdfsClient fastdfsClient = FastDfsConfig.pool.getResource();
        int rs = -1001;
        try {
            rs = fastdfsClient.delete_file(groupName, remoteFileName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (MyException e) {
            e.printStackTrace();
        } finally {
            FastDfsConfig.returnResource(fastdfsClient);
        }
        return rs;

//        int rs = -1001;
//        try {
//            rs = FastDfsConfig.storageClient.delete_file(groupName, remoteFileName);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (MyException e) {
//            e.printStackTrace();
//        }
//
//        return rs;
    }

    public static StorageServer[] getStoreStorages(String groupName) throws IOException {
        return FastDfsConfig.trackerClient.getStoreStorages(FastDfsConfig.trackerServer, groupName);
    }

    public static ServerInfo[] getFetchStorages(String groupName, String remoteFileName) throws IOException {
        return FastDfsConfig.trackerClient.getFetchStorages(FastDfsConfig.trackerServer, groupName, remoteFileName);
    }
}
