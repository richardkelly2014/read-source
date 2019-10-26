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

    CompositeByteBuf compositeBuffer();

    /**
     * Allocate a {@link CompositeByteBuf} with the given maximum number of components that can be stored in it.
     * If it is a direct or heap buffer depends on the actual implementation.
     */
    CompositeByteBuf compositeBuffer(int maxNumComponents);

    /**
     * Allocate a heap {@link CompositeByteBuf}.
     */
    CompositeByteBuf compositeHeapBuffer();

    /**
     * Allocate a heap {@link CompositeByteBuf} with the given maximum number of components that can be stored in it.
     */
    CompositeByteBuf compositeHeapBuffer(int maxNumComponents);

    /**
     * Allocate a direct {@link CompositeByteBuf}.
     */
    CompositeByteBuf compositeDirectBuffer();

    /**
     * Allocate a direct {@link CompositeByteBuf} with the given maximum number of components that can be stored in it.
     */
    CompositeByteBuf compositeDirectBuffer(int maxNumComponents);


    /**
     * Returns {@code true} if direct {@link ByteBuf}'s are pooled
     */
    boolean isDirectBufferPooled();

    /**
     * Calculate the new capacity of a {@link ByteBuf} that is used when a {@link ByteBuf} needs to expand by the
     * {@code minNewCapacity} with {@code maxCapacity} as upper-bound.
     */
    int calculateNewCapacity(int minNewCapacity, int maxCapacity);
}
