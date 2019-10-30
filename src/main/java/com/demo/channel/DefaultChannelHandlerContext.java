package com.demo.channel;

import com.demo.util.concurrent.EventExecutor;

/**
 * 默认 handler 上下文
 * Created by jiangfei on 2019/10/30.
 */
final class DefaultChannelHandlerContext extends AbstractChannelHandlerContext {

    private final ChannelHandler handler;

    DefaultChannelHandlerContext(
            DefaultChannelPipeline pipeline, EventExecutor executor, String name, ChannelHandler handler) {

        super(pipeline, executor, name, handler.getClass());
        this.handler = handler;
    }

    @Override
    public ChannelHandler handler() {

        return handler;
    }
}
