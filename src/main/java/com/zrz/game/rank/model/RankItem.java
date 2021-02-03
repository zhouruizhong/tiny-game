package com.zrz.game.rank.model;

import lombok.Data;

/**
 * 排行项
 *
 * @author zrz
 * @date 2021/2/2 19:28
 */
@Data
public class RankItem {

    /**排行id*/
    private Integer rankId;
    /**用户id*/
    private Integer userId;
    /**用户名称*/
    private String username;
    /**英雄形象*/
    private String heroAvatar;
    /**胜利次数*/
    private Integer win;
}
