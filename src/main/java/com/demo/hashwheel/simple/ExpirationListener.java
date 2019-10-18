package com.demo.hashwheel.simple;

/**
 * Created by jiangfei on 2019/10/18.
 */
public interface ExpirationListener<E> {

    /**
     * expired
     *
     * @param expireObject
     */
    void expired(E expireObject);
}
