package com.jd.arthas.service.test;

import java.util.concurrent.TimeUnit;

import com.jd.arthas.service.handle.Handle;
import com.jd.arthas.service.timer.impl.HelloTimer;

public class Test {

    public static void main(String[] args) {

        HelloTimer timer = new HelloTimer();
        timer.setTimer(1L, TimeUnit.SECONDS);
        timer.setTriggerHandle(new Handle() {

            @Override
            public void execute() {
                System.out.println("定时器触发！");
            }

        });

        timer.start();

    }

}
