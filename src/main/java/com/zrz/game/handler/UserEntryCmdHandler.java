package com.zrz.game.handler;

import com.zrz.game.Broadcaster;
import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 * 用户入场指令处理
 *
 * @author 周瑞忠
 */
public class UserEntryCmdHandler implements ICmdHandler<GameProtocol.UserEntryCmd> {

    @Override
    public void handler(ChannelHandlerContext ctx, GameProtocol.UserEntryCmd msg) {
        int userId = msg.getUserId();
        String heroAvatar = msg.getHeroAvatar();

        GameProtocol.UserEntryResult.Builder builder = GameProtocol.UserEntryResult.newBuilder();
        builder.setUserId(userId);
        builder.setHeroAvatar(heroAvatar);

        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setHeroAvatar(heroAvatar);
        UserManager.addUser(newUser);

        // 将用户id附着到channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        GameProtocol.UserEntryResult result = builder.build();
        Broadcaster.broadcast(result);
    }
}
