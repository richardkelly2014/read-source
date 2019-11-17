package com.pi4jl.io.i2c.impl;

import com.pi4jl.io.i2c.I2CDevice;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class I2CDeviceImpl implements I2CDevice {

    /**
     * Reference to i2c bus
     */
    private I2CBusImpl bus;

    /**
     * I2c device address
     */
    private int deviceAddress;

    @Override
    public int getAddress() {
        return deviceAddress;
    }

    /**
     * Constructor.
     *
     * @param bus     i2c bus
     * @param address i2c device address
     */
    public I2CDeviceImpl(I2CBusImpl bus, int address) {
        this.bus = bus;
        this.deviceAddress = address;
    }

    I2CBusImpl getBus() {
        return bus;
    }

    /**
     * This method writes one byte to i2c device.
     *
     * @param data byte to be written
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final byte data) throws IOException {
        getBus().writeByteDirect(this, data);
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     *
     * @param data   buffer of data to be written to the i2c device in one go
     * @param offset offset in buffer
     * @param size   number of bytes to be written
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final byte[] data, final int offset, final int size) throws IOException {
        getBus().writeBytesDirect(this, size, offset, data);
    }

    /**
     * This method writes all bytes included in the given buffer directly to the i2c device.
     *
     * @param buffer buffer of data to be written to the i2c device in one go
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(byte[] buffer) throws IOException {
        write(buffer, 0, buffer.length);
    }

    /**
     * This method writes one byte to i2c device.
     *
     * @param address local address in the i2c device
     * @param data    byte to be written
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final int address, final byte data) throws IOException {
        getBus().writeByte(this, address, data);
    }

    /**
     * This method writes several bytes to the i2c device from given buffer at given offset.
     *
     * @param address local address in the i2c device
     * @param data    buffer of data to be written to the i2c device in one go
     * @param offset  offset in buffer
     * @param size    number of bytes to be written
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    @Override
    public void write(final int address, final byte[] data, final int offset, final int size) throws IOException {
        getBus().writeBytes(this, address, size, offset, data);
    }

    /**
     * This method writes all bytes included in the given buffer directoy to the register address on the i2c device
     *
     * @param address local address in the i2c device
     * @param buffer  buffer of data to be written to the i2c device in one go
     * @throws IOException thrown in case byte cannot be written to the i2c device or i2c bus
     */
    public void write(int address, byte[] buffer) throws IOException {
        write(address, buffer, 0, buffer.length);
    }

    /**
     * This method reads one byte from the i2c device. Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read() throws IOException {
        return getBus().readByteDirect(this);
    }

    /**
     * <p>
     * This method reads bytes from the i2c device to given buffer at asked offset.
     * </p>
     * <p>
     * <p>
     * Note: Current implementation calls {@link #read(int)}. That means for each read byte i2c bus will send (next) address to i2c device.
     * </p>
     *
     * @param data   buffer of data to be read from the i2c device in one go
     * @param offset offset in buffer
     * @param size   number of bytes to be read
     * @return number of bytes read
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final byte[] data, final int offset, final int size) throws IOException {
        return getBus().readBytesDirect(this, size, offset, data);
    }

    /**
     * This method reads one byte from the i2c device. Result is between 0 and 255 if read operation was successful, else a negative number for an error.
     *
     * @param address local address in the i2c device
     * @return byte value read: positive number (or zero) to 255 if read was successful. Negative number if reading failed.
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final int address) throws IOException {
        return getBus().readByte(this, address);
    }

    /**
     * <p>
     * This method reads bytes from the i2c device to given buffer at asked offset.
     * </p>
     * <p>
     * <p>
     * Note: Current implementation calls {@link #read(int)}. That means for each read byte i2c bus will send (next) address to i2c device.
     * </p>
     *
     * @param address local address in the i2c device
     * @param data    buffer of data to be read from the i2c device in one go
     * @param offset  offset in buffer
     * @param size    number of bytes to be read
     * @return number of bytes read
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final int address, final byte[] data, final int offset, final int size) throws IOException {
        return getBus().readBytes(this, address, size, offset, data);
    }

   
    @Override
    public void ioctl(long command, int value) throws IOException {
        getBus().ioctl(this, command, value);
    }


    @Override
    public void ioctl(long command, ByteBuffer data, IntBuffer offsets) throws IOException {
        getBus().ioctl(this, command, data, offsets);
    }

    /**
     * This method writes and reads bytes to/from the i2c device in a single method call
     *
     * @param writeData   buffer of data to be written to the i2c device in one go
     * @param writeOffset offset in write buffer
     * @param writeSize   number of bytes to be written from buffer
     * @param readData    buffer of data to be read from the i2c device in one go
     * @param readOffset  offset in read buffer
     * @param readSize    number of bytes to be read
     * @return number of bytes read
     * @throws IOException thrown in case byte cannot be read from the i2c device or i2c bus
     */
    @Override
    public int read(final byte[] writeData, final int writeOffset, final int writeSize, final byte[] readData, final int readOffset, final int readSize) throws IOException {
        return getBus().writeAndReadBytesDirect(this, writeSize, writeOffset, writeData, readSize, readOffset, readData);
    }

    /**
     * This helper method creates a string describing bus file name and device address (in hex).
     *
     * @return string with all details
     */
    protected String makeDescription() {
        return "I2CDevice on " + bus + " at address 0x" + Integer.toHexString(deviceAddress);
    }

    /**
     * This helper method creates a string describing bus file name, device address (in hex) and local i2c address.
     *
     * @param address local address in i2c device
     * @return string with all details
     */
    protected String makeDescription(int address) {
        return "I2CDevice on " + bus + " at address 0x" + Integer.toHexString(deviceAddress) + " to address 0x" + Integer.toHexString(address);
    }

}
