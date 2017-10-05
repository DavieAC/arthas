package com.arthas.rpc.net.nio.test.server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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

        // 打印ip信息
        InetAddress address = this.socket.getInetAddress();
        if (address == null) {
            logger.error("获取链接ip地址出错");
            return;
        }
        String ip = address.getHostAddress();
        logger.info("接收到链接,ip:{}", ip);

        // 打印出所有的传入参数
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String body = null;
            while ((body = reader.readLine()) != null) {
                if (body.equals("exit")) {
                    logger.info("读取到了退出命令");
                    break;
                }
                logger.info("本次读取到了:{}", body);
            }
        } catch (Exception e) {
            logger.error("读取链接传输内容出错", e);
        }

        logger.info("本次链接完成");
    }

}
