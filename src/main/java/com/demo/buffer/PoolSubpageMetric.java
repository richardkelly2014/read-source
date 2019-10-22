package com.demo.buffer;

public interface PoolSubpageMetric {

    int maxNumElements();

    int numAvailable();

    int elementSize();
    
    int pageSize();
}
