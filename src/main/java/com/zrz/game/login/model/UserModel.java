package com.zrz.game.login.model;

import lombok.Data;

/**
 * @author zrz
 * @date 2021/1/28 10:44
 */
@Data
public class UserModel {

    private int userId;
    private String userName;
    private String password;
    private String heroAvatar;

}
