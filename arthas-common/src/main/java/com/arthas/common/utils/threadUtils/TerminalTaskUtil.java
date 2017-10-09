package com.arthas.common.utils.threadUtils;

import java.util.List;

import com.arthas.common.thread.TerminalTask;

public class TerminalTaskUtil {

    public static void terminalAll(List<TerminalTask> tasks) {
        for (TerminalTask task : tasks) {
            task.stop();
        }
    }

    public static void main(String[] args) {

    }

}
