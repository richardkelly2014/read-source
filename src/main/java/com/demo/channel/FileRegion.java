package com.demo.channel;

import com.demo.util.ReferenceCounted;

import java.io.IOException;
import java.nio.channels.WritableByteChannel;

/**
 * Created by jiangfei on 2019/10/27.
 */
public interface FileRegion extends ReferenceCounted {


    /**
     * Returns the offset in the file where the transfer began.
     */
    long position();

    /**
     * Returns the bytes which was transferred already.
     *
     * @deprecated Use {@link #transferred()} instead.
     */
    @Deprecated
    long transfered();

    /**
     * Returns the bytes which was transferred already.
     */
    long transferred();

    /**
     * Returns the number of bytes to transfer.
     */
    long count();

    /**
     * Transfers the content of this file region to the specified channel.
     *
     * @param target    the destination of the transfer
     * @param position  the relative offset of the file where the transfer
     *                  begins from.  For example, <tt>0</tt> will make the
     *                  transfer start from {@link #position()}th byte and
     *                  <tt>{@link #count()} - 1</tt> will make the last
     *                  byte of the region transferred.
     */
    long transferTo(WritableByteChannel target, long position) throws IOException;

    @Override
    FileRegion retain();

    @Override
    FileRegion retain(int increment);

    @Override
    FileRegion touch();

    @Override
    FileRegion touch(Object hint);

}
