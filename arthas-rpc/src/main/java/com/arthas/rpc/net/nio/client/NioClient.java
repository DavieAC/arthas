package com.arthas.rpc.net.nio.client;

public interface NioClient {

    /**
     * 
     * @Title: setInetAddress
     * @Description: 给客户端设置消息发送的地址
     * @param ip 客户端连接的ip
     * @param port 客户端连接的端口号
     * @throws
     */
    public void setSocketAddress(String ip, int port);

    /**
     * 
     * @Title: sendMessage
     * @Description: 发送消息
     * @param msg 要发送的内容
     * @return 服务端回应
     * @throws 发送失败的异常
     */
    public String sendMessage(String msg) throws Exception;

    /**
     * 
     * @Title: disconnect
     * @Description: 断开连接
     * @throws
     */
    public void disconnect();

}
