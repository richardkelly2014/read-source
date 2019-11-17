package com.pi4jl.io.i2c.impl;

import com.pi4jl.io.file.LinuxFile;
import com.pi4jl.io.i2c.I2CBus;
import com.pi4jl.io.i2c.I2CConstants;
import com.pi4jl.io.i2c.I2CDevice;
import com.pi4jl.io.i2c.I2CFactory;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class I2CBusImpl implements I2CBus {

    /**
     * File handle for this i2c bus
     */
    protected LinuxFile file = null;

    protected int lastAddress = -1;

    /**
     * File name of this i2c bus
     */
    protected String filename;

    /**
     * Used to identifiy the i2c bus within Pi4J
     **/
    protected int busNumber;

    protected long lockAquireTimeout;

    protected TimeUnit lockAquireTimeoutUnit;

    private final ReentrantLock accessLock = new ReentrantLock(true);

    protected I2CBusImpl(final int busNumber, final String fileName, final long lockAquireTimeout, final TimeUnit lockAquireTimeoutUnit) {
        this.filename = fileName;
        this.busNumber = busNumber;

        if (lockAquireTimeout < 0) {
            this.lockAquireTimeout = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT;
        } else {
            this.lockAquireTimeout = lockAquireTimeout;
        }

        if (lockAquireTimeoutUnit == null) {
            this.lockAquireTimeoutUnit = I2CFactory.DEFAULT_LOCKAQUIRE_TIMEOUT_UNITS;
        } else {
            this.lockAquireTimeoutUnit = lockAquireTimeoutUnit;
        }
    }

    @Override
    public I2CDevice getDevice(int address) throws IOException {
        return new I2CDeviceImpl(this, address);
    }

    protected void open() throws IOException {
        if (file != null) {
            return;
        }

        file = new LinuxFile(filename, "rw");

        lastAddress = -1;
    }

    @Override
    public synchronized void close() throws IOException {
        if (file != null) {
            file.close();
            file = null;
        }
    }

    public int readByteDirect(final I2CDevice device) throws IOException {
        return runBusLockedDeviceAction(device, () -> file.readUnsignedByte());
    }

    public int readBytesDirect(final I2CDevice device, final int size, final int offset, final byte[] buffer) throws IOException {
        return runBusLockedDeviceAction(device, () -> file.read(buffer, offset, size));
    }

    public int readByte(final I2CDevice device, final int localAddress) throws IOException {
        return runBusLockedDeviceAction(device, () -> {
            file.writeByte(localAddress);

            return file.readUnsignedByte();
        });
    }

    public int readBytes(final I2CDevice device, final int localAddress, final int size, final int offset, final byte[] buffer) throws IOException {
        return runBusLockedDeviceAction(device, () -> {
            file.writeByte(localAddress);

            return file.read(buffer, offset, size);
        });
    }

    public void writeByteDirect(final I2CDevice device, final byte data) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.writeByte(data & 0xFF);

            return null;
        });
    }

    public void writeBytesDirect(final I2CDevice device, final int size, final int offset, final byte[] buffer) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.write(buffer, offset, size);

            return null;
        });
    }

    public void writeByte(final I2CDevice device, final int localAddress, final byte data) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.write(new byte[]{(byte) localAddress, data});

            return null;
        });
    }

    public void writeBytes(final I2CDevice device, final int localAddress, final int size, final int offset, final byte[] buffer) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            byte[] buf = new byte[size + 1];

            buf[0] = (byte) localAddress;

            System.arraycopy(buffer, offset, buf, 1, size);

            file.write(buf);

            return null;
        });
    }

    public int writeAndReadBytesDirect(final I2CDevice device, final int writeSize, final int writeOffset, final byte[] writeBuffer,
                                       final int readSize, final int readOffset, final byte[] readBuffer) throws IOException {
        return runBusLockedDeviceAction(device, () -> {
            file.write(writeBuffer, writeOffset, writeSize);

            return file.read(readBuffer, readOffset, readSize);
        });
    }

    public void ioctl(final I2CDevice device, final long command, final int value) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.ioctl(command, value);

            return null;
        });
    }

    public void ioctl(final I2CDevice device, final long command, final ByteBuffer values, final IntBuffer offsets) throws IOException {
        runBusLockedDeviceAction(device, () -> {
            file.ioctl(command, values, offsets);

            return null;
        });
    }


    public <T> T runBusLockedDeviceAction(final I2CDevice device, final Callable<T> action) throws IOException {
        if (action == null) {
            throw new NullPointerException("Parameter 'action' is mandatory!");
        }

        testForProperOperationConditions(device);

        try {
            if (accessLock.tryLock(lockAquireTimeout, lockAquireTimeoutUnit)) {
                try {
                    testForProperOperationConditions(device);

                    selectBusSlave(device);

                    return action.call();
                } finally {
                    accessLock.unlock();
                }
            }
        } catch (InterruptedException e) {
            //logger.log(Level.FINER, "Failed locking I2CBusImpl-" + busNumber, e);
            throw new RuntimeException("Could not obtain an access-lock!", e);
        } catch (IOException e) { // unwrap IOExceptionWrapperException
            throw e;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) { // unexpected exceptions
            throw new RuntimeException(e);
        }
        throw new RuntimeException("Could not obtain an access-lock!");
    }

    /**
     * Selects the slave device if not already selected on this bus.
     * Uses SharedSecrets to get the POSIX file descriptor, and runs
     * the required ioctl's via JNI.
     *
     * @param device Device to select
     */
    protected void selectBusSlave(final I2CDevice device) throws IOException {
        final int addr = device.getAddress();

        if (lastAddress != addr) {
            lastAddress = addr;

            file.ioctl(I2CConstants.I2C_SLAVE, addr & 0xFF);
        }
    }

    protected void testForProperOperationConditions(final I2CDevice device) throws IOException {
        if (file == null) {
            throw new IOException(toString() + " has already been closed! A new bus has to be acquired.");
        }

        if (device == null) {
            throw new NullPointerException("Parameter 'device' is mandatory!");
        }
    }

    @Override
    public int getBusNumber() {
        return busNumber;
    }

    @Override
    public String toString() {
        return "I2CBus '" + busNumber + "' ('" + filename + "')";
    }

}
