package com.pi4jl.io.gpio.exception;

import com.pi4jl.io.gpio.Pin;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class GpioPinExistsException extends RuntimeException {

    private static final long serialVersionUID = 1171484082578232353L;

    private final Pin pin;

    public GpioPinExistsException(Pin pin) {
        super("This GPIO pin already exists: " + pin.toString());
        this.pin = pin;
    }

    public Pin getPin() {
        return pin;
    }
}

