package com.demo.channel.socket;

import com.demo.channel.ServerChannel;

import java.net.InetSocketAddress;

/**
 * Created by jiangfei on 2019/11/2.
 */
public interface ServerSocketChannel extends ServerChannel {

    @Override
    ServerSocketChannelConfig config();
    @Override
    InetSocketAddress localAddress();
    @Override
    InetSocketAddress remoteAddress();

}
