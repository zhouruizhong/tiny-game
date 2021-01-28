package com.zrz.game.login.dao;

import com.zrz.game.login.model.UserModel;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author 52844
 */
public interface IUserDao {

    /**
     * 通过用户名查询用户
     * @param userName 用户名
     * @return 用户
     */
    List<UserModel> getUserByName(@Param("userName") String userName);

    /**
     * 添加用户
     * @param userModel 用户信息
     */
    void insert(UserModel userModel);
}
