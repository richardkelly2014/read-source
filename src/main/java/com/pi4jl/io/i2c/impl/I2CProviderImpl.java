package com.pi4jl.io.i2c.impl;

import com.pi4jl.io.i2c.I2CBus;
import com.pi4jl.io.i2c.I2CFactory;
import com.pi4jl.io.i2c.I2CFactoryProvider;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class I2CProviderImpl implements I2CFactoryProvider {

    public I2CProviderImpl() {
    }

    public I2CBus getBus(final int busNumber, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) throws I2CFactory.UnsupportedBusNumberException, IOException {
        final File sysfs = new File("/sys/bus/i2c/devices/i2c-" + busNumber);
        if (!sysfs.exists() || !sysfs.isDirectory()) {
            throw new I2CFactory.UnsupportedBusNumberException();
        }

        final File devfs = new File("/dev/i2c-" + busNumber);
        if (!devfs.exists() || !devfs.canRead() || !devfs.canWrite()) {
            throw new I2CFactory.UnsupportedBusNumberException();
        }

        I2CBusImpl result = new I2CBusImpl(busNumber, devfs.getCanonicalPath(), lockAquireTimeout, lockAquireTimeoutUnit);
        result.open();

        return result;
    }

}
