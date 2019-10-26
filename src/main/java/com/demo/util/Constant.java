package com.demo.util;

/**
 * 常量？
 * Created by jiangfei on 2019/10/26.
 */
public interface Constant<T extends Constant<T>> extends Comparable<T> {

    /**
     * Returns the unique number assigned to this {@link Constant}.
     */
    int id();

    /**
     * Returns the name of this {@link Constant}.
     */
    String name();
}
