package com.demo.channel;

import java.util.Queue;

/**
 * Created by jiangfei on 2019/11/3.
 */
public interface EventLoopTaskQueueFactory {

    Queue<Runnable> newTaskQueue(int maxCapacity);
}
