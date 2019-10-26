package com.demo.util;

/**
 * 属性
 * Created by jiangfei on 2019/10/26.
 */
public interface Attribute<T> {

    AttributeKey<T> key();


    T get();

    void set(T value);

    T getAndSet(T value);

    T setIfAbsent(T value);

    @Deprecated
    T getAndRemove();

    boolean compareAndSet(T oldValue, T newValue);

    @Deprecated
    void remove();

}
