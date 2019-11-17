package com.pi4jl.io.gpio.trigger;

import com.pi4jl.io.gpio.GpioPin;
import com.pi4jl.io.gpio.PinState;

import java.util.List;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioTrigger {

    void addPinState(PinState... state);

    void addPinState(List<? extends PinState> states);

    boolean hasPinState(PinState state);

    void invoke(GpioPin pin, PinState state);

}
