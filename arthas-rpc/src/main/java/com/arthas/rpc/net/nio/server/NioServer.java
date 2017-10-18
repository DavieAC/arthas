package com.arthas.rpc.net.nio.server;

public interface NioServer {

    /**
     * 
     * @Title: start
     * @Description: 开始监听本地端口
     * @throws
     */
    public void start();

    /**
     * 
     * @Title: close
     * @Description: 关闭本地服务
     * @throws
     */
    public void close();

}
