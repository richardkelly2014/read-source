package com.demo.channel;

import com.demo.util.concurrent.AbstractEventExecutorGroup;

/**
 * Created by jiangfei on 2019/10/30.
 */
public abstract class AbstractEventLoopGroup extends AbstractEventExecutorGroup implements EventLoopGroup {
    @Override
    public abstract EventLoop next();
}
