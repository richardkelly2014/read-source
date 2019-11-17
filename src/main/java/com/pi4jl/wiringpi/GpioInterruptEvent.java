package com.pi4jl.wiringpi;

import java.util.EventObject;

/**
 * 中断事件
 * Created by jiangfei on 2019/11/17.
 */
public class GpioInterruptEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int pin;
    private boolean state;


    public GpioInterruptEvent(Object obj, int pin, boolean state) {
        super(obj);
        this.pin = pin;
        this.state = state;
    }

    public int getPin() {
        return pin;
    }

    public boolean getState() {
        return state;
    }

    public int getStateValue() {
        return (state) ? 1 : 0;
    }

}
