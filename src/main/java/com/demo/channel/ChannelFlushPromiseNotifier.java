package com.demo.channel;

/**
 * 通道刷新  通知
 * Created by jiangfei on 2019/10/28.
 */
public final class ChannelFlushPromiseNotifier {


    interface FlushCheckpoint {
        long flushCheckpoint();
        void flushCheckpoint(long checkpoint);
        ChannelPromise promise();
    }
}
