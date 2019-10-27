package com.demo.channel;

import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.GenericFutureListener;
import com.demo.util.concurrent.ProgressiveFuture;

/**
 * Created by jiangfei on 2019/10/27.
 */
public interface ChannelProgressiveFuture extends ChannelFuture, ProgressiveFuture<Void> {

    @Override
    ChannelProgressiveFuture addListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    ChannelProgressiveFuture addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners);

    @Override
    ChannelProgressiveFuture removeListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    ChannelProgressiveFuture removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners);

    @Override
    ChannelProgressiveFuture sync() throws InterruptedException;

    @Override
    ChannelProgressiveFuture syncUninterruptibly();

    @Override
    ChannelProgressiveFuture await() throws InterruptedException;

    @Override
    ChannelProgressiveFuture awaitUninterruptibly();

}
