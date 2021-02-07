package com.zrz.game.model;

import lombok.Data;

/**
 * 用户
 * @author 周瑞忠
 */
@Data
public class User {
    /**用户id**/
    private int userId;
    /**用户名*/
    private String userName;
    /**英雄头像**/
    private String heroAvatar;
    /**用户血量*/
    private int currHp;
    /**是否存活*/
    private boolean died;
    /**移动状态*/
    private MoveState moveState = new MoveState();
}
