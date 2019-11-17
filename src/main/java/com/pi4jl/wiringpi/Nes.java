package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * 游戏机？
 * Created by jiangfei on 2019/11/17.
 */
public class Nes {

    public static final int NES_RIGHT = 0x01;
    public static final int NES_LEFT = 0x02;
    public static final int NES_DOWN = 0x04;
    public static final int NES_UP = 0x08;
    public static final int NES_START = 0x10;
    public static final int NES_SELECT = 0x20;
    public static final int NES_B = 0x40;
    public static final int NES_A = 0x80;
    public static final int PULSE_TIME = 25;
    public static final int MAX_NES_JOYSTICKS = 8;

    // private constructor
    private Nes() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int setupNesJoystick(int dPin, int cPin, int lPin);
    public static native int readNesJoystick(int joystick);


}
