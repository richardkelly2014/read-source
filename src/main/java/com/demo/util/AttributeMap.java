package com.demo.util;

/**
 * Created by jiangfei on 2019/10/26.
 */
public interface AttributeMap {

    //get attribute
    <T> Attribute<T> attr(AttributeKey<T> key);

    //has attr
    <T> boolean hasAttr(AttributeKey<T> key);

}
