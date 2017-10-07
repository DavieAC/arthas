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

@Service("BioMessageServer")
public class BioServerImpl implements BioServer {

    private static final Logger logger = LoggerFactory.getLogger(BioServerImpl.class);

    private int port;

    private volatile boolean stop;

    private ThreadPoolExecutor executor = null;

    {
        BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
        executor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, queue);
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

    @Override
    public void close() {
        stop = true;
    }

}
