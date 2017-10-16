package com.arthas.rpc.net.bio.server.impl;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arthas.common.thread.AbstractTerminalThread;
import com.arthas.common.utils.threadUtils.ThreadUtil;
import com.arthas.rpc.net.bio.server.BioServer;
import com.arthas.rpc.net.bio.server.handler.BioServerHandler;

public class BioServerImpl implements BioServer {

    private static final Logger logger = LoggerFactory.getLogger(BioServerImpl.class);

    private CircleTask task;

    private Integer port;

    private volatile Boolean running = false;

    @Override
    public void setPort(int port) {
        this.port = port;
    }

    @Override
    public void start() throws Exception {

        if (port == null) {
            throw new Exception("端口未设置,不能启动服务器");
        }

        if (running == true) {
            throw new Exception("不能重复启动同一个服务器");
        }

        synchronized (running) {
            if (running == false) {
                task = new CircleTask();
                task.start();
                logger.info("本机启动服务端,监听端口:{}", port);
                running = true;
            }
        }
    }

    @Override
    public void stop() {
        if (task != null) {
            task.shutdown();
            task.interrupt();
        }
    }

    private class CircleTask extends AbstractTerminalThread {

        private volatile boolean stop = false;

        private ServerSocket server = null;

        private List<AbstractTerminalThread> taskList = new ArrayList<AbstractTerminalThread>();

        private ThreadPoolExecutor taskExecutor = null;

        {
            BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(10);
            taskExecutor = new ThreadPoolExecutor(10, 20, 60, TimeUnit.SECONDS, queue);
        }

        @Override
        public void run() {

            try {
                server = new ServerSocket(port);

                Socket socket = null;
                while (!stop) {
                    try {
                        socket = server.accept();
                    } catch (Exception e) {
                        logger.error("建立连接失败", e);
                        continue;
                    }

                    AbstractTerminalThread curTask = new BioServerHandler(socket);
                    taskList.add(curTask);
                    taskExecutor.execute(new BioServerHandler(socket));
                }
            } catch (SocketException e) {
                logger.error("bio 服务端关闭", e);
            } catch (Exception e) {
                logger.error("bio 循环线程捕获异常", e);
            } finally {

                // 关闭这个服务本身
                if (server != null) {
                    try {
                        server.close();
                        server = null;
                    } catch (IOException e) {
                        logger.error("服务端关闭异常", e);
                    }
                }

                // 尝试终止所有在跑的处理任务
                ThreadUtil.terminalAllThread(taskList);
                taskList.clear();

                // 尝试终止所有的线程
                taskExecutor.shutdownNow();
            }
        }

        public void shutdown() {
            this.stop = true;
        }

        /**
         * 
         * @Title: keepShowPoolState
         * @Description: 调用这个方法之后,开始定时打印线程池信息(尚未实现)
         * @throws
         */
        private void keepShowPoolState() {
            logger.info("线程池正在执行任务的线程数:{},总的线程数:{}", taskExecutor.getActiveCount(), taskExecutor.getPoolSize());
        }
    }

}
