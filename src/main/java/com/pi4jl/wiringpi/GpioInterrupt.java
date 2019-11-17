package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

import java.util.Vector;

/**
 * Gpio 中断
 * Created by jiangfei on 2019/11/17.
 */
public class GpioInterrupt {

    private static Vector<GpioInterruptListener> listeners = new Vector<>();
    private Object lock;

    // private constructor
    private GpioInterrupt() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    //针脚状态可用回调
    public static native int enablePinStateChangeCallback(int pin);

    //针脚状态不可用回调
    public static native int disablePinStateChangeCallback(int pin);

    //触发事件监听
    private static void pinStateChangeCallback(int pin, boolean state) {

        Vector<GpioInterruptListener> listenersClone;
        listenersClone = (Vector<GpioInterruptListener>) listeners.clone();

        for (int i = 0; i < listenersClone.size(); i++) {
            GpioInterruptListener listener = listenersClone.elementAt(i);
            if (listener != null) {
                GpioInterruptEvent event = new GpioInterruptEvent(listener, pin, state);
                listener.pinStateChange(event);
            }
        }
    }

    public static synchronized void addListener(GpioInterruptListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    public static synchronized void removeListener(GpioInterruptListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }

    public static synchronized boolean hasListener(GpioInterruptListener listener) {
        return listeners.contains(listener);
    }
}
