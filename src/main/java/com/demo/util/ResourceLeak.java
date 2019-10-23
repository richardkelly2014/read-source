package com.demo.util;

/**
 * Created by jiangfei on 2019/10/23.
 */
@Deprecated
public interface ResourceLeak {
    /**
     * Records the caller's current stack trace so that the {@link ResourceLeakDetector} can tell where the leaked
     * resource was accessed lastly. This method is a shortcut to {@link #record(Object) record(null)}.
     */
    void record();

    /**
     * Records the caller's current stack trace and the specified additional arbitrary information
     * so that the {@link ResourceLeakDetector} can tell where the leaked resource was accessed lastly.
     */
    void record(Object hint);

    /**
     * Close the leak so that {@link ResourceLeakDetector} does not warn about leaked resources.
     *
     * @return {@code true} if called first time, {@code false} if called already
     */
    boolean close();
}
