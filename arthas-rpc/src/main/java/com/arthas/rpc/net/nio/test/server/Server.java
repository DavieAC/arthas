package com.arthas.rpc.net.nio.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Server {

    private static final Logger logger = LoggerFactory.getLogger(Server.class);

    private ThreadPoolExecutor executor = null;

    {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
        executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, queue);
    }

    public void startLocalBioServer(int port) {

        ServerSocket server = null;

        try {
            server = new ServerSocket(port);
            logger.info("本机启动服务端,监听端口:{}", port);

            Socket socket = null;
            while (true) {
                socket = server.accept();
                logger.info("服务端接受到请求");
                executor.execute(new ServerHandler(socket));
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

    public static void main(String[] args) {

        int port = 8088;
        Server server = new Server();
        server.startLocalBioServer(port);
    }

}
