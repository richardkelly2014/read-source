package com.demo.buffer;

import com.demo.util.ResourceLeakTracker;
import com.demo.util.internal.ObjectUtil;

import java.nio.ByteOrder;

/**
 * 泄漏感知 ByteBuf
 * Created by jiangfei on 2019/10/26.
 */
class SimpleLeakAwareByteBuf extends WrappedByteBuf {

    private final ByteBuf trackedByteBuf;
    //泄漏追踪
    final ResourceLeakTracker<ByteBuf> leak;

    SimpleLeakAwareByteBuf(ByteBuf wrapped, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped);
        this.trackedByteBuf = ObjectUtil.checkNotNull(trackedByteBuf, "trackedByteBuf");
        this.leak = ObjectUtil.checkNotNull(leak, "leak");
    }


    SimpleLeakAwareByteBuf(ByteBuf wrapped, ResourceLeakTracker<ByteBuf> leak) {

        this(wrapped, wrapped, leak);
    }

    @Override
    public ByteBuf slice() {
        return newSharedLeakAwareByteBuf(super.slice());
    }

    @Override
    public ByteBuf retainedSlice() {
        return unwrappedDerived(super.retainedSlice());
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return unwrappedDerived(super.retainedSlice(index, length));
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return unwrappedDerived(super.retainedDuplicate());
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return unwrappedDerived(super.readRetainedSlice(length));
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return newSharedLeakAwareByteBuf(super.slice(index, length));
    }

    @Override
    public ByteBuf duplicate() {
        return newSharedLeakAwareByteBuf(super.duplicate());
    }

    @Override
    public ByteBuf readSlice(int length) {
        return newSharedLeakAwareByteBuf(super.readSlice(length));
    }

    @Override
    public ByteBuf asReadOnly() {
        return newSharedLeakAwareByteBuf(super.asReadOnly());
    }

    @Override
    public ByteBuf touch() {
        return this;
    }

    @Override
    public ByteBuf touch(Object hint) {
        return this;
    }

    @Override
    public boolean release() {
        if (super.release()) {
            closeLeak();
            return true;
        }
        return false;
    }

    @Override
    public boolean release(int decrement) {
        if (super.release(decrement)) {
            closeLeak();
            return true;
        }
        return false;
    }

    private void closeLeak() {
        // Close the ResourceLeakTracker with the tracked ByteBuf as argument. This must be the same that was used when
        // calling DefaultResourceLeak.track(...).
        boolean closed = leak.close(trackedByteBuf);
        assert closed;
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {
        if (order() == endianness) {
            return this;
        } else {
            return newSharedLeakAwareByteBuf(super.order(endianness));
        }
    }

    private ByteBuf unwrappedDerived(ByteBuf derived) {
        // We only need to unwrap SwappedByteBuf implementations as these will be the only ones that may end up in
        // the AbstractLeakAwareByteBuf implementations beside slices / duplicates and "real" buffers.
        ByteBuf unwrappedDerived = unwrapSwapped(derived);

        if (unwrappedDerived instanceof AbstractPooledDerivedByteBuf) {
            // Update the parent to point to this buffer so we correctly close the ResourceLeakTracker.
            ((AbstractPooledDerivedByteBuf) unwrappedDerived).parent(this);

            ResourceLeakTracker<ByteBuf> newLeak = AbstractByteBuf.leakDetector.track(derived);
            if (newLeak == null) {
                // No leak detection, just return the derived buffer.
                return derived;
            }
            return newLeakAwareByteBuf(derived, newLeak);
        }
        return newSharedLeakAwareByteBuf(derived);
    }

    @SuppressWarnings("deprecation")
    private static ByteBuf unwrapSwapped(ByteBuf buf) {
        if (buf instanceof SwappedByteBuf) {
            do {
                buf = buf.unwrap();
            } while (buf instanceof SwappedByteBuf);

            return buf;
        }
        return buf;
    }

    private SimpleLeakAwareByteBuf newSharedLeakAwareByteBuf(
            ByteBuf wrapped) {
        return newLeakAwareByteBuf(wrapped, trackedByteBuf, leak);
    }

    private SimpleLeakAwareByteBuf newLeakAwareByteBuf(
            ByteBuf wrapped, ResourceLeakTracker<ByteBuf> leakTracker) {
        return newLeakAwareByteBuf(wrapped, wrapped, leakTracker);
    }

    protected SimpleLeakAwareByteBuf newLeakAwareByteBuf(
            ByteBuf buf, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leakTracker) {
        return new SimpleLeakAwareByteBuf(buf, trackedByteBuf, leakTracker);
    }


}
