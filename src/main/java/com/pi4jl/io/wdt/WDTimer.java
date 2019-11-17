package com.pi4jl.io.wdt;

import java.io.IOException;

/**
 * Created by jiangfei on 2019/11/17.
 */
public interface WDTimer {

    void open() throws IOException;

    void setTimeOut(int timeout) throws IOException;

    int getTimeOut() throws IOException;

    void heartbeat() throws IOException;

    void disable() throws IOException;

    void close() throws IOException;

}
