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

        // 给参数生成本地固定拷贝,以确保在运行过程中,不会给参数的改变干扰
        Handler curTask = taskHandler.getClass().newInstance();
        Long curTime = new Long(time);
        TimeUnit curUnit = unit.getClass().newInstance();

        // 检查
        if (ParameterUtil.checkNull(curTask, curTime, curUnit)) {
            throw new Exception("定时器参数配置错误");
        }

        if (!running) {
            synchronized (running) {
                if (!running) {
                    timerThread = new TimerThread();

                    timerThread.setTaskHandler(curTask);
                    timerThread.setTime(curTime);
                    timerThread.setUnit(curUnit);

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

        private Handler taskHandler;

        private Long time;

        private TimeUnit unit;

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

        public Handler getTaskHandler() {
            return taskHandler;
        }

        public void setTaskHandler(Handler taskHandler) {
            this.taskHandler = taskHandler;
        }

        public Long getTime() {
            return time;
        }

        public void setTime(Long time) {
            this.time = time;
        }

        public TimeUnit getUnit() {
            return unit;
        }

        public void setUnit(TimeUnit unit) {
            this.unit = unit;
        }
    }

    @Override
    public void setTimer(Long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    @Override
    public void setHandler(Handler handler) {
        this.taskHandler = handler;
    }

}
