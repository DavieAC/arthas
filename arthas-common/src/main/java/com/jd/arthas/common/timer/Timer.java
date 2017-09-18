package com.jd.arthas.common.timer;

import java.util.concurrent.TimeUnit;


/**
 * 
 * @ClassName: Timer
 * @Description: 一个定时触发器
 * @author dsj
 * @date 2017年9月14日 下午7:46:28
 *
 */
public interface Timer {

    /**
     * 
     * @Title: start
     * @Description: 定时器开始运行 
     * @throws
     */
    public void start();

    /**
     * 
     * @Title: stop
     * @Description: 定时器结束运行
     * @throws
     */
    public void stop();

    /**
     * 
     * @Title: setTimer
     * @Description: 给定时器设置触发时间
     * @param time
     * @param unit
     * @throws
     */
    public void setTimer(Long time, TimeUnit unit);

    /**
     * 
     * @Title: setTriggerHandle
     * @Description: 定时器触发的句柄
     * @param handle
     * @throws
     */
    public void setTriggerHandle(Handle handle);

    /**
     * 
     * @Title: doBefore
     * @Description: 开始任务之前
     * @throws
     */
    public void doBefore();

    /**
     * 
     * @Title: doAfter
     * @Description: 开始任务之后
     * @throws
     */
    public void doAfter();

}
