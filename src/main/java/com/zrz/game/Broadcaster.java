package com.zrz.game;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * 广播类
 *
 * @author 周瑞忠
 */
public final class Broadcaster {

    /**
     * 客户端信道数组，一定要使用static 否则无法实现群发
     */
    private static final ChannelGroup CHANNEL_GROUP = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){

    }

    /**
     * 添加信道
     *
     * @param channel 信道
     */
    public static void addChannel(Channel channel){
        CHANNEL_GROUP.add(channel);
    }

    /**
     * 移除信道
     *
     * @param channel 信道
     */
    public static void removeChannel(Channel channel){
        CHANNEL_GROUP.remove(channel);
    }

    /**
     * 广播消息
     *
     * @param msg 消息内容
     */
    public static void broadcast(Object msg){
        if (null == msg) {
            return;
        }
        CHANNEL_GROUP.writeAndFlush(msg);
    }
}
