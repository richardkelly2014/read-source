package com.pi4jl.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Created by jiangfei on 2019/11/16.
 */
public interface ExecutorServiceFactory {

    /**
     * 定时任务
     *
     * @return
     */
    ScheduledExecutorService getScheduledExecutorService();

    /**
     * 普通线程池
     *
     * @return
     */
    ExecutorService getGpioEventExecutorService();

    @Deprecated
    ExecutorService newSingleThreadExecutorService();

    void shutdown();

}
