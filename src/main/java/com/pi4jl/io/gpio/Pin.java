package com.pi4jl.io.gpio;

import java.util.EnumSet;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface Pin extends Comparable<Pin> {

    String getProvider();
    int getAddress();
    String getName();
    EnumSet<PinMode> getSupportedPinModes();
    boolean supportsPinPullResistance();
    EnumSet<PinPullResistance> getSupportedPinPullResistance();
    boolean supportsPinEdges();
    boolean supportsPinEvents();
    EnumSet<PinEdge> getSupportedPinEdges();

}
