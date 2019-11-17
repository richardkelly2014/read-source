package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * io 总线
 * Created by jiangfei on 2019/11/17.
 */
public class I2C {

    public static int CHANNEL_0 = 0;
    public static int CHANNEL_1 = 1;

    // private constructor
    private I2C() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int wiringPiI2CSetup(int devid);

    public static native int wiringPiI2CRead(int fd);

    public static native int wiringPiI2CWrite(int fd, int data);

    public static native int wiringPiI2CWriteReg8(int fd, int reg, int data);
    public static native int wiringPiI2CWriteReg16(int fd, int reg, int data);
    public static native int wiringPiI2CReadReg8(int fd, int reg);
    public static native int wiringPiI2CReadReg16(int fd, int reg);

}
