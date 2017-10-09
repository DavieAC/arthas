package com.arthas.rpc.net.bio.client.impl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.arthas.common.constant.Constant;
import com.arthas.rpc.net.bio.client.BioClient;

@Service("BioClient")
public class BioClientImpl implements BioClient {

    private static final Logger logger = LoggerFactory.getLogger(BioClientImpl.class);

    @Override
    public void sendMessage(String ip, int port, String message) {
        
        Socket socket = null;
        BufferedReader reader = null;
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

    @Override
    public String sendMessageWithResponse(String ip, int port, String message) {
        
        Socket socket = null;
        BufferedReader reader = null;
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
                return null;
            }

            String response = null;
            try {
                response = reader.readLine();
            } catch (Exception e) {
                logger.error("客户端读取失败", e);
                return null;
            }
            
            logger.info(String.format("客户端收到回应:%s", response));
            return response;
        } catch (Exception e) {
            logger.error("捕获未知异常", e);
            return null;
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

}
