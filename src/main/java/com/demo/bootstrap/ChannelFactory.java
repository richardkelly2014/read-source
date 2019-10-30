package com.demo.bootstrap;

import com.demo.channel.Channel;

/**
 * Created by jiangfei on 2019/10/30.
 */
@Deprecated
public interface ChannelFactory<T extends Channel> {
    /**
     * Creates a new channel.
     */
    T newChannel();
}
