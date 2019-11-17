package com.pi4jl.io.i2c;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface I2CFactoryProvider {

    I2CBus getBus(int busNumber, long lockAquireTimeout, TimeUnit lockAquireTimeoutUnit)
            throws I2CFactory.UnsupportedBusNumberException, IOException;

}
