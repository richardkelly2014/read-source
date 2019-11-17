package com.pi4jl.io.wdt.impl;

import com.pi4jl.io.wdt.WDTimer;
import com.pi4jl.jni.WDT;

import java.io.IOException;

/**
 * Created by jiangfei on 2019/11/17.
 */
public final class WDTimerImpl implements WDTimer {

    private static final WDTimerImpl instance = new WDTimerImpl();

    /**
     * File handle for watchdog
     */
    private int fd = -1;

    /**
     * File name of watchdog
     */
    private String filename;

    /**
     * Singleton.
     *
     * @return instance
     */
    public static WDTimer getInstance() {
        return instance;
    }

    @Override
    public void open() throws IOException {
        open("/dev/watchdog");
    }

    public void open(String file) throws IOException {
        filename = file;
        if (fd > 0) {
            throw new IOException("File " + filename + " already open.");
        }
        fd = WDT.open(file);
        if (fd < 0) {
            throw new IOException("Cannot open file handle for " + filename + " got " + fd + " back.");
        }
    }

    @Override
    public void setTimeOut(int timeout) throws IOException {
        isOpen();
        int ret = WDT.setTimeOut(fd, timeout);
        if (ret < 0) {
            throw new IOException("Cannot set timeout for " + filename + " got " + ret + " back.");
        }
    }

    @Override
    public int getTimeOut() throws IOException {
        isOpen();
        int ret = WDT.getTimeOut(fd);
        if (ret < 0) {
            throw new IOException("Cannot read timeout for " + filename + " got " + ret + " back.");
        }
        return ret;
    }

    @Override
    public void heartbeat() throws IOException {
        isOpen();
        int ret = WDT.ping(fd);
        if (ret < 0) {
            throw new IOException("Heartbeat error. File " + filename + " got " + ret + " back.");
        }
    }

    @Override
    public void disable() throws IOException {
        isOpen();
        int ret = WDT.disable(fd);
        if (ret < 0) {
            throw new IOException("Cannot disable WatchDog.  File " + filename + " got " + ret + " back.");
        }
    }

    @Override
    public void close() throws IOException {
        isOpen();
        int ret = WDT.close(fd);
        if (ret < 0) {
            throw new IOException("Close file " + filename + " got " + ret + " back.");
        }
        fd = -1;
    }

    /**
     * Test if WDT is open
     *
     * @throws IOException if not
     */
    private void isOpen() throws IOException {
        if (fd < 0) {
            throw new IOException("Watchdog is not open yet");
        }
    }
}
