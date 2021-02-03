package com.zrz.game.factory;

import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.handler.*;
import com.zrz.game.protobuf.GameProtocol;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 指令处理工厂类
 *
 * @author 周瑞忠
 */
public final class CmdHandlerFactory {

    private static final Logger logger = LoggerFactory.getLogger(CmdHandlerFactory.class);

    private static final Map<Class<?>, ICmdHandler<? extends GeneratedMessageV3>> handlerMap = new HashMap<>(16);

    private CmdHandlerFactory(){

    }

    public static void init(){
        handlerMap.put(GameProtocol.UserEntryCmd.class, new UserEntryCmdHandler());
        handlerMap.put(GameProtocol.WhoElseIsHereCmd.class, new WhoElseIsHereCmdHandler());
        handlerMap.put(GameProtocol.UserMoveToCmd.class, new UserMoveToCmdHandler());
        handlerMap.put(GameProtocol.UserAttackCmd.class, new UserAttackCmdHandler());
        handlerMap.put(GameProtocol.UserLoginCmd.class, new UserLoginCmdHandler());
        handlerMap.put(GameProtocol.GetRankCmd.class, new GetRankCmdHandler());
    }

    /**
     * 利用Reflections反射实现动态添加处理器类
     */
    public static void start(){
        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages("com.zrz.game.handler")
                .addScanners(new SubTypesScanner())
                .addScanners(new MethodAnnotationsScanner()));

        Set<Class<? extends ICmdHandler>> classSet = reflections.getSubTypesOf(ICmdHandler.class);
        for (Class<?> clazz : classSet) {
            Method[] methods = clazz.getDeclaredMethods();
            Class<?> msgType = null;
            for (Method method : methods) {
                String methodName = method.getName();
                if (!"handler".equals(methodName)) {
                    continue;
                }

                Class<?> [] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length >= 2 && GeneratedMessageV3.class.isAssignableFrom(parameterTypes[1])) {
                    msgType = parameterTypes[1];
                    break;
                }
            }
            if (null == msgType) {
                continue;
            }
            try{
                ICmdHandler<?> newHandler = (ICmdHandler<?>)clazz.newInstance();
                handlerMap.put(msgType, newHandler);
            }catch (Exception e) {
                logger.error(e.getMessage(), e);
            }
        }
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
