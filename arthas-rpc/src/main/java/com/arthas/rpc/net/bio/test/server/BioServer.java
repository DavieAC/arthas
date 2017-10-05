package com.arthas.rpc.net.bio.test.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BioServer {

    private static final Logger logger = LoggerFactory.getLogger(BioServer.class);

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
                logger.info("服务端接受到请求,添加链接到线程池进行处理");
                executor.execute(new BioServerHandler(socket));
                logger.info("线程池正在执行任务的线程数:{},总的线程数:{}", executor.getActiveCount(), executor.getPoolSize());
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
        BioServer server = new BioServer();
        server.startLocalBioServer(port);
    }

}
