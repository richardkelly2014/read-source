package com.demo.channel.nio;

import com.demo.channel.AbstractChannel;
import com.demo.channel.Channel;
import com.demo.channel.ChannelException;
import com.demo.channel.ChannelPromise;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.util.concurrent.ScheduledFuture;

/**
 * Created by jiangfei on 2019/11/2.
 */
public abstract class AbstractNioChannel extends AbstractChannel {

    //可被选择的通道
    private final SelectableChannel ch;
    protected final int readInterestOp;

    //选择key（注册的事件 key）
    volatile SelectionKey selectionKey;
    boolean readPending;

    private final Runnable clearReadPendingRunnable = new Runnable() {
        @Override
        public void run() {
            clearReadPending0();
        }
    };

    //连接 future
    private ChannelPromise connectPromise;
    //连接超时 future
    private ScheduledFuture<?> connectTimeoutFuture;
    //地址
    private SocketAddress requestedRemoteAddress;

    //Nio的Channel 与 定义的Channel 进行绑定
    protected AbstractNioChannel(Channel parent, SelectableChannel ch, int readInterestOp) {
        super(parent);
        this.ch = ch;
        this.readInterestOp = readInterestOp;
        try {
            //设置为非阻塞
            ch.configureBlocking(false);
        } catch (IOException e) {
            try {
                ch.close();
            } catch (IOException e2) {
//                logger.warn(
//                        "Failed to close a partially initialized socket.", e2);
            }

            throw new ChannelException("Failed to enter non-blocking mode.", e);
        }
    }


    public interface NioUnsafe extends Unsafe {
        /**
         * Return underlying {@link SelectableChannel}
         */
        SelectableChannel ch();

        /**
         * Finish connect
         */
        void finishConnect();

        /**
         * Read from underlying {@link SelectableChannel}
         */
        void read();

        void forceFlush();
    }

    //Nio UnSafe
    protected abstract class AbstractNioUnsafe extends AbstractUnsafe implements NioUnsafe {

    }

}
