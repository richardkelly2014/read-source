package com.demo.channel;

import java.net.SocketAddress;

import static com.demo.util.internal.ObjectUtil.checkPositive;

/**
 * 通道 扩展数据
 * Created by jiangfei on 2019/10/27.
 */
public final class ChannelMetadata {
    private final boolean hasDisconnect;
    private final int defaultMaxMessagesPerRead;

    public ChannelMetadata(boolean hasDisconnect) {
        this(hasDisconnect, 1);
    }


    public ChannelMetadata(boolean hasDisconnect, int defaultMaxMessagesPerRead) {
        checkPositive(defaultMaxMessagesPerRead, "defaultMaxMessagesPerRead");
        this.hasDisconnect = hasDisconnect;
        this.defaultMaxMessagesPerRead = defaultMaxMessagesPerRead;
    }


    public boolean hasDisconnect() {
        return hasDisconnect;
    }


    public int defaultMaxMessagesPerRead() {
        return defaultMaxMessagesPerRead;
    }
}
