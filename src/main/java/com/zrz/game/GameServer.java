package com.zrz.game;

import com.zrz.game.encoder.GameEncoder;
import com.zrz.game.factory.CmdHandlerFactory;
import com.zrz.game.factory.MysqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zrz.game.decoder.GameDecoder;
import com.zrz.game.handler.GameServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author 周瑞忠
 */
public class GameServer {

  private static final Logger logger = LoggerFactory.getLogger(GameServer.class);

  public static void main(String[] args) throws Exception {
    // 如果使用map存储处理器类，则需要初始化处理器map
    CmdHandlerFactory.init();
    MessageRecognizer.start();
    MysqlSessionFactory.init();

    EventLoopGroup bossGroup = new NioEventLoopGroup(1);
    EventLoopGroup workGroup = new NioEventLoopGroup();

    ServerBootstrap bootstrap = new ServerBootstrap();
    bootstrap.group(bossGroup, workGroup)
        .channel(NioServerSocketChannel.class)
        .handler(new LoggingHandler(LogLevel.INFO))
        .childOption(ChannelOption.TCP_NODELAY, true)
        .childHandler(
            new ChannelInitializer<SocketChannel>() {

              @Override
              protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline channelPipeline = socketChannel.pipeline();
                channelPipeline.addLast(new HttpServerCodec());
                channelPipeline.addLast(new HttpObjectAggregator(65535));
                channelPipeline.addLast(new WebSocketServerProtocolHandler("/websocket"));
                // 自定义的解码器
                channelPipeline.addLast(new GameDecoder());
                channelPipeline.addLast(new GameEncoder());
                channelPipeline.addLast(new GameServerHandler());
                /*channelPipeline.addLast(new ProtobufVarint32FrameDecoder())
                        .addLast(new ProtobufDecoder(PersonModel.Person.getDefaultInstance()))
                        .addLast(new ProtobufVarint32LengthFieldPrepender())
                        .addLast(new ProtobufEncoder())
                        .addLast(new GameServerHandler());*/
              }
            });
    try{
      ChannelFuture channelFuture = bootstrap.bind(12345).sync();
      if (channelFuture.isSuccess()) {
        logger.info("服务器启动成功。。。");
      }
      channelFuture.channel().closeFuture().sync();
    }finally{
      bossGroup.shutdownGracefully();
      workGroup.shutdownGracefully();
    }
  }
}
