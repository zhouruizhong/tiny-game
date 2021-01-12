package com.zrz.game.handler;

import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author 周瑞忠
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameProtocol.WhoElseIsHereCmd> {

    @Override
    public void handler(ChannelHandlerContext ctx, GameProtocol.WhoElseIsHereCmd msg) {
        GameProtocol.WhoElseIsHereResult.Builder builder = GameProtocol.WhoElseIsHereResult.newBuilder();
        for (User currUser: UserManager.listUser()) {
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
    }
}
