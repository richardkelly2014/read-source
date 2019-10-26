package com.demo.buffer;

import com.demo.util.ResourceLeakTracker;
import com.demo.util.internal.ObjectUtil;

import java.nio.ByteOrder;

/**
 * Created by jiangfei on 2019/10/26.
 */
class SimpleLeakAwareCompositeByteBuf extends WrappedCompositeByteBuf {

    final ResourceLeakTracker<ByteBuf> leak;

    SimpleLeakAwareCompositeByteBuf(CompositeByteBuf wrapped, ResourceLeakTracker<ByteBuf> leak) {
        super(wrapped);
        this.leak = ObjectUtil.checkNotNull(leak, "leak");
    }

    @Override
    public boolean release() {
        // Call unwrap() before just in case that super.release() will change the ByteBuf instance that is returned
        // by unwrap().
        ByteBuf unwrapped = unwrap();
        if (super.release()) {
            closeLeak(unwrapped);
            return true;
        }
        return false;
    }

    @Override
    public boolean release(int decrement) {
        // Call unwrap() before just in case that super.release() will change the ByteBuf instance that is returned
        // by unwrap().
        ByteBuf unwrapped = unwrap();
        if (super.release(decrement)) {
            closeLeak(unwrapped);
            return true;
        }
        return false;
    }

    private void closeLeak(ByteBuf trackedByteBuf) {
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
            return newLeakAwareByteBuf(super.order(endianness));
        }
    }

    @Override
    public ByteBuf slice() {
        return newLeakAwareByteBuf(super.slice());
    }

    @Override
    public ByteBuf retainedSlice() {
        return newLeakAwareByteBuf(super.retainedSlice());
    }

    @Override
    public ByteBuf slice(int index, int length) {
        return newLeakAwareByteBuf(super.slice(index, length));
    }

    @Override
    public ByteBuf retainedSlice(int index, int length) {
        return newLeakAwareByteBuf(super.retainedSlice(index, length));
    }

    @Override
    public ByteBuf duplicate() {
        return newLeakAwareByteBuf(super.duplicate());
    }

    @Override
    public ByteBuf retainedDuplicate() {
        return newLeakAwareByteBuf(super.retainedDuplicate());
    }

    @Override
    public ByteBuf readSlice(int length) {
        return newLeakAwareByteBuf(super.readSlice(length));
    }

    @Override
    public ByteBuf readRetainedSlice(int length) {
        return newLeakAwareByteBuf(super.readRetainedSlice(length));
    }

    @Override
    public ByteBuf asReadOnly() {
        return newLeakAwareByteBuf(super.asReadOnly());
    }

    private SimpleLeakAwareByteBuf newLeakAwareByteBuf(ByteBuf wrapped) {
        return newLeakAwareByteBuf(wrapped, unwrap(), leak);
    }

    protected SimpleLeakAwareByteBuf newLeakAwareByteBuf(
            ByteBuf wrapped, ByteBuf trackedByteBuf, ResourceLeakTracker<ByteBuf> leakTracker) {
        return new SimpleLeakAwareByteBuf(wrapped, trackedByteBuf, leakTracker);
    }
}
