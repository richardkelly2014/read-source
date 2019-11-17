package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class GpioUtil {

    private GpioUtil() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static final int DIRECTION_IN = 0;
    public static final int DIRECTION_OUT = 1;
    public static final int DIRECTION_HIGH = 2;
    public static final int DIRECTION_LOW= 3;
    public static final int EDGE_NONE = 0;
    public static final int EDGE_BOTH = 1;
    public static final int EDGE_RISING = 2;
    public static final int EDGE_FALLING = 3;


    public static native void export(int pin, int direction) throws RuntimeException;
    public static native void unexport(int pin) throws RuntimeException;
    public static native boolean isExported(int pin) throws RuntimeException;

    public static native boolean setEdgeDetection(int pin, int edge) throws RuntimeException;
    public static native int getEdgeDetection(int pin) throws RuntimeException;

    public static native boolean setDirection(int pin, int direction) throws RuntimeException;
    public static native int getDirection(int pin) throws RuntimeException;

    public static native int isPinSupported(int pin) throws RuntimeException;
    public static native boolean isPrivilegedAccessRequired() throws RuntimeException;

    public static native void enableNonPrivilegedAccess() throws RuntimeException;


}
