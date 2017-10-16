package com.arthas.common.utils.threadUtils;

import java.util.List;

import com.arthas.common.thread.AbstractTerminalThread;
import com.arthas.common.thread.TerminalTask;

public class ThreadUtil {

    public static void terminalAllThread(List<AbstractTerminalThread> tasks) {
        for (AbstractTerminalThread task : tasks) {
            task.shutdown();
            task.interrupt();
        }
    }

    public static void terminalAllRunnable(List<TerminalTask> tasks) {
        for (TerminalTask task : tasks) {
            task.stop();
        }
    }

    public static void main(String[] args) {

    }

}
