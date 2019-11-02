package com.demo.channel.socket;

import com.demo.buffer.ByteBufAllocator;
import com.demo.channel.ChannelConfig;
import com.demo.channel.MessageSizeEstimator;
import com.demo.channel.RecvByteBufAllocator;
import com.demo.channel.WriteBufferWaterMark;

/**
 * Created by jiangfei on 2019/11/2.
 */
public interface ServerSocketChannelConfig extends ChannelConfig {


    //连接数
    int getBacklog();

    ServerSocketChannelConfig setBacklog(int backlog);

    boolean isReuseAddress();

    ServerSocketChannelConfig setReuseAddress(boolean reuseAddress);

    int getReceiveBufferSize();

    ServerSocketChannelConfig setReceiveBufferSize(int receiveBufferSize);

    ServerSocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth);

    @Override
    ServerSocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis);

    @Override
    @Deprecated
    ServerSocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead);

    @Override
    ServerSocketChannelConfig setWriteSpinCount(int writeSpinCount);

    @Override
    ServerSocketChannelConfig setAllocator(ByteBufAllocator allocator);

    @Override
    ServerSocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator);

    @Override
    ServerSocketChannelConfig setAutoRead(boolean autoRead);

    @Override
    ServerSocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator);

    @Override
    ServerSocketChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark);

    @Override
    ServerSocketChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark);

    @Override
    ServerSocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);
}
