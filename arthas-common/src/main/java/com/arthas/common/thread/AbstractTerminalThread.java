package com.arthas.common.thread;

/**
 * 
 * @ClassName: TerminalThread
 * @Description: 一个可以关闭的线程,比起 {@link TerminalTask} 它的特点是不仅仅通过 TerminalThread.shutdown() 从内部关闭线程.
 * 还可以Thread.interrupt() 关闭正在等待中的线程.
 * @author DavieAC
 * @date 2017年10月16日 上午8:35:36
 *
 */
public abstract class AbstractTerminalThread extends Thread {

    public abstract void shutdown();

}
