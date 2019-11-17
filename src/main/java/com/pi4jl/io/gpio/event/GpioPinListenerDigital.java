package com.pi4jl.io.gpio.event;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinListenerDigital extends GpioPinListener {

    void handleGpioPinDigitalStateChangeEvent(GpioPinDigitalStateChangeEvent event);

}
