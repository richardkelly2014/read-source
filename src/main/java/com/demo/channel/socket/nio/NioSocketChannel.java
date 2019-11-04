package com.demo.channel.socket.nio;

import com.demo.channel.nio.AbstractNioByteChannel;
import com.demo.channel.socket.SocketChannel;

import java.nio.channels.spi.SelectorProvider;

/**
 * Created by jiangfei on 2019/11/4.
 */
public class NioSocketChannel extends AbstractNioByteChannel implements SocketChannel {


    //select 提供者
    private static final SelectorProvider DEFAULT_SELECTOR_PROVIDER = SelectorProvider.provider();


}
