package com.zrz.game.fastdfs;

import lombok.Data;

/**
 * @author zrz
 * @date 2021/2/4 19:01
 */
@Data
public class FastDFSFile {

    /**文件名字*/
    private String name;
    /**文件内容*/
    private byte[] content;
    /**文件扩展名*/
    private String ext;
    /**文件MD5摘要值*/
    private String md5;
    /**文件创建作者*/
    private String author;
}
