package com.pi4jl.io.gpio;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinPwmOutput extends GpioPinPwm, GpioPinOutput {

    /**
     * Set the PWM value/rate to toggle the GPIO pin.
     * If this is a hardware PWM pin, the value should be between a range of 0 to 1024.
     * If this is a software emulated PWM pin, the value should be between a range of 0 to 100.
     *
     * @param value
     */
    void setPwm(int value);

    /**
     * This sets the range register in the PWM generator.
     * The default is 1024 for hardware PWM.
     * The default is 100 for software emulated PWM.
     *
     * @param range
     */
    void setPwmRange(int range);
}
