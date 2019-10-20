package com.demo.util.concurrent;

import java.util.concurrent.Executor;

/**
 * Created by jiangfei on 2019/10/20.
 */
public final class ImmediateExecutor implements Executor {
    public static final ImmediateExecutor INSTANCE = new ImmediateExecutor();

    private  ImmediateExecutor() {
        // use static instance
    }

    @Override
    public void execute(Runnable command) {
        if (command == null) {
            throw new NullPointerException("command");
        }
        command.run();
    }
}
