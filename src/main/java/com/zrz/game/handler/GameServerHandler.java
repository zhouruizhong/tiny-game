package com.zrz.game.handler;

import com.google.protobuf.MessageLite;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 周瑞忠
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("收到客户端消息， msgClazz =" + msg.getClass().getName() + ", msg = " + msg);
    }

    /**
     * 构建一个Protobuf实例，测试
     *
     * @return MessageLite
     */
    public MessageLite build(){
        return null;
    }
}
