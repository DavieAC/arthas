package com.jd.arthas.common.net.impl;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.jd.arthas.common.net.MessageSender;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;



public class MessageSenderImpl implements MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);

    @Override
    public void send(final String ip, final int port, String message) {

        Channel channel = new NioSocketChannel();

        ChannelFuture future = channel.connect(new InetSocketAddress("127.0.0.1", 8088));

        future.addListener(new ChannelFutureListener() {

            @Override
            public void operationComplete(ChannelFuture future) throws Exception {

                if (future.isSuccess()) {
                    logger.debug("连接[{}:{}]成功", ip, port);
                } else {
                    logger.error("连接[{}:{}]失败", ip, port);
                    throw new ConnectException("连接失败");
                }

            }

        });

    }

}
