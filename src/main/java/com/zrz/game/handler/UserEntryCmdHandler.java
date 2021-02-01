package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 用户入场指令处理
 *
 * @author 周瑞忠
 */
public class UserEntryCmdHandler implements ICmdHandler<GameProtocol.UserEntryCmd> {

    private final static Logger logger = LoggerFactory.getLogger(UserEntryCmdHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, GameProtocol.UserEntryCmd msg) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        logger.info("userId = {}", userId);
        if (null == userId) {
            return;
        }
        User existUser = UserManager.getUserById(userId);
        String heroAvatar = existUser.getHeroAvatar();

        GameProtocol.UserEntryResult.Builder builder = GameProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(heroAvatar);

        GameProtocol.UserEntryResult result = builder.build();
        Broadcaster.broadcast(result);
    }
}
