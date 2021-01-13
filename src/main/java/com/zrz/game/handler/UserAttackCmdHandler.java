package com.zrz.game.handler;

import com.zrz.game.GameServer;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
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
    }
}
