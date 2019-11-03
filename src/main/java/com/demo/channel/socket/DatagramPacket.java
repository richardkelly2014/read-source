package com.demo.channel.socket;

import com.demo.buffer.ByteBuf;
import com.demo.buffer.ByteBufHolder;
import com.demo.channel.DefaultAddressedEnvelope;

import java.net.InetSocketAddress;

/**
 * udp 包
 * Created by jiangfei on 2019/11/3.
 */
public class DatagramPacket
        extends DefaultAddressedEnvelope<ByteBuf, InetSocketAddress> implements ByteBufHolder {

    public DatagramPacket(ByteBuf data, InetSocketAddress recipient) {
        super(data, recipient);
    }

    /**
     * Create a new instance with the specified packet {@code data}, {@code recipient} address, and {@code sender}
     * address.
     */
    public DatagramPacket(ByteBuf data, InetSocketAddress recipient, InetSocketAddress sender) {
        super(data, recipient, sender);
    }

    @Override
    public DatagramPacket copy() {
        return replace(content().copy());
    }

    @Override
    public DatagramPacket duplicate() {
        return replace(content().duplicate());
    }

    @Override
    public DatagramPacket retainedDuplicate() {
        return replace(content().retainedDuplicate());
    }

    @Override
    public DatagramPacket replace(ByteBuf content) {
        return new DatagramPacket(content, recipient(), sender());
    }

    @Override
    public DatagramPacket retain() {
        super.retain();
        return this;
    }

    @Override
    public DatagramPacket retain(int increment) {
        super.retain(increment);
        return this;
    }

    @Override
    public DatagramPacket touch() {
        super.touch();
        return this;
    }

    @Override
    public DatagramPacket touch(Object hint) {
        super.touch(hint);
        return this;
    }

}
