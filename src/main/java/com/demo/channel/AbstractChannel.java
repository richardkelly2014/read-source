package com.demo.channel;

import com.demo.util.DefaultAttributeMap;

/**
 * Created by jiangfei on 2019/10/30.
 */
public abstract class AbstractChannel extends DefaultAttributeMap implements Channel{


    private final CloseFuture closeFuture = new CloseFuture(this);





    static final class CloseFuture extends DefaultChannelPromise {

        CloseFuture(AbstractChannel ch) {
            super(ch);
        }

        @Override
        public ChannelPromise setSuccess() {
            throw new IllegalStateException();
        }

        @Override
        public ChannelPromise setFailure(Throwable cause) {
            throw new IllegalStateException();
        }

        @Override
        public boolean trySuccess() {
            throw new IllegalStateException();
        }

        @Override
        public boolean tryFailure(Throwable cause) {
            throw new IllegalStateException();
        }

        boolean setClosed() {
            return super.trySuccess();
        }
    }
}
