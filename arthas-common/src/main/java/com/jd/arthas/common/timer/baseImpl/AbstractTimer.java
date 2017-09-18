package com.jd.arthas.common.timer.baseImpl;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.jd.arthas.common.timer.Handle;
import com.jd.arthas.common.timer.Timer;
import com.jd.arthas.common.utils.paraUtils.ParameterUtil;

/**
 * 
 * @ClassName: AbstractTimer
 * @Description: 定时器的基础实现,线程安全无法保证
 * @author dsj
 * @date 2017年9月15日 上午8:49:31
 * 
 */
public abstract class AbstractTimer implements Timer {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTimer.class);

    // 每一个定时器都拥有一个单例线程池，用于定时执行任务
    private ExecutorService TimerExecutor = Executors.newSingleThreadExecutor();

    // 定时器运行状态
    private volatile Boolean isRunning = false;

    // 任务句柄
    private Handle taskHandle;

    private Long time;

    private TimeUnit unit;

    @Override
    public void start() {

        // 检查
        if (ParameterUtil.checkNull(taskHandle, time, unit)) {
            logger.error("[{}]未能正确启动:运行参数尚未配置", this.getClass());
            return;
        }

        isRunning = true;

        TimerExecutor.execute(new Runnable() {

            @Override
            public void run() {

                while (true) {

                    try {

                        doBefore();

                        // sleep
                        unit.sleep(time);

                        // do
                        try {
                            taskHandle.execute();
                        } catch (Exception e) {
                            logger.error("[{}]执行异常:{}", this.getClass(), e);
                        }

                        doAfter();
                    } catch (InterruptedException e) {
                        logger.error("[{}]被打断,退出定时", this.getClass());
                    } catch (Exception e) {
                        logger.error("[{}]异常,{}", this.getClass(), e);
                    }
                }
            }
        });

    }

    @Override
    public void stop() {
        TimerExecutor.shutdownNow();
        isRunning = false;
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

    @Override
    public void doBefore() {

    }

    @Override
    public void doAfter() {

    }

    public boolean getIsRunning() {
        return isRunning;
    }

}
