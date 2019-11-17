package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * 液晶显示
 * Created by jiangfei on 2019/11/17.
 */
public class Lcd {

    // private constructor
    private Lcd() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }


    public static native int lcdInit(int rows, int cols, int bits, int rs, int strb, int d0,
                                     int d1, int d2, int d3, int d4, int d5, int d6, int d7);
    public static native void lcdHome(int handle);
    public static native void lcdClear(int handle);
    public static native void lcdDisplay(int handle, int state);
    public static native void lcdCursor(int handle, int state);
    public static native void lcdCursorBlink(int handle, int state);
    public static native void lcdPosition(int handle, int x, int y);
    public static native void lcdCharDef(int handle, int index, byte data[]);
    public static native void lcdPutchar(int handle, byte data);
    public static native void lcdPuts(int handle, String data);

    public static void lcdPuts(int handle, String data, String... args) {
        lcdPuts(handle, String.format(data, (Object[]) args));
    }

}
