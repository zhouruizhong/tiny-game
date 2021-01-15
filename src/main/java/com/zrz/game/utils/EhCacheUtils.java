package com.zrz.game.utils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * 缓存工具类
 *
 * @author 周瑞忠
 * @date 2021-1-15
 */
public class EhCacheUtils {

    private static CacheManager cacheManager=null;

    public  static final String SYS_CACHE = "sysCache";
    public static final String MIN10 = "MIN10";
    public static final String HOUR2 = "HOUR2";

    public final static Byte[] LOCKS = new Byte[0];

    /**
     * 获取SYS_CACHE缓存
     * @param key 键
     * @return 值
     */
    public static Object get(String key) {
        return get(SYS_CACHE, key);
    }

    /**
     * 写入SYS_CACHE缓存
     * @param key 键
     * @return 值
     */
    public static void put(String key, Object value) {
        put(SYS_CACHE, key, value);
    }

    /**
     * @param key 键名
     * @return 值
     */
    public static void remove(String key) {
        remove(SYS_CACHE, key);
    }

    /**
     * 获取缓存
     * @param cacheName 缓存名称
     * @param key 键名
     * @return 缓存值
     */
    public static Object get(String cacheName, String key) {
        Element element = getCache(cacheName).get(key);
        return element == null ? null : element.getObjectValue();
    }

    /**
     * 写入缓存
     * @param cacheName 缓存名称
     * @param key 键
     * @param value 值
     */
    public static void put(String cacheName, String key, Object value) {
        Element element = new Element(key, value);
        getCache(cacheName).put(element);
    }

    /**
     * 从缓存中移除
     * @param cacheName 缓存名称
     * @param key 键
     */
    public static void remove(String cacheName, String key) {
        getCache(cacheName).remove(key);
    }

    public static void removeAll(String cacheName) {
        getCache(cacheName).removeAll();
    }

    /**
     * @param cacheName 缓存名称
     * @return 缓存对象
     */
    public static Cache getCache(String cacheName) {
        if (cacheManager == null) {
            synchronized (LOCKS) {
                if (cacheManager == null) {
                    System.setProperty("net.sf.ehcache.enableShutdownHook","true");
                    cacheManager = CacheManager.create();
                }
            }
        }
        Cache cache = cacheManager.getCache(cacheName);
        if (cache == null) {
            cacheManager.addCache(cacheName);
            cache = cacheManager.getCache(cacheName);
            //sava all the day
            cache.getCacheConfiguration().setEternal(true);
        }
        return cache;
    }

    public static CacheManager getCacheManager() {
        return cacheManager;
    }
}
