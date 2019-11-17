package com.pi4jl.io.i2c;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface I2CDevice {

    int getAddress();

    void write(byte b) throws IOException;

    void write(byte[] buffer, int offset, int size) throws IOException;

    void write(byte[] buffer) throws IOException;

    void write(int address, byte b) throws IOException;

    void write(int address, byte[] buffer, int offset, int size) throws IOException;

    void write(int address, byte[] buffer) throws IOException;

    int read() throws IOException;

    int read(byte[] buffer, int offset, int size) throws IOException;

    int read(int address) throws IOException;

    int read(int address, byte[] buffer, int offset, int size) throws IOException;


    void ioctl(long command, int value) throws IOException;

    void ioctl(long command, ByteBuffer data, IntBuffer offsets) throws IOException;

    int read(byte[] writeBuffer, int writeOffset, int writeSize, byte[] readBuffer, int readOffset, int readSize) throws IOException;

}
