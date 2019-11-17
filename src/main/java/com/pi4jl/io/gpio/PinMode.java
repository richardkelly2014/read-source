package com.pi4jl.io.gpio;

import java.util.EnumSet;

/**
 * Created by jiangfei on 2019/11/17.
 */
public enum  PinMode {

    DIGITAL_INPUT(0, "input", PinDirection.IN),
    DIGITAL_OUTPUT(1, "output", PinDirection.OUT),

    PWM_OUTPUT(2, "pwm_output", PinDirection.OUT),
    GPIO_CLOCK(3, "gpio_clock", PinDirection.OUT),

    SOFT_PWM_OUTPUT(4, "soft_pwm_output", PinDirection.OUT),
    SOFT_TONE_OUTPUT(5, "soft_tone_output", PinDirection.OUT),

    PWM_TONE_OUTPUT(6, "pwm_tone_output", PinDirection.OUT),

    ANALOG_INPUT(998, "analog_input", PinDirection.IN),
    ANALOG_OUTPUT(999, "analog_output", PinDirection.OUT);

    private final int value;
    private final String name;
    private final PinDirection direction;

    private PinMode(int value, String name, PinDirection direction) {
        this.value = value;
        this.name = name;
        this.direction = direction;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public PinDirection getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }

    public static EnumSet<PinMode> allDigital() {
        return EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT);
    }

    public static EnumSet<PinMode> allAnalog() {
        return EnumSet.of(PinMode.ANALOG_INPUT, PinMode.ANALOG_OUTPUT);
    }

    public static EnumSet<PinMode> all() {
        return EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT,
                PinMode.ANALOG_INPUT, PinMode.ANALOG_OUTPUT,
                PinMode.PWM_OUTPUT);
    }

    public static EnumSet<PinMode> allInputs() {
        return EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.ANALOG_INPUT);
    }

    public static EnumSet<PinMode> allOutput() {
        return EnumSet.of(PinMode.DIGITAL_OUTPUT,
                PinMode.ANALOG_OUTPUT,
                PinMode.PWM_OUTPUT);
    }

}
