package com.arthas.common.thread;

/**
 * 
 * @ClassName: TerminalTask
 * @Description: 一个特殊的接口,实现这个接口的线程可以被逻辑结束
 * @author DavieAC
 * @date 2017年10月9日 下午8:39:54
 *
 */
public interface TerminalTask extends Runnable {

    /**
     * 
     * @Title: stop
     * @Description: 停止这个线程
     * @throws
     */
    public void stop();
    
}
