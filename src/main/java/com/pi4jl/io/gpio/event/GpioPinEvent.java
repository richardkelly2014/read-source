package com.pi4jl.io.gpio.event;

import com.pi4jl.io.gpio.GpioPin;

import java.util.EventObject;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class GpioPinEvent extends EventObject {

    private static final long serialVersionUID = -1036445757629271L;
    protected final GpioPin pin;
    protected final PinEventType type;

    public GpioPinEvent(Object obj, GpioPin pin, PinEventType type) {
        super(obj);
        this.pin = pin;
        this.type = type;
    }

    /**
     * Get the pin number that changed and raised this event.
     *
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public GpioPin getPin() {
        return pin;
    }

    public PinEventType getEventType() {
        return type;
    }
}
