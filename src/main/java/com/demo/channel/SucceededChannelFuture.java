package com.demo.channel;

import com.demo.util.concurrent.EventExecutor;

/**
 * Created by jiangfei on 2019/10/27.
 */
final class SucceededChannelFuture extends CompleteChannelFuture {

    /**
     * Creates a new instance.
     *
     * @param channel the {@link Channel} associated with this future
     */
    SucceededChannelFuture(Channel channel, EventExecutor executor) {

        super(channel, executor);
    }

    @Override
    public Throwable cause() {
        return null;
    }

    @Override
    public boolean isSuccess() {
        return true;
    }
}
