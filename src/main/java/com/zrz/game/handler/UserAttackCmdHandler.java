package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
import com.zrz.game.GameServer;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户攻击处理器
 *
 * @author 周瑞忠
 */
public class UserAttackCmdHandler implements ICmdHandler<GameProtocol.UserAttackCmd> {

    private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

    @Override
    public void handler(ChannelHandlerContext ctx, GameProtocol.UserAttackCmd userAttackCmd) {
        logger.info("用户发起了攻击指令");
        if (null == ctx || null == userAttackCmd) {
            return;
        }

        // 攻击者id
        Integer attackUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == attackUserId) {
            return;
        }

        // 被攻击者id
        int targetUserId = userAttackCmd.getTargetUserId();
        GameProtocol.UserAttackResult.Builder builder = GameProtocol.UserAttackResult.newBuilder();
        builder.setAttackUserId(attackUserId);
        builder.setTargetUserId(targetUserId);
        GameProtocol.UserAttackResult result = builder.build();
        Broadcaster.broadcast(result);

        // 使被攻击者掉血量，血量值随机
        GameProtocol.UserSubtractHpResult.Builder builder1 = GameProtocol.UserSubtractHpResult.newBuilder();
        builder1.setTargetUserId(targetUserId);
        builder1.setSubtractHp((int)(Math.random()*10));

        GameProtocol.UserSubtractHpResult result1 = builder1.build();
        Broadcaster.broadcast(result1);
    }
}
