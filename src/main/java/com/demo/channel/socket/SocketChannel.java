package com.demo.channel.socket;

import java.net.InetSocketAddress;

/**
 * client Socket Channel
 * Created by jiangfei on 2019/11/2.
 */
public interface SocketChannel extends DuplexChannel {

    //server socket channel
    @Override
    ServerSocketChannel parent();

    @Override
    SocketChannelConfig config();

    @Override
    InetSocketAddress localAddress();

    @Override
    InetSocketAddress remoteAddress();

}
