package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * 串行外设 接口
 * Created by jiangfei on 2019/11/17.
 */
public class Spi {

    public static int CHANNEL_0 = 0;
    public static int CHANNEL_1 = 1;

    public static int MODE_0 = 0;
    public static int MODE_1 = 1;
    public static int MODE_2 = 2;
    public static int MODE_3 = 3;

    private Spi() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int wiringPiSPISetup(int channel, int speed);

    public static native int wiringPiSPISetupMode(int channel, int speed, int mode);

    public static native int wiringPiSPIGetFd(int channel);

    public static int wiringPiSPIDataRW(int channel, String data) {
        return wiringPiSPIDataRW(channel, data, data.length());
    }

    public static native int wiringPiSPIDataRW(int channel, String data, int len);

    public static native int wiringPiSPIDataRW(int channel, byte[] data, int len);

    public static int wiringPiSPIDataRW(int channel, byte[] data) {
        return wiringPiSPIDataRW(channel, data, data.length);
    }

    public static native int wiringPiSPIDataRW(int channel, short[] data, int len);

    public static int wiringPiSPIDataRW(int channel, short[] data) {
        return wiringPiSPIDataRW(channel, data, data.length);
    }
}
