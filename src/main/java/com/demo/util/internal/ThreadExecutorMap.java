package com.demo.util.internal;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.FastThreadLocal;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;

/**
 * Created by jiangfei on 2019/10/20.
 */
public final class ThreadExecutorMap {

    private static final FastThreadLocal<EventExecutor> mappings = new FastThreadLocal<EventExecutor>();

    private ThreadExecutorMap() { }

    public static Executor apply(final Executor executor, final EventExecutor eventExecutor) {
        ObjectUtil.checkNotNull(executor, "executor");
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        return new Executor() {
            @Override
            public void execute(final Runnable command) {

                executor.execute(apply(command, eventExecutor));
            }
        };
    }


    public static ThreadFactory apply(final ThreadFactory threadFactory, final EventExecutor eventExecutor) {
        ObjectUtil.checkNotNull(threadFactory, "command");
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        return new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                return threadFactory.newThread(apply(r, eventExecutor));
            }
        };
    }

    public static Runnable apply(final Runnable command, final EventExecutor eventExecutor) {
        ObjectUtil.checkNotNull(command, "command");
        ObjectUtil.checkNotNull(eventExecutor, "eventExecutor");
        return new Runnable() {
            @Override
            public void run() {
                setCurrentEventExecutor(eventExecutor);
                try {
                    command.run();
                } finally {
                    setCurrentEventExecutor(null);
                }
            }
        };
    }


    /**
     * Returns the current {@link EventExecutor} that uses the {@link Thread}, or {@code null} if none / unknown.
     */
    public static EventExecutor currentExecutor() {
        return mappings.get();
    }

    /**
     * Set the current {@link EventExecutor} that is used by the {@link Thread}.
     */
    private static void setCurrentEventExecutor(EventExecutor executor) {
        mappings.set(executor);
    }

}
