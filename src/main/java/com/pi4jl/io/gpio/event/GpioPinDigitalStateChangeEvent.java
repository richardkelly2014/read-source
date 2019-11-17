package com.pi4jl.io.gpio.event;

import com.pi4jl.io.gpio.GpioPin;
import com.pi4jl.io.gpio.PinEdge;
import com.pi4jl.io.gpio.PinState;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class GpioPinDigitalStateChangeEvent extends GpioPinEvent {

    static final long serialVersionUID = 1L;
    private final PinState state;
    private final PinEdge edge;

    /**
     * Default event constructor
     *
     * @param obj   Ignore this parameter
     * @param pin   GPIO pin number (not header pin number; not wiringPi pin number)
     * @param state New GPIO pin state.
     */
    public GpioPinDigitalStateChangeEvent(Object obj, GpioPin pin, PinState state) {
        super(obj, pin, PinEventType.DIGITAL_STATE_CHANGE);
        this.state = state;

        // set pin edge caused by the state change
        this.edge = (state == PinState.HIGH) ? PinEdge.RISING : PinEdge.FALLING;
    }

    /**
     * Get the new pin state raised in this event.
     *
     * @return GPIO pin state (HIGH, LOW)
     */
    public PinState getState() {
        return state;
    }

    /**
     * Get the pin edge for the state change caused by this event.
     *
     * @return
     */
    public PinEdge getEdge() {
        return this.edge;
    }
}

