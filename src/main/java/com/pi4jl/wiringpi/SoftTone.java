package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class SoftTone {

    // private constructor
    private SoftTone() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int softToneCreate(int pin);
    public static native void softToneWrite(int pin, int frequency);
    public static native void softToneStop(int pin);

}
