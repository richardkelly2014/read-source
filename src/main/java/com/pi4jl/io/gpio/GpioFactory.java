package com.pi4jl.io.gpio;

import com.pi4jl.concurrent.ExecutorServiceFactory;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class GpioFactory {

    // we only allow a single controller to exists
    private static GpioController controller = null;

    // we only allow a single default provider to exists
    private static GpioProvider provider = null;

    // we only allow a single default scheduled executor service factory to exists
    private static ExecutorServiceFactory executorServiceFactory = null;

    // private constructor
    private GpioFactory() {
        // forbid object construction
    }

}
