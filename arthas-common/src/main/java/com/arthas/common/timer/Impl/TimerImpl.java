package com.arthas.common.timer.Impl;

import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.arthas.common.timer.Handler;
import com.arthas.common.timer.Timer;
import com.arthas.common.utils.paraUtils.ParameterUtil;

/**
 * 
 * @ClassName: AbstractTimer
 * @Description: 定时器的基础实现,线程安全无法保证
 * @author dsj
 * @date 2017年9月15日 上午8:49:31
 * 
 */
public class TimerImpl implements Timer {

    private static final Logger logger = LoggerFactory.getLogger(TimerImpl.class);

    // 定时器线程
    private TimerThread timerThread = null;

    private volatile Boolean running = false;

    private Handler taskHandler;

    private Long time;

    private TimeUnit unit;

    @Override
    public void start() throws Exception {

        // 检查
        if (ParameterUtil.checkNull(taskHandler, time, unit)) {
            throw new Exception("定时器参数配置错误");
        }

        if (!running) {
            synchronized (running) {
                if (!running) {
                    timerThread = new TimerThread();
                    timerThread.start();
                    running = true;
                }
            }
        }
    }

    @Override
    public void stop() throws Exception {

        if (!running) {
            throw new Exception("定时器尚未开始");
        }

        synchronized (running) {
            if (running) {
                // 标志位
                timerThread.cancel();
                // 打断睡眠
                if (!timerThread.isInterrupted()) {
                    timerThread.interrupt();
                }
                running = false;
            }
        }
    }

    private class TimerThread extends Thread {

        private volatile boolean stop = false;

        @Override
        public void run() {

            // circle
            while (!stop) {

                // sleep
                try {
                    unit.sleep(time);
                } catch (InterruptedException e) {
                    logger.info("定时线程被打断,退出定时任务");
                    stop = true;
                    return;
                }

                // do
                try {
                    taskHandler.execute();
                } catch (Exception e) {
                    logger.error("handler执行异常:{}", this.getClass(), e);
                }
            }

        }

        public void cancel() {
            stop = true;
        }

    }

    @Override
    public void setTimer(Long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    @Override
    public void setTriggerHandle(Handler handle) {
        this.taskHandler = handle;
    }

}
