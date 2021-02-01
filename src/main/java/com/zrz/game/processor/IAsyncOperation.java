package com.zrz.game.processor;

/**
 * 异步操作接口
 *
 * @author 周瑞忠
 */
public interface IAsyncOperation {

    /**
     * 获取绑定id
     * @return 绑定id
     */
    default int bindId(){
        return 0;
    }

    /**
     * 执行异步操作
     */
    void doAsync();

    /**
     * 执行完成逻辑
     */
    default void doFinish() {

    };
}
