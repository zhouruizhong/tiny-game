package com.zrz.game.factory;

import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.handler.ICmdHandler;
import com.zrz.game.handler.UserEntryCmdHandler;
import com.zrz.game.handler.UserMoveToCmdHandler;
import com.zrz.game.handler.WhoElseIsHereCmdHandler;
import com.zrz.game.protobuf.GameProtocol;

import java.util.HashMap;
import java.util.Map;

/**
 * 指令处理工厂类
 *
 * @author 周瑞忠
 */
public final class CmdHandlerFactory {

    private static final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> handlerMap = new HashMap<>(16);

    private CmdHandlerFactory(){

    }

    public static void init(){
        handlerMap.put(GameProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        handlerMap.put(GameProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        handlerMap.put(GameProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
    }

    /**
     * 工厂方法
     *
     * @param msg 消息
     * @return 消息处理器
     */
    public static ICmdHandler<? extends GeneratedMessageV3> create(Object msg){
        ICmdHandler<? extends GeneratedMessageV3> cmdHandler = null;
        if (msg instanceof GameProtocol.UserEntryCmd) {
            cmdHandler = new UserEntryCmdHandler();
        } else if (msg instanceof GameProtocol.WhoElseIsHereCmd) {
            cmdHandler = new WhoElseIsHereCmdHandler();
        } else if (msg instanceof GameProtocol.UserMoveToCmd) {
            cmdHandler = new UserMoveToCmdHandler();
        }
        return cmdHandler;
    }

    /**
     * 使用map 获取实例
     *
     * @param msgClazz 消息类对象
     * @return 消息处理器
     */
    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz){
        if (null == msgClazz) {
            return null;
        }
        return handlerMap.get(msgClazz);
    }
}
