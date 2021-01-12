package com.zrz.game.encoder;

import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.MessageRecognizer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息编码
 * @author 周瑞忠
 */
public class GameEncoder extends ChannelOutboundHandlerAdapter {

    private static final Logger logger = LoggerFactory.getLogger(GameEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (!(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        int msgCode = MessageRecognizer.getMsgCodeByMsgClazz(msg);
        if (msgCode <= -1) {
            logger.error("无法识别的消息，msgClazz = {}", msg.getClass().getName());
            return;
        }

        byte[] byteArray = ((GeneratedMessageV3)msg).toByteArray();
        ByteBuf byteBuf = ctx.alloc().buffer();
        byteBuf.writeShort((short)0);
        byteBuf.writeShort((short)msgCode);
        byteBuf.writeBytes(byteArray);

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(byteBuf);
        super.write(ctx, frame, promise);
    }
}
