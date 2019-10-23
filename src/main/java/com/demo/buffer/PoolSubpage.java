//package com.demo.buffer;
//
//final class PoolSubpage<T> implements PoolSubpageMetric {
//
//    final PoolChunk<T> chunk;
//    private final int memoryMapIdx;
//    private final int runOffset;
//    private final int pageSize;
//    private final long[] bitmap;
//
//    PoolSubpage<T> prev;
//    PoolSubpage<T> next;
//
//    boolean doNotDestroy;
//    int elemSize;
//    private int maxNumElems;
//    private int bitmapLength;
//    private int nextAvail;
//    private int numAvail;
//
//    PoolSubpage(int pageSize) {
//        chunk = null;
//        memoryMapIdx = -1;
//        runOffset = -1;
//        elemSize = -1;
//        this.pageSize = pageSize;
//        bitmap = null;
//    }
//
//
//    PoolSubpage(PoolSubpage<T> head, PoolChunk<T> chunk, int memoryMapIdx, int runOffset, int pageSize, int elemSize) {
//        this.chunk = chunk;
//        this.memoryMapIdx = memoryMapIdx;
//        this.runOffset = runOffset;
//        this.pageSize = pageSize;
//        bitmap = new long[pageSize >>> 10]; // pageSize / 16 / 64
//        init(head, elemSize);
//    }
//
//    void init(PoolSubpage<T> head, int elemSize) {
//        doNotDestroy = true;
//        this.elemSize = elemSize;
//        if (elemSize != 0) {
//            maxNumElems = numAvail = pageSize / elemSize;
//            nextAvail = 0;
//            bitmapLength = maxNumElems >>> 6;
//            if ((maxNumElems & 63) != 0) {
//                bitmapLength++;
//            }
//
//            for (int i = 0; i < bitmapLength; i++) {
//                bitmap[i] = 0;
//            }
//        }
//        addToPool(head);
//    }
//
//    private void addToPool(PoolSubpage<T> head) {
//        assert prev == null && next == null;
//        prev = head;
//        next = head.next;
//        next.prev = this;
//        head.next = this;
//    }
//
//}
