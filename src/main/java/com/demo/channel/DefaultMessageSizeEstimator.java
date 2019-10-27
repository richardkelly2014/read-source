package com.demo.channel;

import com.demo.buffer.ByteBuf;
import com.demo.buffer.ByteBufHolder;

import static com.demo.util.internal.ObjectUtil.checkPositiveOrZero;

/**
 * Created by jiangfei on 2019/10/27.
 */
public final class DefaultMessageSizeEstimator implements MessageSizeEstimator {

    public static final MessageSizeEstimator DEFAULT = new DefaultMessageSizeEstimator(8);

    private final Handle handle;

    public DefaultMessageSizeEstimator(int unknownSize) {
        checkPositiveOrZero(unknownSize, "unknownSize");
        handle = new HandleImpl(unknownSize);
    }

    @Override
    public Handle newHandle() {

        return handle;
    }

    private static final class HandleImpl implements Handle {
        private final int unknownSize;

        private HandleImpl(int unknownSize) {
            this.unknownSize = unknownSize;
        }

        @Override
        public int size(Object msg) {
            if (msg instanceof ByteBuf) {
                return ((ByteBuf) msg).readableBytes();
            }
            if (msg instanceof ByteBufHolder) {
                return ((ByteBufHolder) msg).content().readableBytes();
            }
            if (msg instanceof FileRegion) {
                return 0;
            }
            return unknownSize;
        }
    }
}
