package com.pi4jl.io.gpio.event;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface IFTTTMakerChannelTriggerListener {
    
    boolean onTriggered(IFTTTMakerChannelTriggerEvent event);
}
