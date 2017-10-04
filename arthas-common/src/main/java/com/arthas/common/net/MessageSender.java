package com.arthas.common.net;

/**
 * 
 * @ClassName: MessageSender
 * @Description: Netty消息收发器接口
 * @author dsj
 * @date 2017年9月19日 下午8:37:26
 *
 */
public interface MessageSender {

    public void send(String ip, int port, String message);

}
