package com.pi4jl.concurrent;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jiangfei on 2019/11/16.
 */
public class DefaultExecutorServiceFactory implements ExecutorServiceFactory {

    private static int MAX_THREADS_IN_POOL = 25;
    private static List<ExecutorService> singleThreadExecutorServices = new ArrayList<>();


    private static class ScheduledExecutorServiceHolder {
        static final ScheduledExecutorService heldExecutor = Executors.newScheduledThreadPool(MAX_THREADS_IN_POOL, getThreadFactory("pi4j-scheduled-executor-%d"));
    }

    private static ScheduledExecutorService getInternalScheduledExecutorService() {
        return ScheduledExecutorServiceHolder.heldExecutor;
    }

    private static class ScheduledExecutorServiceWrapperHolder {
        static final ScheduledExecutorServiceWrapper heldWrapper = new ScheduledExecutorServiceWrapper(getInternalScheduledExecutorService());
    }

    private static ScheduledExecutorServiceWrapper getScheduledExecutorServiceWrapper() {
        return ScheduledExecutorServiceWrapperHolder.heldWrapper;
    }

    private static class GpioEventExecutorServiceHolder {
        static final ExecutorService heldExecutor = Executors.newCachedThreadPool(getThreadFactory("pi4j-gpio-event-executor-%d"));
    }

    private static ExecutorService getInternalGpioExecutorService() {
        return GpioEventExecutorServiceHolder.heldExecutor;
    }

    private static class GpioEventExecutorServiceWrapperHolder {
        static final ExecutorService heldWrapper = new com.pi4jl.concurrent.ShutdownDisabledExecutorWrapper(getInternalGpioExecutorService());
    }

    private static ExecutorService getGpioEventExecutorServiceWrapper() {
        return GpioEventExecutorServiceWrapperHolder.heldWrapper;
    }

    private static ThreadFactory getThreadFactory(final String nameFormat) {
        final ThreadFactory defaultThreadFactory = Executors.privilegedThreadFactory();
        return new ThreadFactory() {
            final AtomicLong count = (nameFormat != null) ? new AtomicLong(0) : null;

            @Override
            public Thread newThread(Runnable runnable) {
                Thread thread = defaultThreadFactory.newThread(runnable);
                if (nameFormat != null) {
                    thread.setName(String.format(nameFormat, count.getAndIncrement()));
                }
                return thread;
            }
        };
    }


    public ScheduledExecutorService getScheduledExecutorService() {
        // we return the protected wrapper to prevent any consumers from
        // being able to shutdown the scheduled executor service
        return getScheduledExecutorServiceWrapper();
    }

    @Override
    public ExecutorService getGpioEventExecutorService() {
        // we return the protected wrapper to prevent any consumers from
        // being able to shutdown the scheduled executor service
        return getGpioEventExecutorServiceWrapper();
    }

    @Override
    public ExecutorService newSingleThreadExecutorService() {

        // create new single thread executor service
        ExecutorService singleThreadExecutorService = Executors.newSingleThreadExecutor(getThreadFactory("pi4j-single-executor-%d"));

        // add new instance to managed collection
        singleThreadExecutorServices.add(singleThreadExecutorService);

        // return new executor service
        return singleThreadExecutorService;
    }

    /**
     * shutdown executor threads
     */
    public void shutdown() {
        // shutdown each single thread executor in the managed collection
        for (ExecutorService singleThreadExecutorService : singleThreadExecutorServices) {
            shutdownExecutor(singleThreadExecutorService);
        }

        // shutdown scheduled executor instance
        shutdownExecutor(getInternalScheduledExecutorService());
        shutdownExecutor(getInternalGpioExecutorService());

    }

    private void shutdownExecutor(ExecutorService executor) {
        if (executor != null) {
            if (!executor.isShutdown()) {
                // this is a forceful shutdown;
                // don't wait for the scheduled tasks to complete
                executor.shutdownNow();
            }
        }
    }

}
