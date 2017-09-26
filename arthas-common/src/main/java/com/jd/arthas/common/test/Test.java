package com.jd.arthas.common.test;


import com.jd.arthas.common.net.MessageSender;
import com.jd.arthas.common.net.impl.MessageSenderImpl;

public class Test {

    private static MessageSender messageSender = new MessageSenderImpl();

    public static void main(String[] args) {
        messageSender.send("127.0.0.1", 8088, "Hello");
    }

}
