package com.pi4jl.jni;

import java.util.EventObject;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class AnalogInputEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int pin;
    private double value;

    /**
     * <h1>Default event constructor</h1>
     *
     * @param obj Ignore this parameter
     * @param pin GPIO pin number (not header pin number; not wiringPi pin number)
     * @param value New GPIO analog input value.
     */
    public AnalogInputEvent(Object obj, int pin, double value) {
        super(obj);
        this.pin = pin;
        this.value = value;
    }

    /**
     * Get the pin number that changed and raised this event.
     *
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public int getPin() {
        return pin;
    }

    /**
     * Get the new pin analog input value raised in this event.
     *
     * @return analog input value
     */
    public double getValue() {
        return value;
    }
}
