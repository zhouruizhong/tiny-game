package com.zrz.game.handler;

import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.Broadcaster;
import com.zrz.game.factory.CmdHandlerFactory;
import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 周瑞忠
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        Broadcaster.addChannel(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        Broadcaster.removeChannel(ctx.channel());
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        UserManager.removeUserById(userId);

        GameProtocol.UserQuitResult.Builder builder = GameProtocol.UserQuitResult.newBuilder();
        builder.setQuitUserId(userId);

        GameProtocol.UserQuitResult result = builder.build();
        Broadcaster.broadcast(result);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        logger.info("收到客户端消息， msgClazz =" + msg.getClass().getName() + ", msg = " + msg);

        ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msg.getClass());
        if (null != cmdHandler) {
            cmdHandler.handler(ctx, cast(msg));
        }
    }

    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if (null == msg) {
            return null;
        } else {
            return (TCmd) msg;
        }
    }


}
