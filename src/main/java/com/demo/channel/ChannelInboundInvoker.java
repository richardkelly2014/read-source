package com.demo.channel;

/**
 * 通道 入站 调用器
 * Created by jiangfei on 2019/10/27.
 */
public interface ChannelInboundInvoker {

    ChannelInboundInvoker fireChannelRegistered();

    ChannelInboundInvoker fireChannelUnregistered();

    ChannelInboundInvoker fireChannelActive();

    ChannelInboundInvoker fireChannelInactive();

    ChannelInboundInvoker fireExceptionCaught(Throwable cause);

    ChannelInboundInvoker fireUserEventTriggered(Object event);

    ChannelInboundInvoker fireChannelRead(Object msg);

    ChannelInboundInvoker fireChannelReadComplete();

    ChannelInboundInvoker fireChannelWritabilityChanged();
}
