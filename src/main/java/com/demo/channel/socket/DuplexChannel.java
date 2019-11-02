package com.demo.channel.socket;

import com.demo.channel.Channel;
import com.demo.channel.ChannelFuture;
import com.demo.channel.ChannelPromise;

/**
 * 复试通道
 * Created by jiangfei on 2019/11/2.
 */
public interface DuplexChannel extends Channel {

    //输入通道是否关闭
    boolean isInputShutdown();

    //关闭input stream
    ChannelFuture shutdownInput();

    ChannelFuture shutdownInput(ChannelPromise promise);

    boolean isOutputShutdown();

    ChannelFuture shutdownOutput();

    ChannelFuture shutdownOutput(ChannelPromise promise);

    boolean isShutdown();

    ChannelFuture shutdown();

    ChannelFuture shutdown(ChannelPromise promise);
}
