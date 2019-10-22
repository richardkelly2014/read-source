package com.demo.buffer;

public interface PoolChunkMetric {

    int usage();

    int chunkSize();

    int freeBytes();

}
