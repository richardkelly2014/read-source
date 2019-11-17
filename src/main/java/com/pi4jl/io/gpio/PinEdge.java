package com.pi4jl.io.gpio;

import java.util.EnumSet;

/**
 * Created by jiangfei on 2019/11/17.
 */
public enum PinEdge {
    NONE(0, "none"),
    BOTH(1, "both"),
    RISING(2, "rising"),
    FALLING(3, "falling");

    private final int value;
    private final String name;

    private PinEdge(int value, String name) {
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
        return name;
    }

    public static PinEdge getEdge(int edge) {
        for (PinEdge item : PinEdge.values()) {
            if (item.getValue() == edge) {
                return item;
            }
        }
        return null;
    }

    public static EnumSet<PinEdge> all() {
        return EnumSet.allOf(PinEdge.class);
    }
}
