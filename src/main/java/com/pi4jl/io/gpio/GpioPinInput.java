package com.pi4jl.io.gpio;

import com.pi4jl.io.gpio.trigger.GpioTrigger;

import java.util.Collection;
import java.util.List;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinInput extends GpioPin {

    Collection<GpioTrigger> getTriggers();

    void addTrigger(GpioTrigger... trigger);

    void addTrigger(List<? extends GpioTrigger> triggers);

    void removeTrigger(GpioTrigger... trigger);

    void removeTrigger(List<? extends GpioTrigger> triggers);

    void removeAllTriggers();

}
