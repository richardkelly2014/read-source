package com.demo.channel;

import com.demo.util.ReferenceCountUtil;
import com.demo.util.ReferenceCounted;
import com.demo.util.internal.StringUtil;

import java.net.SocketAddress;

/**
 * 默认 地址
 * Created by jiangfei on 2019/11/2.
 */
public class DefaultAddressedEnvelope<M, A extends SocketAddress> implements AddressedEnvelope<M, A> {

    private final M message;
    private final A sender;
    private final A recipient;

    public DefaultAddressedEnvelope(M message, A recipient, A sender) {
        if (message == null) {
            throw new NullPointerException("message");
        }

        if (recipient == null && sender == null) {
            throw new NullPointerException("recipient and sender");
        }

        this.message = message;
        this.sender = sender;
        this.recipient = recipient;
    }

    public DefaultAddressedEnvelope(M message, A recipient) {

        this(message, recipient, null);
    }

    @Override
    public M content() {
        return message;
    }

    @Override
    public A sender() {
        return sender;
    }

    @Override
    public A recipient() {
        return recipient;
    }

    @Override
    public int refCnt() {
        if (message instanceof ReferenceCounted) {
            return ((ReferenceCounted) message).refCnt();
        } else {
            return 1;
        }
    }

    @Override
    public AddressedEnvelope<M, A> retain() {
        ReferenceCountUtil.retain(message);
        return this;
    }

    @Override
    public AddressedEnvelope<M, A> retain(int increment) {
        ReferenceCountUtil.retain(message, increment);
        return this;
    }

    @Override
    public boolean release() {

        return ReferenceCountUtil.release(message);
    }

    @Override
    public boolean release(int decrement) {
        return ReferenceCountUtil.release(message, decrement);
    }

    @Override
    public AddressedEnvelope<M, A> touch() {
        ReferenceCountUtil.touch(message);
        return this;
    }

    @Override
    public AddressedEnvelope<M, A> touch(Object hint) {
        ReferenceCountUtil.touch(message, hint);
        return this;
    }

    @Override
    public String toString() {
        if (sender != null) {
            return StringUtil.simpleClassName(this) +
                    '(' + sender + " => " + recipient + ", " + message + ')';
        } else {
            return StringUtil.simpleClassName(this) +
                    "(=> " + recipient + ", " + message + ')';
        }
    }

}
