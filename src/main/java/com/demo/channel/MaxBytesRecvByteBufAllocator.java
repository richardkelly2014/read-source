package com.demo.channel;

import java.util.Map;

public interface MaxBytesRecvByteBufAllocator extends RecvByteBufAllocator {

    int maxBytesPerRead();

    MaxBytesRecvByteBufAllocator maxBytesPerRead(int maxBytesPerRead);

    int maxBytesPerIndividualRead();

    MaxBytesRecvByteBufAllocator maxBytesPerIndividualRead(int maxBytesPerIndividualRead);

    Map.Entry<Integer, Integer> maxBytesPerReadPair();

    MaxBytesRecvByteBufAllocator maxBytesPerReadPair(int maxBytesPerRead, int maxBytesPerIndividualRead);
}
