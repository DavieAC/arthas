package com.jd.arthas.service.net;

/**
 * 
 * @ClassName: MessageSender
 * @Description: 客户端之前的消息传递器
 * @author dsj
 * @date 2017年9月19日 下午8:33:58
 *
 */
public interface MessageSender {

    /**
     * 
     * @Title: sendMaster
     * @Description: 发送消息到Master
     * @throws
     */
    public void sendMaster();

    /**
     * 
     * @Title: sendFollower
     * @Description: 发送消息到Follower
     * @throws
     */
    public void sendFollower();

}
