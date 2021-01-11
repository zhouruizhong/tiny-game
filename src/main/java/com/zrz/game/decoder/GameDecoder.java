package com.zrz.game.decoder;

import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.protobuf.GameProtocol;
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

        // 拿到真实的字节数组 并打印
        byte [] msgBody = new byte[byteBuf.readableBytes()];
        byteBuf.readBytes(msgBody);

        GeneratedMessageV3 cmd = null;
        switch (msgCode) {
            case GameProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = GameProtocol.UserEntryCmd.parseFrom(msgBody);
                break;
            case GameProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                cmd = GameProtocol.WhoElseIsHereCmd.parseFrom(msgBody);
                break;
            case GameProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                cmd = GameProtocol.UserMoveToCmd.parseFrom(msgBody);
                break;
            default:
        }

        if (null != cmd) {
            ctx.fireChannelRead(cmd);
        }
    }
}

