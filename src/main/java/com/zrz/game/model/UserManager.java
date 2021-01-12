package com.zrz.game.model;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用户管理类
 *
 * @author 周瑞忠
 */
public final class UserManager {

    /**
     * 用户
     */
    private static final Map<Integer, User> USER_MAP = new ConcurrentHashMap<>(16);

    private UserManager(){

    }

    /**
     * 添加用户
     * @param newUser 用户
     */
    public static void addUser(User newUser){
        if (null != newUser) {
            USER_MAP.put(newUser.getUserId(), newUser);
        }
    }

    /**
     * 根据用户id 移除用户
     * @param userId 用户id
     */
    public static void removeUserById(Integer userId){
        USER_MAP.remove(userId);
    }

    /**
     * 获取所有用户
     * @return Collection
     */
    public static Collection<User> listUser(){
        return USER_MAP.values();
    }

}
