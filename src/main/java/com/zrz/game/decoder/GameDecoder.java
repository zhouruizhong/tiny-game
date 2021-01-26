package com.zrz.game.decoder;

import com.google.protobuf.Message;
import com.zrz.game.MessageRecognizer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * 消息解码
 * @author 周瑞忠
 */
public class GameDecoder extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof BinaryWebSocketFrame)) {
            return;
        }

        BinaryWebSocketFrame binaryWebSocketFrame = (BinaryWebSocketFrame) msg;
        ByteBuf byteBuf = binaryWebSocketFrame.content();

        // 读取消息的长度
        byteBuf.readShort();
        // 读取消息的编号
        int msgCode = byteBuf.readShort();

        Message.Builder msgBuilder = MessageRecognizer.genBuilderByMsgCode(msgCode);

        // 拿到真实的字节数组 并打印
        byte [] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        assert msgBuilder != null;
        msgBuilder.clear();

        msgBuilder.mergeFrom(msgBody);

        Message newMsg = msgBuilder.build();
        ctx.fireChannelRead(newMsg);
    }
}

