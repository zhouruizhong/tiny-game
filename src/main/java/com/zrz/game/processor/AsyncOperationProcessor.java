package com.zrz.game.processor;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 异步操作处理器
 *
 * @author zrz
 * @date 2021/1/28 22:57
 */
public final class AsyncOperationProcessor {

    private static final AsyncOperationProcessor INSTANCE = new AsyncOperationProcessor();

    private final static Logger logger = LoggerFactory.getLogger(AsyncOperationProcessor.class);

    private final ExecutorService [] executorServiceArray = new ExecutorService[8];


    private AsyncOperationProcessor(){
        for (int i = 0; i < executorServiceArray.length; i++) {
            ThreadFactory namedThreadFactory = new ThreadFactoryBuilder().setNameFormat("async-pool"+i+"-%d").build();
            executorServiceArray[i]  = new ThreadPoolExecutor(8, 20,0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
        }
    }

    public static AsyncOperationProcessor getInstance(){
        return INSTANCE;
    }

    public void process(IAsyncOperation asyncOperation){
        if (null == asyncOperation) {
            return;
        }

        // 根据bindId 获取线程索引
        int bindId = Math.abs(asyncOperation.bindId());
        int index = bindId % executorServiceArray.length;

        executorServiceArray[index].submit(() -> {
            try{
                // 执行异步操作
                asyncOperation.doAsync();

                // 返回主线程执行完成逻辑
                MainThreadProcessor.getInstance().processor(asyncOperation::doFinish);
            } catch (Exception e) {
                logger.error(e.getMessage(), e);
            }

        });
    }

}
