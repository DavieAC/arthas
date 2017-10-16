package com.arthas.rpc.net.bio.server;

public interface BioServer {

    /**
     * 
     * @Title: setPort
     * @Description: 设置本机监听端口
     * @param port
     * @throws
     */
    public void setPort(int port);

    /**
     * 
     * @Title: startServer
     * @Description: 本地打开端口开始监听消息
     * @param port
     * @throws
     */
    public void start() throws Exception;

    /**
     * 
     * @Title: close
     * @Description: 关闭服务
     * @throws
     */
    public void stop();

}
