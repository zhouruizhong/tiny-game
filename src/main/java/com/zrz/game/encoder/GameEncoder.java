package com.zrz.game.encoder;

import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.protobuf.GameProtocol;
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
        if (null == msg || !(msg instanceof GeneratedMessageV3)) {
            super.write(ctx, msg, promise);
            return;
        }

        int msgCode = -1;
        if (msg instanceof GameProtocol.UserEntryResult) {
            msgCode = GameProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        } else if (msg instanceof GameProtocol.WhoElseIsHereResult){
            msgCode = GameProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        } else if (msg instanceof GameProtocol.UserMoveToResult) {
            msgCode = GameProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        } else if (msg instanceof GameProtocol.UserQuitResult) {
            msgCode = GameProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        } else {
            logger.info("无法识别消息类型， msgClazz = " + msg.getClass().getName());
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
