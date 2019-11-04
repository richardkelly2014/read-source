package com.demo.channel.nio;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

// Nio Task
public interface NioTask<C extends SelectableChannel> {
    /**
     * Invoked when the {@link SelectableChannel} has been selected by the {@link Selector}.
     */
    void channelReady(C ch, SelectionKey key) throws Exception;

    /**
     * Invoked when the {@link SelectionKey} of the specified {@link SelectableChannel} has been cancelled and thus
     * this {@link NioTask} will not be notified anymore.
     *
     * @param cause the cause of the unregistration. {@code null} if a user called {@link SelectionKey#cancel()} or
     *              the event loop has been shut down.
     */
    void channelUnregistered(C ch, Throwable cause) throws Exception;
}
