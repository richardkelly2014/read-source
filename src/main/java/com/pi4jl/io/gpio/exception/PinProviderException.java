package com.pi4jl.io.gpio.exception;

import com.pi4jl.io.gpio.GpioProvider;
import com.pi4jl.io.gpio.Pin;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class PinProviderException extends RuntimeException {

    private static final long serialVersionUID = -519207741462960871L;
    private final Pin pin;
    private final GpioProvider gpioProvider;

    public PinProviderException(GpioProvider provider, Pin pin) {
        super("GPIO pin [" + pin.toString() + "] expects provider [" + pin.getProvider() + "] but is attempting to be provisioned with provider [" + provider.getName() + "]; provisioning failed.");
        this.pin = pin;
        this.gpioProvider = provider;
    }

    public Pin getPin() {
        return pin;
    }

    public GpioProvider getGpioProvider() {
        return gpioProvider;
    }
}

