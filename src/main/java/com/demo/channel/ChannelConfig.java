package com.demo.channel;

import com.demo.buffer.ByteBufAllocator;

import java.util.Map;

/**
 * 通道配置
 * Created by jiangfei on 2019/10/27.
 */
public interface ChannelConfig {

    //配置项
    Map<ChannelOption<?>, Object> getOptions();

    boolean setOptions(Map<ChannelOption<?>, ?> options);

    <T> T getOption(ChannelOption<T> option);

    <T> boolean setOption(ChannelOption<T> option, T value);

    //连接超时 时间（毫秒）
    int getConnectTimeoutMillis();

    //设置连接超时 时间（毫秒）
    ChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis);

    @Deprecated
    int getMaxMessagesPerRead();

    @Deprecated
    ChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead);

    int getWriteSpinCount();

    ChannelConfig setWriteSpinCount(int writeSpinCount);

    ByteBufAllocator getAllocator();

    ChannelConfig setAllocator(ByteBufAllocator allocator);

    <T extends RecvByteBufAllocator> T getRecvByteBufAllocator();

    ChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator);

    boolean isAutoRead();

    ChannelConfig setAutoRead(boolean autoRead);

    boolean isAutoClose();

    ChannelConfig setAutoClose(boolean autoClose);

    int getWriteBufferHighWaterMark();

    ChannelConfig setWriteBufferHighWaterMark(int writeBufferHighWaterMark);

    int getWriteBufferLowWaterMark();

    ChannelConfig setWriteBufferLowWaterMark(int writeBufferLowWaterMark);

    MessageSizeEstimator getMessageSizeEstimator();

    ChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator);

    WriteBufferWaterMark getWriteBufferWaterMark();

    ChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);
}
