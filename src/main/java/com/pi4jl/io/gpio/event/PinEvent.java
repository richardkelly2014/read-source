package com.pi4jl.io.gpio.event;

import com.pi4jl.io.gpio.Pin;

import java.util.EventObject;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class PinEvent extends EventObject {

    private static final long serialVersionUID = 5238592505805435621L;
    protected final Pin pin;
    protected final PinEventType type;

    /**
     * Default event constructor
     *
     * @param obj Ignore this parameter
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public PinEvent(Object obj, Pin pin, PinEventType type) {
        super(obj);
        this.pin = pin;
        this.type = type;
    }

    /**
     * Get the pin number that changed and raised this event.
     *
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public Pin getPin() {
        return pin;
    }

    public PinEventType getEventType() {
        return type;
    }
}
