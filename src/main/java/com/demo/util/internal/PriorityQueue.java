package com.demo.util.internal;

import java.util.Queue;

/**
 * Created by jiangfei on 2019/10/20.
 */
public interface PriorityQueue<T> extends Queue<T> {

    boolean removeTyped(T node);

    boolean containsTyped(T node);

    void priorityChanged(T node);

    void clearIgnoringIndexes();
}
