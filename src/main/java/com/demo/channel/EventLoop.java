package com.demo.channel;

import com.demo.util.concurrent.OrderedEventExecutor;

/**
 * 事件循环执行器
 * Created by jiangfei on 2019/10/27.
 */
public interface EventLoop extends OrderedEventExecutor, EventLoopGroup {

    @Override
    EventLoopGroup parent();

}
