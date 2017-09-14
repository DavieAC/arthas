package com.jd.arthas.service.timer.baseImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jd.arthas.service.handle.Handle;
import com.jd.arthas.service.timer.Timer;

public abstract class AbstractTimer implements Timer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTimer.class);

    // 每一个定时器都拥有一个单例线程池，用于定时执行任务
    private ExecutorService TimerExecutor = Executors.newSingleThreadExecutor();

    // 任务句柄
    private Handle taskHandle;

    private Long time;

    private TimeUnit unit;

    @Override
    public void start() {

        TimerExecutor.execute(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    try {
                        // sleep
                        unit.sleep(time);

                        // do
                        try {
                            taskHandle.execute();
                        } catch (Exception e) {
                            logger.error("定时器:{}执行异常:{}", this.getClass(), e);
                        }

                    } catch (InterruptedException e) {
                        logger.error("定时器:{}睡眠被打断", this.getClass());
                    }
                }
            }

        });
    }

    @Override
    public void stop() {
        TimerExecutor.shutdown();
    }

    @Override
    public void setTimer(Long time, TimeUnit unit) {
        this.time = time;
        this.unit = unit;
    }

    @Override
    public void setTriggerHandle(Handle handle) {
        this.taskHandle = handle;
    }

}
