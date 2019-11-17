package com.pi4jl.jni;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface AnalogInputListener extends java.util.EventListener {
    void pinValueChange(AnalogInputEvent event);
}