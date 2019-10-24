package com.demo.util;

/**
 * 应用计数
 */
public interface ReferenceCounted {

    /**
     * 111
     * @return
     */
    int refCnt();

    ReferenceCounted retain();

    ReferenceCounted retain(int increment);

    ReferenceCounted touch();

    ReferenceCounted touch(Object hint);

    boolean release();

    boolean release(int decrement);
}
