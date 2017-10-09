package com.arthas.rpc.net.bio.server.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arthas.common.constant.Constant;
import com.arthas.common.thread.TerminalTask;

public class BioServerHandler implements TerminalTask, Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BioServerHandler.class);

    private volatile boolean stop;

    private Socket socket;

    public BioServerHandler(Socket socket) {
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

        // 处理传入参数
        BufferedReader reader = null;
        OutputStreamWriter writer = null;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Constant.UTF_8));
            writer = new OutputStreamWriter(socket.getOutputStream(), Constant.UTF_8);

            String info;
            while (stop == false && (info = reader.readLine()) != null) {

                if (info.equals("exit")) {
                    logger.info("读取到了退出命令");
                    break;
                }
                logger.info("本次读取到了:{}", info);

                writer.write(String.format("服务端接受到了传入参数:%s\n", info));
                writer.flush();
            }
        } catch (Exception e) {
            logger.error("读取链接传输内容出错", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("链接reader关闭失败", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("链接writer关闭失败", e);
                }
            }
        }
        logger.info("本次链接完成");
    }

    @Override
    public void stop() {
        stop = true;
    }

}
