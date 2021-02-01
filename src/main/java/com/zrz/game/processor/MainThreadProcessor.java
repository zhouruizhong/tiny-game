package com.zrz.game.processor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.google.protobuf.GeneratedMessageV3;
import com.zrz.game.factory.CmdHandlerFactory;
import com.zrz.game.handler.ICmdHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 主线程处理器
 *
 * @author zrz
 * @date 2021/1/27 16:11
 */
public final class MainThreadProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MainThreadProcessor.class);

    private static final MainThreadProcessor INSTANCE = new MainThreadProcessor();

    ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("main-thread-pool-%d").build();
    ExecutorService singleThreadPool = new ThreadPoolExecutor(1, 1,0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());

    private MainThreadProcessor(){

    }

    public static MainThreadProcessor getInstance(){
        return INSTANCE;
    }

    public void processor(ChannelHandlerContext ctx, GeneratedMessageV3 msg){
        if (null == ctx || null == msg) {
            return;
        }

        singleThreadPool.submit(()-> {
                Class<?> msgClazz = msg.getClass();

                // 获取指令处理器
                ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msgClazz);
                if (null == cmdHandler) {
                    logger.error("未找到相应的指令处理器， msgClazz = {}", msgClazz.getName());
                    return;
                }

                try{
                    cmdHandler.handler(ctx, cast(msg));
                }catch (Exception e) {
                    logger.error(e.getMessage());
                }
            }
        );
    }

    public void processor(Runnable runnable){
        if (null == runnable) {
            return;
        }
        singleThreadPool.submit(runnable);
    }

    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){
        if (null == msg) {
            return null;
        } else {
            return (TCmd) msg;
        }
    }
}
