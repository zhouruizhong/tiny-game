package com.zrz.game.mq;

import lombok.Data;

/**
 * 战斗结束消息
 *
 * @author zrz
 * @date 2021/2/3 18:25
 */
@Data
public class VictorMsg {

    /**输家id*/
    private Integer loseId;
    /**胜利者id*/
    private Integer winnerId;
}
