package com.demo.channel.socket;

import com.demo.buffer.ByteBufAllocator;
import com.demo.channel.ChannelConfig;
import com.demo.channel.MessageSizeEstimator;
import com.demo.channel.RecvByteBufAllocator;
import com.demo.channel.WriteBufferWaterMark;

/**
 * client Socket Channel Config
 * Created by jiangfei on 2019/11/2.
 */
public interface SocketChannelConfig extends ChannelConfig {

    boolean isTcpNoDelay();

    SocketChannelConfig setTcpNoDelay(boolean tcpNoDelay);

    //选项用来设置延迟关闭的时间
    int getSoLinger();

    SocketChannelConfig setSoLinger(int soLinger);

    // 发送缓存大小
    int getSendBufferSize();

    SocketChannelConfig setSendBufferSize(int sendBufferSize);

    int getReceiveBufferSize();

    SocketChannelConfig setReceiveBufferSize(int receiveBufferSize);

    boolean isKeepAlive();

    SocketChannelConfig setKeepAlive(boolean keepAlive);

    int getTrafficClass();

    SocketChannelConfig setTrafficClass(int trafficClass);

    //端口复用
    boolean isReuseAddress();

    SocketChannelConfig setReuseAddress(boolean reuseAddress);

    SocketChannelConfig setPerformancePreferences(int connectionTime, int latency, int bandwidth);

    boolean isAllowHalfClosure();

    SocketChannelConfig setAllowHalfClosure(boolean allowHalfClosure);

    @Override
    SocketChannelConfig setConnectTimeoutMillis(int connectTimeoutMillis);

    @Override
    @Deprecated
    SocketChannelConfig setMaxMessagesPerRead(int maxMessagesPerRead);

    @Override
    SocketChannelConfig setWriteSpinCount(int writeSpinCount);

    @Override
    SocketChannelConfig setAllocator(ByteBufAllocator allocator);

    @Override
    SocketChannelConfig setRecvByteBufAllocator(RecvByteBufAllocator allocator);

    @Override
    SocketChannelConfig setAutoRead(boolean autoRead);

    @Override
    SocketChannelConfig setAutoClose(boolean autoClose);

    @Override
    SocketChannelConfig setMessageSizeEstimator(MessageSizeEstimator estimator);

    @Override
    SocketChannelConfig setWriteBufferWaterMark(WriteBufferWaterMark writeBufferWaterMark);
}
