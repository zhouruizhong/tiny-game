package com.zrz.game;

import com.google.protobuf.Message;
import com.zrz.game.encoder.GameEncoder;
import com.zrz.game.protobuf.GameProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 消息识别器
 *
 * @author 周瑞忠
 */
public final class MessageRecognizer {

    private static final Logger logger = LoggerFactory.getLogger(GameEncoder.class);

    /**
     * 私有化构造器
     */
    private MessageRecognizer(){

    }

    public static Message.Builder getBuilderByMsgCode(int msgCode){
        Message.Builder msgBuilder = null;
        switch (msgCode) {
            case GameProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                msgBuilder = GameProtocol.UserEntryCmd.newBuilder();
                break;
            case GameProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                msgBuilder = GameProtocol.WhoElseIsHereCmd.newBuilder();
                break;
            case GameProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                msgBuilder = GameProtocol.UserMoveToCmd.newBuilder();
                break;
            default:
                logger.error("无法识别消息， msgCode = {}", msgCode);
        }
        return msgBuilder;
    }

    public static int getMsgCodeByMsgClazz(Object msg){
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
        }
        return msgCode;
    }

}
