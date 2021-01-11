package com.zrz.game.handler;

import com.google.protobuf.MessageLite;
import com.zrz.game.model.User;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.util.AttributeKey;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 周瑞忠
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameServerHandler.class);

    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private static final Map<Integer, User> USER_MAP = new ConcurrentHashMap<>(16);

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        CHANNEL_GROUP.add(ctx.channel());
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        super.handlerRemoved(ctx);
        CHANNEL_GROUP.remove(ctx);
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if (null == userId) {
            return;
        }
        USER_MAP.remove(userId);

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("收到客户端消息， msgClazz =" + msg.getClass().getName() + ", msg = " + msg);
        if (msg instanceof GameProtocol.UserEntryCmd) {
            GameProtocol.UserEntryCmd cmd = (GameProtocol.UserEntryCmd)msg;
            int userId = cmd.getUserId();
            String heroAvatar = cmd.getHeroAvatar();

            GameProtocol.UserEntryResult.Builder builder = GameProtocol.UserEntryResult.newBuilder();
            builder.setUserId(userId);
            builder.setHeroAvatar(heroAvatar);

            User newUser = new User();
            newUser.setUserId(userId);
            newUser.setHeroAvatar(heroAvatar);
            USER_MAP.put(newUser.getUserId(), newUser);

            // 将用户id附着到channel
            ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

            GameProtocol.UserEntryResult result = builder.build();
            CHANNEL_GROUP.writeAndFlush(result);
        } else if (msg instanceof GameProtocol.WhoElseIsHereCmd) {
            GameProtocol.WhoElseIsHereResult.Builder builder = GameProtocol.WhoElseIsHereResult.newBuilder();
            for (User currUser: USER_MAP.values()) {
                if (null == currUser) {
                    continue;
                }
                GameProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
                userInfoBuilder.setUserId(currUser.getUserId());
                userInfoBuilder.setHeroAvatar(currUser.getHeroAvatar());
                builder.addUserInfo(userInfoBuilder);
            }
            GameProtocol.WhoElseIsHereResult newResult = builder.build();
            ctx.writeAndFlush(newResult);

        } else if (msg instanceof GameProtocol.UserMoveToCmd) {
            Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
            if (null == userId) {
                return;
            }
            GameProtocol.UserMoveToCmd cmd = (GameProtocol.UserMoveToCmd) msg;
            GameProtocol.UserMoveToResult.Builder builder = GameProtocol.UserMoveToResult.newBuilder();
            builder.setMoveUserId(userId);
            builder.setMoveToPosX(cmd.getMoveToPosX());
            builder.setMoveToPosY(cmd.getMoveToPosY());

            GameProtocol.UserMoveToResult result = builder.build();
            CHANNEL_GROUP.writeAndFlush(result);
        }
    }

}
