package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
import com.zrz.game.model.MoveState;
import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
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
        if (null == ctx || null == msg) {
            return;
        }

        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }

        User user = UserManager.getUserById(userId);
        if (null == user) {
            return;
        }

        MoveState state = user.getMoveState();
        state.setFromPosX(msg.getMoveFromPosX());
        state.setFromPosY(msg.getMoveFromPosY());
        state.setToPosX(msg.getMoveToPosX());
        state.setToPosY(msg.getMoveToPosY());
        state.setStartTime(System.currentTimeMillis());

        GameProtocol.UserMoveToResult.Builder builder = GameProtocol.UserMoveToResult.newBuilder();
        builder.setMoveUserId(userId);
        builder.setMoveToPosX(state.getToPosX());
        builder.setMoveToPosY(state.getToPosY());
        builder.setMoveFromPosX(state.getFromPosX());
        builder.setMoveFromPosY(state.getFromPosY());
        builder.setMoveStartTime(state.getStartTime());

        GameProtocol.UserMoveToResult result = builder.build();
        Broadcaster.broadcast(result);
    }
}
