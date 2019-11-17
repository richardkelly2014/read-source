package com.pi4jl.io.i2c;

import java.io.IOException;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface I2CBus {
    int BUS_0 = 0;
    int BUS_1 = 1;
    int BUS_2 = 2;
    int BUS_3 = 3;
    int BUS_4 = 4;
    int BUS_5 = 5;
    int BUS_6 = 6;
    int BUS_7 = 7;
    int BUS_8 = 8;
    int BUS_9 = 9;
    int BUS_10 = 10;
    int BUS_11 = 11;
    int BUS_12 = 12;
    int BUS_13 = 13;
    int BUS_14 = 14;
    int BUS_15 = 15;
    int BUS_16 = 16;
    int BUS_17 = 17;

    I2CDevice getDevice(int address) throws IOException;

    int getBusNumber();

    void close() throws IOException;

}
