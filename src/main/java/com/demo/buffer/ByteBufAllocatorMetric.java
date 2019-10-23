package com.demo.buffer;

/**
 * Created by jiangfei on 2019/10/23.
 */
public interface ByteBufAllocatorMetric {

    /**
     * Returns the number of bytes of heap memory used by a {@link ByteBufAllocator} or {@code -1} if unknown.
     */
    long usedHeapMemory();

    /**
     * Returns the number of bytes of direct memory used by a {@link ByteBufAllocator} or {@code -1} if unknown.
     */
    long usedDirectMemory();
}
