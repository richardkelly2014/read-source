package com.pi4jl.io.gpio;

/**
 * Created by jiangfei on 2019/11/17.
 */
public enum RaspiPinNumberingScheme {
    DEFAULT_PIN_NUMBERING,  // this numbering scheme uses the abstract WiringPi pin number scheme
    BROADCOM_PIN_NUMBERING  // this numbering scheme uses the concrete Broadcom pin number scheme
}
