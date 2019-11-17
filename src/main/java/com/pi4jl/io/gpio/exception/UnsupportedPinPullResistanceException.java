package com.pi4jl.io.gpio.exception;


import com.pi4jl.io.gpio.Pin;
import com.pi4jl.io.gpio.PinPullResistance;

/**
 * Unsupported pin pull up/down resistance exception.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class UnsupportedPinPullResistanceException extends RuntimeException {

    private static final long serialVersionUID = 6065621786188662862L;
    private final Pin pin;
    private final PinPullResistance resistance;

    public UnsupportedPinPullResistanceException(Pin pin, PinPullResistance resistance) {
        super("This GPIO pin [" + pin.getName() + "] does not support the pull resistance specified [" + resistance.getName() + "]");
        this.pin = pin;
        this.resistance = resistance;
    }

    public Pin getPin() {
        return pin;
    }

    public PinPullResistance getPullResistance() {
        return resistance;
    }
}
