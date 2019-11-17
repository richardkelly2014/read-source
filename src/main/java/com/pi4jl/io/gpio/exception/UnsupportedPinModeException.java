package com.pi4jl.io.gpio.exception;

import com.pi4jl.io.gpio.Pin;
import com.pi4jl.io.gpio.PinMode;

/**
 * Unsupported pin exception.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class UnsupportedPinModeException extends RuntimeException {

    private static final long serialVersionUID = 6435118278151751895L;
    private final Pin pin;
    private final PinMode mode;

    public UnsupportedPinModeException(Pin pin, PinMode mode) {
        super("This GPIO pin [" + pin.getName() + "] does not support the pin mode specified [" + mode.getName() + "]");
        this.pin = pin;
        this.mode = mode;
    }

    public Pin getPin() {
        return pin;
    }

    public PinMode getMode() {
        return mode;
    }
}
