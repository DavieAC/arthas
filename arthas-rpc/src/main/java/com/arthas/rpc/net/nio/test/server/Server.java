package com.arthas.rpc.net.nio.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    public static void main(String[] args) {

        int port = 8088;

        ServerSocket server = null;
        try {
            server = new ServerSocket(port);
            logger.info("本机启动服务端,监听端口:{}", port);

            Socket socket = null;
            while (true) {
                socket = server.accept();

                logger.info("接受到请求");
                // 替换为线程池实现
                new Thread(new ServerHandler(socket)).start();

            }

        } catch (Exception e) {
            logger.error("服务端异常", e);
        } finally {
            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("服务端关闭异常", e);
                }
            }
        }
    }

}
