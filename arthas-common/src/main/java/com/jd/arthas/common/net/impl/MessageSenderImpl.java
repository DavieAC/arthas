package com.jd.arthas.common.net.impl;

import com.jd.arthas.common.net.MessageSender;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

public class MessageSenderImpl implements MessageSender {

    @Override
    public void send(String ip, int port, String message) {

        EventLoopGroup group = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap();
        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}
