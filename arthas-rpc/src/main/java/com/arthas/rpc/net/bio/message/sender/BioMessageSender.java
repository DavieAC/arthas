package com.arthas.rpc.net.bio.message.sender;

/**
 * 
 * @ClassName: BioMessageSender
 * @Description: 使用Java bio实现的消息发送器
 * @author DavieAC
 * @date 2017年10月5日 下午10:06:53
 *
 */
public interface BioMessageSender {

    /**
     * 
     * @Title: sendMessage
     * @Description: 给指定socket发送String
     * @param ip
     * @param port
     * @param message
     * @throws
     */
    public void sendMessage(String ip, int port, String message);

    /**
     * 
     * @Title: sendMessageWithResponse
     * @Description: 给指定socket发送String并且接受回应
     * @param ip
     * @param port
     * @param message
     * @return
     * @throws
     */
    public String sendMessageWithResponse(String ip, int port, String message);

}
