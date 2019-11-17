package com.pi4jl.io.gpio;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinDigitalInput extends GpioPinDigital, GpioPinInput {

    /**
     * Determines if a debounce delay interval has been configured for the given pin state.
     *
     * @param state the pin state to test for debounce delay.
     * @return 'true' if the specified ping state has been configured with a debounce delay; else return 'false'.
     */
    boolean hasDebounce(PinState state);

    /**
     * Gets the configured debounce delay interval (in milliseconds) for the given pin state.
     *
     * @param state the pin state to get the configured debounce delay interval.
     * @return the debounce delay interval (in milliseconds) for the specified pin state.
     */
    int getDebounce(PinState state);

    /**
     * Sets the debounce delay interval (in milliseconds) for all pin states.
     *
     * @param debounce The debounce delay interval in milliseconds.
     */
    void setDebounce(int debounce);

    /**
     * Sets the debounce delay interval (in milliseconds) for the specified pin state.
     *
     * @param debounce The debounce delay interval in milliseconds.
     * @param state The pin states to apply the debounce delay interval to.
     */
    void setDebounce(int debounce, PinState ... state);
}

