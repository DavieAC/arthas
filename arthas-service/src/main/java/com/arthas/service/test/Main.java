package com.arthas.service.test;

import java.util.concurrent.TimeUnit;

/**
 * 
 * @ClassName: Main
 * @Description: 使用这个本地类来测试一些代码
 * @author DavieAC
 * @date 2017年10月16日 下午7:59:20
 *
 */
public class Main {

    public static void main(String[] args) {

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {
                    TimeUnit.DAYS.sleep(1L);
                    System.out.println("睡眠结束");
                } catch (Exception e) {
                    System.out.println("异常:" + e);
                } finally {
                    System.out.println("finally块被执行");
                }
            }
        });

        thread.start();

        if (!thread.isInterrupted()) {
            thread.interrupt();
        }
        System.out.println("主线程结束");
    }

}
