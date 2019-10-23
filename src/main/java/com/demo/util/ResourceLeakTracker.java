package com.demo.util;

/**
 * 资源泄漏跟踪
 * Created by jiangfei on 2019/10/23.
 */
public interface ResourceLeakTracker<T> {
    void record();

    void record(Object hint);

    boolean close(T trackedObject);
}
