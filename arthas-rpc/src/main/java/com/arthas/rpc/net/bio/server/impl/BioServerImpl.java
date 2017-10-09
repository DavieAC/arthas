package com.arthas.rpc.net.bio.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.arthas.rpc.net.bio.server.BioServer;
import com.arthas.rpc.net.bio.test.server.BioServerHandler;

/*
 * 暂时只设计为启停一次,不能重复使用 BioServer server = new BioServerImpl(); server.setPort(8088); server.start();
 * // 目前是阻塞的,待优化 server.stop();
 * 
 */
@Service("BioServer")
public class BioServerImpl implements BioServer {

    private static final Logger logger = LoggerFactory.getLogger(BioServerImpl.class);

    private int port;

    private volatile boolean stop;

    private ThreadPoolExecutor taskExecutor = null;

    {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
        taskExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, queue);
    }

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {

        ServerSocket server = null;
        stop = false;
        try {
            server = new ServerSocket(port);
            logger.info("本机启动服务端,监听端口:{}", port);

            Socket socket = null;
            while (!stop) {
                socket = server.accept();
                logger.info("服务端接受到请求,添加链接到线程池进行处理");
                taskExecutor.execute(new BioServerHandler(socket));
                logger.info("线程池正在执行任务的线程数:{},总的线程数:{}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize());
            }

        } catch (Exception e) {
            logger.error("服务端异常", e);
        } finally {

            taskExecutor.shutdownNow();

            if (!taskExecutor.awaitTermination(1, TimeUnit.MINUTES)) {
                logger.error("一分钟内,线程池内线程都没有完全结束,可能有线程");
            }

            if (server != null) {
                try {
                    server.close();
                } catch (IOException e) {
                    logger.error("服务端关闭异常", e);
                }
            }
        }
    }

    @Override
    public void close() {
        stop = true;
    }

    private class circle implements Runnable {

        private ServerSocket server;

        @Override
        public void run() {
            try {
                Socket socket = null;
                while (!stop) {
                    try {
                        socket = server.accept();
                    } catch (IOException e) {
                        logger.error("建立连接失败", e);
                    }
                    logger.info("服务端接受到请求,添加链接到线程池进行处理");
                    taskExecutor.execute(new BioServerHandler(socket));
                    logger.info("线程池正在执行任务的线程数:{},总的线程数:{}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize());
                }
            } catch (Exception e) {
                // catch 住所有异常
                logger.error("bio 循环线程异常", e);
            }
        }

        public ServerSocket getServer() {
            return server;
        }

        public void setServer(ServerSocket server) {
            this.server = server;
        }

    }


}
