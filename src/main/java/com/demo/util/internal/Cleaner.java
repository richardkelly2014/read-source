package com.demo.util.internal;

import java.nio.ByteBuffer;

/**
 * cleaner ByteBuffer
 */
public interface Cleaner {

    void freeDirectBuffer(ByteBuffer buffer);
}
