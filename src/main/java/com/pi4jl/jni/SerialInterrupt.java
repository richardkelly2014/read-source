package com.pi4jl.jni;

import com.pi4jl.util.NativeLibraryLoader;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class SerialInterrupt {

    private static Map<Integer, SerialInterruptListener> listeners = new ConcurrentHashMap<>();

    // private constructor
    private SerialInterrupt() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int enableSerialDataReceiveCallback(int fileDescriptor);

    public static native int disableSerialDataReceiveCallback(int fileDescriptor);

    private static void onDataReceiveCallback(int fileDescriptor, byte[] data) {

        // notify event listeners
        if (listeners.containsKey(fileDescriptor)) {
            SerialInterruptListener listener = listeners.get(fileDescriptor);
            if (listener != null) {
                SerialInterruptEvent event = new SerialInterruptEvent(listener, fileDescriptor, data);
                listener.onDataReceive(event);
            }
        }

        //System.out.println("SERIAL PORT [" + fileDescriptor + "] DATA LENGTH = " + data.length + " / " + new String(data));
    }

    public static synchronized void addListener(int fileDescriptor, SerialInterruptListener listener) {
        if (!listeners.containsKey(fileDescriptor)) {
            listeners.put(fileDescriptor, listener);
            enableSerialDataReceiveCallback(fileDescriptor);
        }
    }

    public static synchronized void removeListener(int fileDescriptor) {
        if (listeners.containsKey(fileDescriptor)) {
            listeners.remove(fileDescriptor);
            disableSerialDataReceiveCallback(fileDescriptor);
        }
    }

    public static synchronized boolean hasListener(int fileDescriptor) {
        return listeners.containsKey(fileDescriptor);
    }

}
