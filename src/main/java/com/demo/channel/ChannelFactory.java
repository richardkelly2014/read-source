package com.demo.channel;

public interface ChannelFactory<T extends Channel> extends com.demo.bootstrap.ChannelFactory<T> {
    @Override
    T newChannel();
}
