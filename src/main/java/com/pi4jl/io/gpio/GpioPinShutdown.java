package com.pi4jl.io.gpio;

/**
 * 关闭
 * Created by jiangfei on 2019/11/17.
 */
public interface GpioPinShutdown {

    void setUnexport(Boolean unexport);

    Boolean getUnexport();

    void setMode(PinMode mode);

    PinMode getMode();

    void setPullResistor(PinPullResistance resistance);

    PinPullResistance getPullResistor();

    void setState(PinState state);

    PinState getState();


}
