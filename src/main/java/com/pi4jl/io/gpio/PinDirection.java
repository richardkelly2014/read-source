package com.pi4jl.io.gpio;

import java.util.EnumSet;

/**
 * Created by jiangfei on 2019/11/17.
 */
public enum PinDirection {

    IN(0, "in"),
    OUT(1, "out");

    private final int value;
    private final String name;

    private PinDirection(int value, String name) {
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

    public static PinDirection getDirection(int direction) {
        for (PinDirection item : PinDirection.values()) {
            if (item.getValue() == direction) {
                return item;
            }
        }
        return null;
    }

    public static EnumSet<PinDirection> all() {
        return EnumSet.allOf(PinDirection.class);
    }

}
