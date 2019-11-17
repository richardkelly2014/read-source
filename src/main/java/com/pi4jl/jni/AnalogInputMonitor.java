package com.pi4jl.jni;

import com.pi4jl.util.NativeLibraryLoader;

import java.util.Vector;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class AnalogInputMonitor {

    private static Vector<AnalogInputListener> listeners = new Vector<>();
    private Object lock;

    // private constructor
    private AnalogInputMonitor() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public static native int enablePinValueChangeCallback(int pin, int pollingRate, double changeThreshold);

    public static native int disablePinValueChangeCallback(int pin);

    private static void pinValueChangeCallback(int pin, double value) {

        Vector<AnalogInputListener> listenersClone;
        listenersClone = (Vector<AnalogInputListener>) listeners.clone();

        for (int i = 0; i < listenersClone.size(); i++) {
            AnalogInputListener listener = listenersClone.elementAt(i);
            if (listener != null) {
                AnalogInputEvent event = new AnalogInputEvent(listener, pin, value);
                listener.pinValueChange(event);
            }
        }
    }

    /**
     * <p>
     * Java consumer code can all this method to register itself as a listener for pin analog
     * input value changes.
     * </p>
     *
     * @param listener A class instance that implements the AnalogInputListener interface.
     * @see AnalogInputListener
     * @see AnalogInputEvent
     */
    public static synchronized void addListener(AnalogInputListener listener) {
        if (!listeners.contains(listener)) {
            listeners.addElement(listener);
        }
    }

    /**
     * <p>
     * Java consumer code can all this method to unregister itself as a listener for pin analog
     * input value changes.
     * </p>
     *
     * @param listener A class instance that implements the AnalogInputListener interface.
     * @see AnalogInputListener
     * @see AnalogInputEvent
     */
    public static synchronized void removeListener(AnalogInputListener listener) {
        if (listeners.contains(listener)) {
            listeners.removeElement(listener);
        }
    }


    /**
     * <p>
     * Returns true if the listener is already registered for event callbacks.
     * </p>
     *
     * @param listener A class instance that implements the AnalogInputListener interface.
     * @see AnalogInputListener
     * @see AnalogInputEvent
     */
    public static synchronized boolean hasListener(AnalogInputListener listener) {
        return listeners.contains(listener);
    }

}
