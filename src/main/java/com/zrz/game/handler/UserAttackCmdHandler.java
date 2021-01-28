package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
import com.zrz.game.GameServer;
import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
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

    private static final Logger logger = LoggerFactory.getLogger(UserAttackCmdHandler.class);

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

        User targetUser = UserManager.getUserById(targetUserId);
        if (null == targetUser) {
            return;
        }

        logger.info("当前线程 = {}", Thread.currentThread().getName());

        int subtractHp = (int)(Math.random() * 10);
        targetUser.setCurrHp(targetUser.getCurrHp() - subtractHp);
        broadcastSubtractHp(targetUserId, subtractHp);

        // 用户血量小于等于0时，用户死亡
        if (targetUser.getCurrHp() <= 0) {
            broadcastDie(targetUserId);
        }
    }

    /**
     * 广播用户掉血
     * @param targetUserId 目标用户id
     * @param subtractHp 掉血量
     */
    public void broadcastSubtractHp(int targetUserId, int subtractHp) {
        if (targetUserId <= 0 || subtractHp <= 0) {
            return;
        }
        // 使被攻击者掉血量，血量值随机
        GameProtocol.UserSubtractHpResult.Builder builder1 = GameProtocol.UserSubtractHpResult.newBuilder();
        builder1.setTargetUserId(targetUserId);
        builder1.setSubtractHp(subtractHp);

        GameProtocol.UserSubtractHpResult result1 = builder1.build();
        Broadcaster.broadcast(result1);
    }

    /**
     * 广播用户死亡
     * @param targetUserId 目标用户id
     */
    public static void broadcastDie(int targetUserId){
        GameProtocol.UserDieResult.Builder builder = GameProtocol.UserDieResult.newBuilder();
        builder.setTargetUserId(targetUserId);
        GameProtocol.UserDieResult result = builder.build();
        Broadcaster.broadcast(result);
    }
}
