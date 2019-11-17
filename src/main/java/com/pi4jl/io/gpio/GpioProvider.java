package com.pi4jl.io.gpio;

import com.pi4jl.io.gpio.event.PinListener;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioProvider {

    String getName();

    boolean hasPin(Pin pin);

    void export(Pin pin, PinMode mode, PinState defaultState);
    void export(Pin pin, PinMode mode);
    boolean isExported(Pin pin);
    void unexport(Pin pin);

    void setMode(Pin pin, PinMode mode);
    PinMode getMode(Pin pin);

    void setPullResistance(Pin pin, PinPullResistance resistance);
    PinPullResistance getPullResistance(Pin pin);

    void setState(Pin pin, PinState state);
    PinState getState(Pin pin);

    void setValue(Pin pin, double value);
    double getValue(Pin pin);

    void setPwm(Pin pin, int value);
    void setPwmRange(Pin pin, int range);
    int getPwm(Pin pin);

    void addListener(Pin pin, PinListener listener);
    void removeListener(Pin pin, PinListener listener);
    void removeAllListeners();

    void shutdown();
    boolean isShutdown();

}
