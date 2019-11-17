package com.pi4jl.wiringpi;

/**
 * gpio 中断 回调
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioInterruptCallback {

    void callback(int pin);
}
