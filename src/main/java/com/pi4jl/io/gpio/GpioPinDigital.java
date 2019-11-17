package com.pi4jl.io.gpio;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinDigital extends GpioPin {

    boolean isHigh();
    boolean isLow();
    PinState getState();
    boolean isState(PinState state);

}
