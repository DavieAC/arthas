package com.arthas.rpc.net.nio.test.server;

import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    private Socket socket;

    public ServerHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InetAddress address = null;
        try {
            address = this.socket.getInetAddress();
        } catch (Exception e) {
            logger.error("获取链接ip地址出错", e);
            return;
        }
        String ip = address.getHostAddress();
        logger.info("接收到链接,ip:{}", ip);
    }

}
