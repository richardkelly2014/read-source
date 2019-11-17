package com.pi4jl.io.gpio.exception;

import com.pi4jl.io.gpio.Pin;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class InvalidPinException extends RuntimeException {

    private static final long serialVersionUID = -5101222911651959182L;
    private final Pin pin;

    public InvalidPinException(Pin pin) {
        super("Invalid pin exception; this pin [" + pin.getName() + "] is not supported by GPIO provider [" + pin.getProvider() + "]");
        this.pin = pin;
    }

    public Pin getPin() {
        return pin;
    }
}