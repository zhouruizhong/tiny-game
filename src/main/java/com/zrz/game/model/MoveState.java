package com.zrz.game.model;

import lombok.Data;

/**
 * 移动状态
 *
 * @author zrz
 * @date 2021/1/26 20:04
 */
@Data
public class MoveState {

    /**起始位置*/
    private float fromPosX;
    /**起始位置*/
    private float fromPosY;
    /**目标位置*/
    private float toPosX;
    /**目标位置*/
    private float toPosY;
    /**启程时间*/
    private long startTime;
}
