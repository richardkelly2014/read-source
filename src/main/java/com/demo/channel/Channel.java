package com.demo.channel;

import java.net.SocketAddress;

/**
 * 通道
 * Created by jiangfei on 2019/10/26.
 */
public interface Channel {

    //id
    ChannelId id();

    //通道线程池（事件循环）
    EventLoop eventLoop();

    //parent
    Channel parent();

    //config
    ChannelConfig config();

    boolean isOpen();

    boolean isRegistered();

    boolean isActive();

    ChannelMetadata metadata();

    SocketAddress localAddress();

    SocketAddress remoteAddress();

    //关闭Future
    ChannelFuture closeFuture();

    boolean isWritable();


    long bytesBeforeUnwritable();

    long bytesBeforeWritable();

}
