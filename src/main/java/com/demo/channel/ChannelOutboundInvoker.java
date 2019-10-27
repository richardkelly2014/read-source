package com.demo.channel;

import java.net.SocketAddress;

/**
 * 通道 出站 调用器
 * Created by jiangfei on 2019/10/27.
 */
public interface ChannelOutboundInvoker {

    //bind
    ChannelFuture bind(SocketAddress localAddress);

    //conection
    ChannelFuture connect(SocketAddress remoteAddress);

    ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress);

    //断开 连接
    ChannelFuture disconnect();

    //关闭
    ChannelFuture close();

    ChannelFuture deregister();

    ChannelFuture bind(SocketAddress localAddress, ChannelPromise promise);

    ChannelFuture connect(SocketAddress remoteAddress, ChannelPromise promise);

    ChannelFuture connect(SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise);

    ChannelFuture disconnect(ChannelPromise promise);

    ChannelFuture close(ChannelPromise promise);

    ChannelFuture deregister(ChannelPromise promise);

    //读取
    ChannelOutboundInvoker read();

    //写入
    ChannelFuture write(Object msg);

    //写入
    ChannelFuture write(Object msg, ChannelPromise promise);

    ChannelOutboundInvoker flush();

    ChannelFuture writeAndFlush(Object msg, ChannelPromise promise);

    ChannelFuture writeAndFlush(Object msg);

    ChannelPromise newPromise();

    ChannelProgressivePromise newProgressivePromise();

    ChannelFuture newSucceededFuture();

    ChannelFuture newFailedFuture(Throwable cause);

    ChannelPromise voidPromise();
}
