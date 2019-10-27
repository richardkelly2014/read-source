package com.demo.channel;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.internal.PlatformDependent;

/**
 * Created by jiangfei on 2019/10/27.
 */
final class FailedChannelFuture extends CompleteChannelFuture {

    private final Throwable cause;

    /**
     * Creates a new instance.
     *
     * @param channel the {@link Channel} associated with this future
     * @param cause   the cause of failure
     */
    FailedChannelFuture(Channel channel, EventExecutor executor, Throwable cause) {
        super(channel, executor);
        if (cause == null) {
            throw new NullPointerException("cause");
        }
        this.cause = cause;
    }

    @Override
    public Throwable cause() {
        return cause;
    }

    @Override
    public boolean isSuccess() {
        return false;
    }

    @Override
    public ChannelFuture sync() {
        PlatformDependent.throwException(cause);
        return this;
    }

    @Override
    public ChannelFuture syncUninterruptibly() {
        PlatformDependent.throwException(cause);
        return this;
    }
}
