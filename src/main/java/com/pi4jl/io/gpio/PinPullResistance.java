package com.pi4jl.io.gpio;

import java.util.EnumSet;

/**
 * Created by jiangfei on 2019/11/17.
 */
public enum PinPullResistance {

    OFF(0, "off"),
    PULL_DOWN(1, "down"),
    PULL_UP(2, "up");

    private final int value;
    private final String name;

    private PinPullResistance(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name.toUpperCase();
    }

    public static EnumSet<PinPullResistance> all() {
        return EnumSet.allOf(PinPullResistance.class);
    }
}

