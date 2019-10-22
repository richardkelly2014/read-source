package com.demo.buffer;

/**
 * byteBuf 分配器
 */
public interface ByteBufAllocator {

    ByteBuf buffer();

    ByteBuf buffer(int initialCapacity);

    ByteBuf buffer(int initialCapacity, int maxCapacity);

    ByteBuf ioBuffer();

    ByteBuf ioBuffer(int initialCapacity);

    ByteBuf ioBuffer(int initialCapacity, int maxCapacity);

    /**
     * 堆 buffer
     *
     * @return
     */
    ByteBuf heapBuffer();

    ByteBuf heapBuffer(int initialCapacity);

    ByteBuf heapBuffer(int initialCapacity, int maxCapacity);


    ByteBuf directBuffer();

    ByteBuf directBuffer(int initialCapacity);

    ByteBuf directBuffer(int initialCapacity, int maxCapacity);


}
