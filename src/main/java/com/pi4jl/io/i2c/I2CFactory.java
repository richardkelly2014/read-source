package com.pi4jl.io.i2c;

import java.util.concurrent.TimeUnit;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class I2CFactory {

    public static final long DEFAULT_LOCKAQUIRE_TIMEOUT = 1000;

    public static final TimeUnit DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS = TimeUnit.MILLISECONDS;

    public static class UnsupportedBusNumberException extends Exception {
        private static final long serialVersionUID = 1L;

        public UnsupportedBusNumberException() {
            super();
        }
    }


}
