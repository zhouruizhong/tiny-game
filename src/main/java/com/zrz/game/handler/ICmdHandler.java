package com.zrz.game.handler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

/**
 * 指令处理接口
 * @author 周瑞忠
 */
public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 处理指令
     *
     * @param ctx 上下文
     * @param tCmd 指令
     */
    void handler(ChannelHandlerContext ctx, TCmd tCmd);
}
