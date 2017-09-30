package com.jd.arthas.common.test.client;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class EchoClient {

    private static final Logger logger = LoggerFactory.getLogger(EchoClient.class);

    private final String host;

    private final int port;

    public EchoClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group);
            b.channel(NioSocketChannel.class);
            b.remoteAddress(new InetSocketAddress(host, port));
            b.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                public void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new EchoClientHandler());
                }
            });

            // connect() TCP / bind() UDP
            ChannelFuture f = null;
            for (int i = 1; i <= 10; i++) {

                logger.info("开始发送第:{}次请求", i);
                f = b.connect().sync();
                logger.info("第:{}次请求接受到了回应", i);
                // 线程 wait 直到 连接关闭
                f.channel().closeFuture().sync();
                logger.info("发送请求结束");
            }

        } finally {
            group.shutdownGracefully().sync();
        }
    }

    public static void main(String[] args) throws Exception {

        String host = "127.0.0.1";
        int port = 8088;
        new EchoClient(host, port).start();
    }
}
