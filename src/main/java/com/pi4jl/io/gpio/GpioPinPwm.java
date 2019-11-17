package com.pi4jl.io.gpio;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinPwm extends GpioPin {

    /**
     * If this is a hardware PWM pin, the value will be between a range of 0 to 1024.
     * If this is a software emulated PWM pin, the value will be between a range of 0 to 100.
     *
     * @return the value currently set for the PWM output
     */
    int getPwm();

}
