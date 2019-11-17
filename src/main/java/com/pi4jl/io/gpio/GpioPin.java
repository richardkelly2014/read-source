package com.pi4jl.io.gpio;

import com.pi4jl.io.gpio.event.GpioPinListener;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPin {

    GpioProvider getProvider();
    Pin getPin();

    void setName(String name);
    String getName();

    void setTag(Object tag);
    Object getTag();

    void setProperty(String key, String value);
    boolean hasProperty(String key);
    String getProperty(String key);
    String getProperty(String key, String defaultValue);
    Map<String,String> getProperties();
    void removeProperty(String key);
    void clearProperties();

    void export(PinMode mode);
    void export(PinMode mode, PinState defaultState);
    void unexport();
    boolean isExported();

    void setMode(PinMode mode);
    PinMode getMode();
    boolean isMode(PinMode mode);

    void setPullResistance(PinPullResistance resistance);
    PinPullResistance getPullResistance();
    boolean isPullResistance(PinPullResistance resistance);

    Collection<GpioPinListener> getListeners();
    void addListener(GpioPinListener... listener);
    void addListener(List<? extends GpioPinListener> listeners);
    boolean hasListener(GpioPinListener... listener);
    void removeListener(GpioPinListener... listener);
    void removeListener(List<? extends GpioPinListener> listeners);
    void removeAllListeners();

    GpioPinShutdown getShutdownOptions();
    void setShutdownOptions(GpioPinShutdown options);
    void setShutdownOptions(Boolean unexport);
    void setShutdownOptions(Boolean unexport, PinState state);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance);
    void setShutdownOptions(Boolean unexport, PinState state, PinPullResistance resistance, PinMode mode);

}
