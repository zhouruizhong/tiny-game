package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 用户移动指令处理
 *
 * @author 周瑞忠
 */
public class UserMoveToCmdHandler implements ICmdHandler<GameProtocol.UserMoveToCmd> {

    @Override
    public void handler(ChannelHandlerContext ctx, GameProtocol.UserMoveToCmd msg) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        GameProtocol.UserMoveToCmd cmd = msg;
        GameProtocol.UserMoveToResult.Builder builder = GameProtocol.UserMoveToResult.newBuilder();
        builder.setMoveUserId(userId);
        builder.setMoveToPosX(cmd.getMoveToPosX());
        builder.setMoveToPosY(cmd.getMoveToPosY());

        GameProtocol.UserMoveToResult result = builder.build();
        Broadcaster.broadcast(result);
    }
}
