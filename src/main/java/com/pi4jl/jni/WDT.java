package com.pi4jl.jni;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class WDT {

    private WDT() {
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    //打开文件
    public static native int open(String file);

    public static native int close(int fd);

    public static native int disable(int fd);

    public static native int getTimeOut(int fd);

    public static native int setTimeOut(int fd, int timeout);

    public static native int ping(int fd);


}
