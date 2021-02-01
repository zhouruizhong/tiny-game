package com.zrz.game.handler;

import com.zrz.game.login.model.UserModel;
import com.zrz.game.login.service.LoginService;
import com.zrz.game.model.User;
import com.zrz.game.model.UserManager;
import com.zrz.game.processor.AsyncOperationProcessor;
import com.zrz.game.protobuf.GameProtocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author zrz
 * @date 2021/1/28 10:00
 */
public class UserLoginCmdHandler implements ICmdHandler<GameProtocol.UserLoginCmd> {

    private static final Logger logger = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handler(ChannelHandlerContext ctx, GameProtocol.UserLoginCmd cmd) {
        String username = cmd.getUserName();
        String password = cmd.getPassword();
        logger.info("username = {}， password = {}", username, password);

        AsyncOperationProcessor.getInstance().process(() -> {
            LoginService.getInstance().login(username, password, (userModel) -> {
                if (null == userModel) {
                    logger.error("登录失败！用户名是={}", username);
                    return null;
                }

                logger.info("当前线程 = {}", Thread.currentThread().getName());

                logger.info("用户登录成功，userId = {}, username = {}", userModel.getUserId(), userModel.getUserName());

                // 新建用户
                User user = new User();
                user.setUserId(userModel.getUserId());
                user.setHeroAvatar(userModel.getHeroAvatar());
                user.setCurrHp(100);
                // 将用户加入管理器
                UserManager.addUser(user);

                ctx.channel().attr(AttributeKey.valueOf("userId")).set(userModel.getUserId());

                // 构建结果
                GameProtocol.UserLoginResult.Builder builder = GameProtocol.UserLoginResult.newBuilder();
                builder.setUserId(userModel.getUserId());
                builder.setUserName(userModel.getUserName());
                builder.setHeroAvatar(userModel.getHeroAvatar());

                // 发送
                GameProtocol.UserLoginResult result = builder.build();
                ctx.writeAndFlush(result);
                return null;
            });
        });

    }
}
