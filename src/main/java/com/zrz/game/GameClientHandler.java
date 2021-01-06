package com.zrz.game;

import com.google.protobuf.MessageLite;
import com.zrz.game.protobuf.PersonModel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * @author 周瑞忠
 */
public class GameClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 通道就绪后，发送数据
        ctx.writeAndFlush(build());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        PersonModel.Person person = (PersonModel.Person)msg;
        System.out.println(person.getName());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    public MessageLite build(){
        PersonModel.Person.Builder builder = PersonModel.Person.newBuilder();
        builder.setAge("35");
        builder.setName("天启");
        builder.setId(5);
        return builder.build();
    }
}
