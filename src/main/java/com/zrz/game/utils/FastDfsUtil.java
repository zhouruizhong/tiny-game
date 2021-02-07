package com.zrz.game.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.util.UUID;

import com.zrz.game.fastdfs.FastDFSFile;
import org.csource.common.NameValuePair;
import org.csource.fastdfs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * fastDFS文件操作工具类
 * 1).初始化连接池；
 * 2).实现文件的上传与下载;
 *
 * @author zrz
 * @date 2021/2/4 18:50
 */
public class FastDfsUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(FastDfsUtil.class);

    /*
     * 初始化加载FastDFS的TrackerServer配置
     */
    static {
        try {
            String filePath = new File("fastDFS.conf").getAbsolutePath();
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            LOGGER.error("FastDFS Client Init Fail!",e);
        }
    }

    /***
     * 文件上传
     * @param file 文件信息
     * @return 1.文件的组名  2.文件的路径信息
     */
    public static String[] upload(FastDFSFile file) {
        //获取文件的作者
        NameValuePair[] metaList = new NameValuePair[1];
        metaList[0] = new NameValuePair("author", file.getAuthor());

        //接收返回数据
        String[] uploadResults = null;
        StorageClient storageClient=null;
        try {
            //创建StorageClient客户端对象
            storageClient = getTrackerClient();

            /*
             * 文件上传
             * 1)文件字节数组
             * 2)文件扩展名
             * 3)文件作者
             */
            uploadResults = storageClient.upload_file(file.getContent(), file.getExt(), metaList);
        } catch (Exception e) {
            LOGGER.error("Exception when uploadind the file:" + file.getName(), e);
        }

        if (uploadResults == null && storageClient!=null) {
            LOGGER.error("upload file fail, error code:" + storageClient.getErrorCode());
        }
        //获取组名
        assert uploadResults != null;
        String groupName = uploadResults[0];
        //获取文件存储路径
        String remoteFileName = uploadResults[1];
        return uploadResults;
    }

    /***
     * 获取文件信息
     * @param groupName:组名
     * @param remoteFileName：文件存储完整名
     * @return FileInfo
     */
    public static FileInfo getFile(String groupName, String remoteFileName) {
        try {
            StorageClient storageClient = getTrackerClient();
            return storageClient.get_file_info(groupName, remoteFileName);
        } catch (Exception e) {
            LOGGER.error("Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /***
     * 文件下载
     * @param groupName 组名
     * @param remoteFileName 文件存储完整名
     * @return InputStream
     */
    public static InputStream downFile(String groupName, String remoteFileName) {
        try {
            //创建StorageClient
            StorageClient storageClient = getTrackerClient();

            //下载文件
            byte[] fileByte = storageClient.download_file(groupName, remoteFileName);
            return new ByteArrayInputStream(fileByte);
        } catch (Exception e) {
            LOGGER.error("Exception: Get File from Fast DFS failed", e);
        }
        return null;
    }

    /***
     * 文件删除
     * @param groupName 组名
     * @param remoteFileName 文件存储完整名
     * @throws Exception 异常
     */
    public static void deleteFile(String groupName, String remoteFileName)
            throws Exception {
        //创建StorageClient
        StorageClient storageClient = getTrackerClient();

        //删除文件
        int i = storageClient.delete_file(groupName, remoteFileName);
    }

    /***
     * 获取Storage组
     * @param groupName 组名
     * @return StorageServer 存储服务器
     * @throws IOException 异常
     */
    public static StorageServer[] getStoreStorages(String groupName)
            throws IOException {
        //创建TrackerClient
        TrackerClient trackerClient = new TrackerClient();
        //获取TrackerServer
        TrackerServer trackerServer = trackerClient.getConnection();
        //获取Storage组
        return trackerClient.getStoreStorages(trackerServer, groupName);
    }

    /***
     * 获取Storage信息,IP和端口
     * @param groupName 组名
     * @param remoteFileName 文件存储完整名
     * @return ServerInfo 服务信息
     * @throws IOException 异常
     */
    public static ServerInfo[] getFetchStorages(String groupName,
                                                String remoteFileName) throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        TrackerServer trackerServer = trackerClient.getConnection();
        return trackerClient.getFetchStorages(trackerServer, groupName, remoteFileName);
    }

    /***
     * 获取Tracker服务地址
     * @return 服务地址
     * @throws IOException 异常
     */
    public static String getTrackerUrl() throws IOException {
        return "http://"+getTrackerServer().getInetSocketAddress().getHostString()+":"+ClientGlobal.getG_tracker_http_port()+"/";
    }

    /***
     * 获取Storage客户端
     * @return 客户端
     * @throws IOException 异常
     */
    private static StorageClient getTrackerClient() throws IOException {
        TrackerServer trackerServer = getTrackerServer();
        return new StorageClient(trackerServer, null);
    }

    /***
     * 获取Tracker
     * @return TrackerServer
     * @throws IOException 异常
     */
    private static TrackerServer getTrackerServer() throws IOException {
        TrackerClient trackerClient = new TrackerClient();
        return trackerClient.getConnection();
    }
}
