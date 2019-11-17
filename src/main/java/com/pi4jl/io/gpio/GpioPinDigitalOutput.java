package com.pi4jl.io.gpio;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinDigitalOutput extends GpioPinDigital, GpioPinOutput {

    void high();
    void low();
    void toggle();
    Future<?> blink(long delay);
    Future<?> blink(long delay, TimeUnit timeUnit);
    Future<?> blink(long delay, PinState blinkState);
    Future<?> blink(long delay, PinState blinkState, TimeUnit timeUnit);
    Future<?> blink(long delay, long duration);
    Future<?> blink(long delay, long duration, TimeUnit timeUnit);
    Future<?> blink(long delay, long duration, PinState blinkState);
    Future<?> blink(long delay, long duration, PinState blinkState, TimeUnit timeUnit);
    Future<?> pulse(long duration);
    Future<?> pulse(long duration, TimeUnit timeUnit);
    Future<?> pulse(long duration, Callable<Void> callback);
    Future<?> pulse(long duration, Callable<Void> callback, TimeUnit timeUnit);
    Future<?> pulse(long duration, boolean blocking);
    Future<?> pulse(long duration, boolean blocking, TimeUnit timeUnit);
    Future<?> pulse(long duration, boolean blocking, Callable<Void> callback);
    Future<?> pulse(long duration, boolean blocking, Callable<Void> callback, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState);
    Future<?> pulse(long duration, PinState pulseState, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback);
    Future<?> pulse(long duration, PinState pulseState, Callable<Void> callback, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking, TimeUnit timeUnit);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback);
    Future<?> pulse(long duration, PinState pulseState, boolean blocking, Callable<Void> callback, TimeUnit timeUnit);
    void setState(PinState state);
    void setState(boolean state);

}
