package com.demo.channel;

import com.demo.util.concurrent.GenericFutureListener;

/**
 * Created by jiangfei on 2019/10/27.
 */
public interface ChannelFutureListener extends GenericFutureListener<ChannelFuture> {

    ChannelFutureListener CLOSE = new ChannelFutureListener() {
        @Override
        public void operationComplete(ChannelFuture future) {
            future.channel().close();
        }
    };

}
