package com.demo.util.concurrent;

/**
 * Created by jiangfei on 2019/10/19.
 */
public interface GenericProgressiveFutureListener<F extends ProgressiveFuture<?>>
        extends GenericFutureListener<F> {

    void operationProgressed(F future, long progress, long total) throws Exception;

}
