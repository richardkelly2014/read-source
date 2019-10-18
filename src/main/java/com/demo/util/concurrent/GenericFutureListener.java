package com.demo.util.concurrent;

import java.util.EventListener;

public interface GenericFutureListener<F extends Future<?>> extends EventListener {

    /**
     * 完成
     *
     * @param future
     * @throws Exception
     */
    void operationComplete(F future) throws Exception;

}
