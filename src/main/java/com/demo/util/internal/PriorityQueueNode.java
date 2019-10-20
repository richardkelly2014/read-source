package com.demo.util.internal;

/**
 * Created by jiangfei on 2019/10/20.
 */
public interface PriorityQueueNode {
    int INDEX_NOT_IN_QUEUE = -1;

    int priorityQueueIndex(DefaultPriorityQueue<?> queue);

    void priorityQueueIndex(DefaultPriorityQueue<?> queue, int i);
}
