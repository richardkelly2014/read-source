package com.pi4jl.io.gpio;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinAnalogOutput extends GpioPinAnalog, GpioPinOutput {

    void setValue(double value);
    void setValue(Number value);
}