package com.zrz.game;

import com.zrz.game.protobuf.PersonModel;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

import java.net.InetSocketAddress;

public class GameClient {

  public static void main(String[] args) {
      EventLoopGroup workGroup = new NioEventLoopGroup();
      try{
          Bootstrap bootstrap = new Bootstrap();
          bootstrap.group(workGroup)
                  .channel(NioSocketChannel.class)
                  .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 1024)
                  .handler(new ChannelInitializer<SocketChannel>() {

                      @Override
                      protected void initChannel(SocketChannel socketChannel) throws Exception {
                          System.out.println("initChannel");
                          ChannelPipeline channelPipeline = socketChannel.pipeline();
                          channelPipeline.addLast(new ProtobufVarint32FrameDecoder())
                                  .addLast(new ProtobufDecoder(PersonModel.Person.getDefaultInstance()))
                                  .addLast(new ProtobufVarint32LengthFieldPrepender())
                                  .addLast(new ProtobufEncoder())
                                  .addLast(new GameClientHandler());
                      }
                  });
          ChannelFuture future = bootstrap.connect(new InetSocketAddress("127.0.0.1",18080));
          System.out.println("begin");
          future.channel().closeFuture().sync();
          System.out.println("closed");
      }catch (Exception e) {
            e.printStackTrace();
      }finally{
          workGroup.shutdownGracefully();
      }
  }
}
