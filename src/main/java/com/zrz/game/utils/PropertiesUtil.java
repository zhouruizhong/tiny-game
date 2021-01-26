package com.zrz.game.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

/**
 * 属性值
 *
 * @author zrz
 * @date 2021/1/26 9:12
 */
public class PropertiesUtil {

    private String propertiesName = "";

    public PropertiesUtil() {

    }

    public PropertiesUtil(String fileName) {
        this.propertiesName = fileName;
    }

    /**
     * 按key获取值
     * @param key 键
     * @return 值
     */
    public String readProperty(String key) {
        String value = "";
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            Properties p = new Properties();
            p.load(is);
            value = p.getProperty(key);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return value;
    }

    /**
     * 获取整个配置信息
     * @return Properties
     */
    public Properties getProperties() {
        Properties p = new Properties();
        InputStream is = null;
        try {
            is = PropertiesUtil.class.getClassLoader().getResourceAsStream(propertiesName);
            p.load(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                assert is != null;
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return p;
    }

    /**
     * key-value写入配置文件
     * @param key 键
     * @param value 值
     */
    public void writeProperty(String key, String value) {
        InputStream is = null;
        OutputStream os = null;
        Properties p = new Properties();
        try {
            is = new FileInputStream(propertiesName);
            p.load(is);
            os = new FileOutputStream(propertiesName);

            p.setProperty(key, value);
            p.store(os, key);
            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != is) {
                    is.close();
                }
                if (null != os) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public static void main(String[] args) {
        // sysConfig.properties(配置文件)
        //PropertiesUtil p = new PropertiesUtil("config.properties");
        //System.out.println(p.getProperties().get("db.url"));
        //System.out.println(p.readProperty("db.url"));
        //redisIp=39.96.68.251
        //redisPort=6379
        //redisPassword=
        //maxTotal=2
        //maxIdle=2
        PropertiesUtil q = new PropertiesUtil("/config.properties");
        q.writeProperty("redisIp", "39.96.68.251");
        q.writeProperty("redisPort", "6379");
        q.writeProperty("redisPassword", "123456");
        q.writeProperty("maxTotal", "2");
        q.writeProperty("maxIdle", "2");
        System.exit(0);
    }
}
