package com.pi4jl.io.gpio.exception;

import com.pi4jl.io.gpio.Pin;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class GpioPinNotProvisionedException extends RuntimeException {

    private static final long serialVersionUID = 1171484082578232353L;

    private final Pin pin;

    public GpioPinNotProvisionedException(Pin pin) {
        super("This GPIO pin has not been provisioned: " + pin.toString());
        this.pin = pin;
    }

    public Pin getPin() {
        return pin;
    }
}