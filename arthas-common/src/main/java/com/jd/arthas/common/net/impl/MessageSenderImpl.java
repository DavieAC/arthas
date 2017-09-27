package com.jd.arthas.common.net.impl;

import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.jd.arthas.common.net.MessageSender;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.socket.nio.NioSocketChannel;

@Service("messageSender")
public class MessageSenderImpl implements MessageSender {

    private static final Logger logger = LoggerFactory.getLogger(MessageSenderImpl.class);

    @Override
    public void send(final String ip, final int port, final String message) {

        try {
            Channel channel = new NioSocketChannel();
            ChannelFuture future = channel.connect(new InetSocketAddress(ip, port));

            future.addListener(new ChannelFutureListener() {

                @Override
                public void operationComplete(ChannelFuture future) throws Exception {

                    if (future.isSuccess()) {
                        logger.debug("连接[{}:{}]成功", ip, port);

                        ByteBuf buffer = Unpooled.copiedBuffer(message, Charset.defaultCharset());
                        ChannelFuture wf = future.channel().writeAndFlush(buffer);

                    } else {
                        Throwable cause = future.cause();
                        logger.error("连接[{}:{}]失败", ip, port, cause);
                        throw new ConnectException("连接失败");
                    }
                }
            });

            int i = 1;
        } catch (Exception e) {
            logger.error("发送失败", e);
        }

    }

}
