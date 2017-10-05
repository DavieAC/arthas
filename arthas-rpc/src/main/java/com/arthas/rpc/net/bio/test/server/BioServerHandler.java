package com.arthas.rpc.net.bio.test.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BioServerHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(BioServerHandler.class);

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
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new OutputStreamWriter(socket.getOutputStream(), "UTF-8");

            String body = null;
            while ((body = reader.readLine()) != null) {
                if (body.equals("exit")) {
                    logger.info("读取到了退出命令");
                    break;
                }
                logger.info("本次读取到了:{}", body);

                writer.write(String.format("服务端接受到了传入参数:%s\n", body));
                writer.flush();
            }
        } catch (Exception e) {
            logger.error("读取链接传输内容出错", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                    writer.close();
                } catch (IOException e) {
                    logger.error("链接reader关闭失败", e);
                }
            }
        }

        logger.info("本次链接完成");
    }

}
