package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class Shift {

    public static final int LSBFIRST = 0;
    public static final int MSBFIRST = 1;

    // private constructor
    private Shift() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }


    public static native byte shiftIn(byte dPin, byte cPin, byte order);
    public static native void shiftOut(byte dPin, byte cPin, byte order, byte val);


}
