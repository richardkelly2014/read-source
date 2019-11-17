package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * 树莓派 扩展
 * Created by jiangfei on 2019/11/17.
 */
public class Gertboard {

    public static final int SPI_ADC_SPEED = 1000000;
    public static final int SPI_DAC_SPEED = 1000000;
    public static final int SPI_A2D = 0;
    public static final int SPI_D2A = 1;

    private Gertboard() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native void gertboardAnalogWrite(int chan, int value);
    public static native int gertboardAnalogRead(int chan);
    public static native int gertboardSPISetup();
    public static native int gertboardAnalogSetup(int pinBase);

}
