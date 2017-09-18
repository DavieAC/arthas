package com.jd.arthas.service.cluster;

/**
 * 主要实现：在多个实例中同时启动,进行通信和选主
 * 
 * @ClassName: ClientServer
 * @Description: 分布式化的客户端抽象
 * @author dsj
 * @date 2017年9月18日 上午8:09:43
 *
 */
public interface ClientServer {

    /**
     * 
     * @Title: sendHeartbate
     * @Description: 向Master发送心跳包
     * @throws
     */
    public void sendHeartbate();

    /**
     * 
     * @Title: electMaster
     * @Description: 尝试竞选Master
     * @return true if success false if fail
     * @throws
     */
    public boolean tryElectMaster();

}
