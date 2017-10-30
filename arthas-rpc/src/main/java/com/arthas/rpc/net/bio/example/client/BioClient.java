package com.arthas.rpc.net.bio.example.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arthas.common.constant.Constant;

public class BioClient {

    private static final Logger logger = LoggerFactory.getLogger(BioClient.class);

    public void sendMessage(String ip, int port, String message) {

        Socket socket = null;
        BufferedReader reader = null;
        /*
         * <<Netty权威指南>> 使用了PrintWriter 理由有二
         * 1. 在调用 PrintWriter.println(String s) 时,会自动flush
         * 2. 会给入参加入\n 让服务端的 BufferedReader 能够根据这个进行断行读取
         */
        OutputStreamWriter writer = null;
        try {
            socket = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), Constant.UTF_8));
            writer = new OutputStreamWriter(socket.getOutputStream(), Constant.UTF_8);

            Date data = new Date();
            logger.info("客户端发送当前时间戳");
            try {
                writer.write(String.format("%s\n", message));
                writer.flush();
            } catch (Exception e) {
                logger.error("客户端发送失败", e);
                return;
            }

            String response = null;
            try {
                response = reader.readLine();
            } catch (Exception e) {
                logger.error("客户端读取失败", e);
                return;
            }
            logger.info(String.format("客户端收到回应:%s", response));

        } catch (Exception e) {
            logger.error("捕获未知异常", e);
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    logger.error("客户端socket关闭异常", e);
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    logger.error("客户端reader关闭异常", e);
                }
            }
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    logger.error("客户端writer关闭异常", e);
                }
            }
        }

    }

    public static void main(String[] args) {

        String ip = "127.0.0.1";
        int port = 8088;
        BioClient client = new BioClient();

        client.sendMessage(ip, port, "Hello, 你好");
    }

}
