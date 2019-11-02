package com.demo.channel;

import com.demo.util.ReferenceCounted;

import java.net.SocketAddress;

/**
 * Created by jiangfei on 2019/11/2.
 */
public interface AddressedEnvelope<M, A extends SocketAddress> extends ReferenceCounted {

    /**
     * Returns the message wrapped by this envelope message.
     */
    M content();

    /**
     * Returns the address of the sender of this message.
     */
    A sender();

    /**
     * Returns the address of the recipient of this message.
     */
    A recipient();

    @Override
    AddressedEnvelope<M, A> retain();

    @Override
    AddressedEnvelope<M, A> retain(int increment);

    @Override
    AddressedEnvelope<M, A> touch();

    @Override
    AddressedEnvelope<M, A> touch(Object hint);

}
