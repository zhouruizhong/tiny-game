package com.zrz.game;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author 周瑞忠
 */
public class GameServer {

  public static void main(String[] args) throws Exception {
    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup();
    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workGroup)
      .channel(NioServerSocketChannel.class)
      .handler(new LoggingHandler(LogLevel.INFO))
      .childHandler(new ChannelInitializer<SocketChannel>() {

        @Override
        protected void initChannel(SocketChannel socketChannel) throws Exception {
          ChannelPipeline channelPipeline = socketChannel.pipeline();
          channelPipeline.addLast(new HttpServerCodec());
          channelPipeline.addLast(new HttpObjectAggregator(65535));
          channelPipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
          channelPipeline.addLast(new GameServerHandler());
        }
      });
    try{
      ChannelFuture channelFuture = bootstrap.bind(12345).sync();
      if (channelFuture.isSuccess()) {
        System.out.println("服务器启动成功。。。");
      }
      channelFuture.channel().closeFuture().sync();
    }finally{
      bossGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }
}
