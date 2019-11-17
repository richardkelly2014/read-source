package com.pi4jl.io.gpio.event;

import com.pi4jl.io.gpio.Pin;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class PinAnalogValueChangeEvent extends PinEvent {

    private static final long serialVersionUID = -6210539419288104794L;
    private final double value;

    /**
     * Default event constructor
     *
     * @param obj Ignore this parameter
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param value  New GPIO pin analog value.
     */
    public PinAnalogValueChangeEvent(Object obj, Pin pin, double value) {
        super(obj, pin, PinEventType.ANALOG_VALUE_CHANGE);
        this.value = value;
    }

    /**
     * Get the new pin value raised in this event.
     *
     * @return GPIO pin value
     */
    public double getValue() {
        return value;
    }
}

