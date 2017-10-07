package com.arthas.rpc.net.bio.server;

public interface BioServer {
    
    /**
     * 
     * @Title: startServer
     * @Description: 本地打开端口开始监听消息
     * @param port
     * @throws
     */
    public void startListen(int port) throws Exception;

}
