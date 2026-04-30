package org.example;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.example.handlers.KvStoreHandler;
import org.example.store.Command.CommandDispatcher;
import org.example.store.engine.FullScanExpirationScheduler;
import org.example.store.engine.SimpleStoreEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Bedis {
    private final Logger logger = LoggerFactory.getLogger(Bedis.class);
    private int port;
    private ServerBootstrap serverBootstrap;
    private final EventLoopGroup bossGroup
            = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    private final EventLoopGroup workerGroup
            = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    public static BedisBuilder builder(){
        return new BedisBuilder();
    }
    protected Bedis(int port) {
        this.port = port;
    }
    public void run() {
        try{
            setBootstrap();
            ChannelFuture future = serverBootstrap.bind(port).sync();
            future.channel().closeFuture().sync();
            logger.info("Bedis is listen on port: {}", port);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            stop();
        }
    }

    private void setBootstrap() {
        serverBootstrap = new ServerBootstrap();
        final SimpleStoreEngine storeEngine = new SimpleStoreEngine();
        final FullScanExpirationScheduler scheduler = new FullScanExpirationScheduler(storeEngine);
        scheduler.start();
        serverBootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        CommandDispatcher commandDispatcher = new CommandDispatcher(storeEngine);
                        socketChannel.pipeline()
                                .addLast(new HttpServerCodec())
                                .addLast(new HttpObjectAggregator(65535))
                                .addLast(new WebSocketServerProtocolHandler("/ws"))
                                .addLast(new CommandDecoder())
                                .addLast(new KvStoreHandler(commandDispatcher))
                                ;
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);
    }

    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
    }
}
