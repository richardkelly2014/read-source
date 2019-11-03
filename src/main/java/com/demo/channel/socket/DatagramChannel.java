package com.demo.channel.socket;

import com.demo.channel.Channel;
import com.demo.channel.ChannelFuture;
import com.demo.channel.ChannelPromise;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;

/**
 * udp Channel
 * Created by jiangfei on 2019/11/2.
 */
public interface DatagramChannel extends Channel {

    @Override
    DatagramChannelConfig config();
    @Override
    InetSocketAddress localAddress();
    @Override
    InetSocketAddress remoteAddress();

    /**
     * Return {@code true} if the {@link DatagramChannel} is connected to the remote peer.
     */
    boolean isConnected();

    /**
     * Joins a multicast group and notifies the {@link ChannelFuture} once the operation completes.
     */
    ChannelFuture joinGroup(InetAddress multicastAddress);

    /**
     * Joins a multicast group and notifies the {@link ChannelFuture} once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture joinGroup(InetAddress multicastAddress, ChannelPromise future);

    /**
     * Joins the specified multicast group at the specified interface and notifies the {@link ChannelFuture}
     * once the operation completes.
     */
    ChannelFuture joinGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface);

    /**
     * Joins the specified multicast group at the specified interface and notifies the {@link ChannelFuture}
     * once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture joinGroup(
            InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise future);

    /**
     * Joins the specified multicast group at the specified interface and notifies the {@link ChannelFuture}
     * once the operation completes.
     */
    ChannelFuture joinGroup(InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source);

    /**
     * Joins the specified multicast group at the specified interface and notifies the {@link ChannelFuture}
     * once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture joinGroup(
            InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source, ChannelPromise future);

    /**
     * Leaves a multicast group and notifies the {@link ChannelFuture} once the operation completes.
     */
    ChannelFuture leaveGroup(InetAddress multicastAddress);

    /**
     * Leaves a multicast group and notifies the {@link ChannelFuture} once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture leaveGroup(InetAddress multicastAddress, ChannelPromise future);

    /**
     * Leaves a multicast group on a specified local interface and notifies the {@link ChannelFuture} once the
     * operation completes.
     */
    ChannelFuture leaveGroup(InetSocketAddress multicastAddress, NetworkInterface networkInterface);

    /**
     * Leaves a multicast group on a specified local interface and notifies the {@link ChannelFuture} once the
     * operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture leaveGroup(
            InetSocketAddress multicastAddress, NetworkInterface networkInterface, ChannelPromise future);

    /**
     * Leave the specified multicast group at the specified interface using the specified source and notifies
     * the {@link ChannelFuture} once the operation completes.
     *
     */
    ChannelFuture leaveGroup(
            InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source);

    /**
     * Leave the specified multicast group at the specified interface using the specified source and notifies
     * the {@link ChannelFuture} once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture leaveGroup(
            InetAddress multicastAddress, NetworkInterface networkInterface, InetAddress source,
            ChannelPromise future);

    /**
     * Block the given sourceToBlock address for the given multicastAddress on the given networkInterface and notifies
     * the {@link ChannelFuture} once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture block(
            InetAddress multicastAddress, NetworkInterface networkInterface,
            InetAddress sourceToBlock);

    /**
     * Block the given sourceToBlock address for the given multicastAddress on the given networkInterface and notifies
     * the {@link ChannelFuture} once the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture block(
            InetAddress multicastAddress, NetworkInterface networkInterface,
            InetAddress sourceToBlock, ChannelPromise future);

    /**
     * Block the given sourceToBlock address for the given multicastAddress and notifies the {@link ChannelFuture} once
     * the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture block(InetAddress multicastAddress, InetAddress sourceToBlock);

    /**
     * Block the given sourceToBlock address for the given multicastAddress and notifies the {@link ChannelFuture} once
     * the operation completes.
     *
     * The given {@link ChannelFuture} will be notified and also returned.
     */
    ChannelFuture block(
            InetAddress multicastAddress, InetAddress sourceToBlock, ChannelPromise future);

}
