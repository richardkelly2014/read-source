package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

/**
 * 串口
 * Created by jiangfei on 2019/11/17.
 */
public class Serial {

    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";

    // private constructor
    private Serial() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public synchronized static native int serialOpen(String device, int baud);
    public synchronized static native void serialClose(int fd);

    public synchronized static native void serialFlush(int fd);
    public synchronized static native void serialPutByte(int fd, byte data);
    public synchronized static void serialPutchar(int fd, char data){
        serialPutByte(fd, (byte)data);
    }
    public synchronized static native void serialPutBytes(int fd, byte[] data, int length);
    public synchronized static void serialPutBytes(int fd, byte ... data){
        serialPutBytes(fd, data, data.length);
    }
    public synchronized static native void serialPuts(int fd, String data);

    public synchronized static void serialPuts(int fd, String data, String... args) {
        serialPuts(fd, String.format(data, (Object[]) args));
    }

    public synchronized static native int serialDataAvail(int fd);
    public synchronized static native byte serialGetByte(int fd);
    public synchronized static native byte[] serialGetBytes(int fd, int length);
    public synchronized static native byte[] serialGetAvailableBytes(int fd);
    public synchronized static native int serialGetchar(int fd);

}
