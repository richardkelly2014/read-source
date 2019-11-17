package com.pi4jl.jni;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface SerialInterruptListener extends java.util.EventListener {
    void onDataReceive(SerialInterruptEvent event);
}
