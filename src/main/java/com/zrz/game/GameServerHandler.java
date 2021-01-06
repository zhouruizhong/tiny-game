package com.zrz.game;

import com.google.protobuf.MessageLite;
import com.zrz.game.protobuf.PersonModel;
import io.netty.channel.*;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;

/**
 * @author 周瑞忠
 */
public class GameServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonModel.Person person = (PersonModel.Person)msg;
        // 经过pipeline的各个decoder,到此Person的类型可以确定
        System.out.println(person.getName());
        // 发送数据之后，我们手动关闭channel,这个关闭是异步的，当数据发送完毕后执行
        ChannelFuture future = ctx.writeAndFlush(build());
        future.addListener(ChannelFutureListener.CLOSE);
    }

    /**
     * 构建一个Protobuf实例，测试
     *
     * @return MessageLite
     */
    public MessageLite build(){
        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setAge("34");
        builder.setName("王五");
        builder.setId(2);
        return builder.build();
    }
}
