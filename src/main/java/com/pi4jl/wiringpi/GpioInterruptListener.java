package com.pi4jl.wiringpi;

/**
 * 中断事件 监听
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioInterruptListener extends java.util.EventListener {
    
    void pinStateChange(GpioInterruptEvent event);
}
