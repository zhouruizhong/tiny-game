package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
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
        if (msg instanceof GameProtocol.UserEntryCmd) {
            new UserEntryCmdHandler().handler(ctx, (GameProtocol.UserEntryCmd) msg);
        } else if (msg instanceof GameProtocol.WhoElseIsHereCmd) {
            new WhoElseIsHereCmdHandler().handler(ctx, (GameProtocol.WhoElseIsHereCmd) msg);
        } else if (msg instanceof GameProtocol.UserMoveToCmd) {
            new UserMoveToCmdHandler().handler(ctx, (GameProtocol.UserMoveToCmd) msg);
        }
    }




}
