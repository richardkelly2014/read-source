package com.demo.channel;

import com.demo.util.concurrent.AbstractEventExecutor;

/**
 * Created by jiangfei on 2019/10/28.
 */
public abstract class AbstractEventLoop extends AbstractEventExecutor implements EventLoop {

    protected AbstractEventLoop() {
    }

    protected AbstractEventLoop(EventLoopGroup parent) {
        super(parent);
    }

    @Override
    public EventLoopGroup parent() {
        return (EventLoopGroup) super.parent();
    }

    @Override
    public EventLoop next() {
        return (EventLoop) super.next();
    }
}
