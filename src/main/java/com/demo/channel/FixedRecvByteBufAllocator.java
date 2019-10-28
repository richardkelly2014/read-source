package com.demo.channel;

import static com.demo.util.internal.ObjectUtil.checkPositive;

/**
 * 固定 接收 ByteBuf 分配器
 */
public class FixedRecvByteBufAllocator extends DefaultMaxMessagesRecvByteBufAllocator {

    private final int bufferSize;

    private final class HandleImpl extends MaxMessageHandle {
        private final int bufferSize;

        HandleImpl(int bufferSize) {
            this.bufferSize = bufferSize;
        }

        @Override
        public int guess() {
            return bufferSize;
        }
    }

    public FixedRecvByteBufAllocator(int bufferSize) {
        checkPositive(bufferSize, "bufferSize");
        this.bufferSize = bufferSize;
    }

    @SuppressWarnings("deprecation")
    @Override
    public Handle newHandle() {
        return new HandleImpl(bufferSize);
    }

    @Override
    public FixedRecvByteBufAllocator respectMaybeMoreData(boolean respectMaybeMoreData) {
        super.respectMaybeMoreData(respectMaybeMoreData);
        return this;
    }

}
