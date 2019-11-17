package com.pi4jl.io.gpio.exception;


import com.pi4jl.io.gpio.Pin;

/**
 * Unsupported pin events exception.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class UnsupportedPinEventsException extends RuntimeException {

    private static final long serialVersionUID = 6435112275151751895L;
    private final Pin pin;

    public UnsupportedPinEventsException(Pin pin) {
        super("This GPIO pin [" + pin.getName() + "] does not support pin events; no support for edge detection or interrupts; you will have to poll this pin for change changes.");
        this.pin = pin;
    }

    public Pin getPin() {
        return pin;
    }
}
