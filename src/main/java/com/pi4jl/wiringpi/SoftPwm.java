package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class SoftPwm {

    private SoftPwm() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int softPwmCreate(int pin, int value, int range);
    public static native void softPwmWrite(int pin, int value);
    public static native void softPwmStop(int pin);

}
