package com.demo.channel;

import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.GenericFutureListener;
import com.demo.util.concurrent.ProgressivePromise;

/**
 * Created by jiangfei on 2019/10/27.
 */
public interface ChannelProgressivePromise extends ProgressivePromise<Void>, ChannelProgressiveFuture, ChannelPromise {

    @Override
    ChannelProgressivePromise addListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    ChannelProgressivePromise addListeners(GenericFutureListener<? extends Future<? super Void>>... listeners);

    @Override
    ChannelProgressivePromise removeListener(GenericFutureListener<? extends Future<? super Void>> listener);

    @Override
    ChannelProgressivePromise removeListeners(GenericFutureListener<? extends Future<? super Void>>... listeners);

    @Override
    ChannelProgressivePromise sync() throws InterruptedException;

    @Override
    ChannelProgressivePromise syncUninterruptibly();

    @Override
    ChannelProgressivePromise await() throws InterruptedException;

    @Override
    ChannelProgressivePromise awaitUninterruptibly();

    @Override
    ChannelProgressivePromise setSuccess(Void result);

    @Override
    ChannelProgressivePromise setSuccess();

    @Override
    ChannelProgressivePromise setFailure(Throwable cause);

    @Override
    ChannelProgressivePromise setProgress(long progress, long total);

    @Override
    ChannelProgressivePromise unvoid();

}
