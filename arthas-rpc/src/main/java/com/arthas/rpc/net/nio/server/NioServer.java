package com.arthas.rpc.net.nio.server;

public interface NioServer {

    /**
     * 
     * @Title: startListen
     * @Description: 开始监听本地端口
     * @param port
     * @throws
     */
    public void startListen(int port);
    
    /**
     * 
     * @Title: close
     * @Description: 关闭本地服务
     * @throws
     */
    public void close();

}
