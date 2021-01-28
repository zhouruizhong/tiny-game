package com.zrz.game;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import com.zrz.game.encoder.GameEncoder;
import com.zrz.game.protobuf.GameProtocol;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 消息识别器
 *
 * @author 周瑞忠
 */
public final class MessageRecognizer {

    private static final Logger logger = LoggerFactory.getLogger(GameEncoder.class);

    private static final Map<Integer, GeneratedMessageV3> MSG_CODE_AND_MSG_BODY_MAP = new HashMap<>(16);
    private static final Map<Class<?>, Integer> MSG_CLAZZ_AND_MSG_CODE_MAP = new HashMap<>(16);

    /**
     * 私有化构造器
     */
    private MessageRecognizer(){

    }

    /**
     * 初始化
     */
    public static void init(){
        MSG_CODE_AND_MSG_BODY_MAP.put(GameProtocol.MsgCode.USER_ENTRY_CMD_VALUE, GameProtocol.UserEntryCmd.getDefaultInstance());
        MSG_CODE_AND_MSG_BODY_MAP.put(GameProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE, GameProtocol.WhoElseIsHereCmd.getDefaultInstance());
        MSG_CODE_AND_MSG_BODY_MAP.put(GameProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE, GameProtocol.UserMoveToCmd.getDefaultInstance());

        MSG_CLAZZ_AND_MSG_CODE_MAP.put(GameProtocol.UserEntryResult.class, GameProtocol.MsgCode.USER_ENTRY_RESULT_VALUE);
        MSG_CLAZZ_AND_MSG_CODE_MAP.put(GameProtocol.WhoElseIsHereResult.class, GameProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE);
        MSG_CLAZZ_AND_MSG_CODE_MAP.put(GameProtocol.UserMoveToResult.class, GameProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE);
        MSG_CLAZZ_AND_MSG_CODE_MAP.put(GameProtocol.UserQuitResult.class, GameProtocol.MsgCode.USER_QUIT_RESULT_VALUE);
    }

    /**
     * 利用反射初始化
     */
    public static void start(){
        Class<?> [] innerClazzArray = GameProtocol.class.getDeclaredClasses();
        for (Class<?> innerClazz : innerClazzArray) {
            if (!GeneratedMessageV3.class.isAssignableFrom(innerClazz)) {
                continue;
            }
            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();

            for (GameProtocol.MsgCode msgCode : GameProtocol.MsgCode.values()) {
                String nameMsgCode = msgCode.name();
                nameMsgCode = nameMsgCode.replaceAll("_", "");
                nameMsgCode = nameMsgCode.toLowerCase();

                if (!nameMsgCode.startsWith(clazzName)) {
                    continue;
                }

                try{
                    Object obj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);

                    logger.info("{} <==> {}", innerClazz.getName(), msgCode.getNumber());

                    MSG_CODE_AND_MSG_BODY_MAP.put(msgCode.getNumber(), (GeneratedMessageV3) obj);
                    MSG_CLAZZ_AND_MSG_CODE_MAP.put(innerClazz, msgCode.getNumber());
                }catch (Exception e) {
                    e.printStackTrace();
                    logger.error(e.getMessage(), e);
                }
            }
        }
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

    public static Message.Builder genBuilderByMsgCode(int msgCode){
        if (msgCode < 0) {
            return null;
        }
        GeneratedMessageV3 messageV3 = MSG_CODE_AND_MSG_BODY_MAP.get(msgCode);
        if (null == messageV3) {
            return null;
        }
        return messageV3.newBuilderForType();
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

    public static int findMsgCodeByMsgClazz(Class<?> msg){
        Integer msgCode = MSG_CLAZZ_AND_MSG_CODE_MAP.get(msg);
        if (null == msgCode) {
            return -1;
        }
        return msgCode;
    }

}
