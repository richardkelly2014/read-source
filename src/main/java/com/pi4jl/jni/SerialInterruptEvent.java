package com.pi4jl.jni;

import java.util.EventObject;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class SerialInterruptEvent extends EventObject {

    private static final long serialVersionUID = 1L;
    private int fileDescriptor;
    private byte[] data;

    /**
     * <h1>Default event constructor</h1>
     *
     * @param obj            Ignore this parameter
     * @param fileDescriptor The serial file handle/descriptor in use
     * @param data           data bytes received in this event from the serial receive buffer
     */
    public SerialInterruptEvent(Object obj, int fileDescriptor, byte[] data) {
        super(obj);
        this.fileDescriptor = fileDescriptor;
        this.data = data;
    }

    /**
     * Get the serial port file descriptor/handle
     *
     * @return serial port file descriptor/handle
     */
    public int getFileDescriptor() {
        return fileDescriptor;
    }

    /**
     * Get the length of data bytes received in this event.
     *
     * @return length of data bytes received in this event
     */
    public int getLength() {
        return data.length;
    }

    /**
     * Get the data bytes received in this event.
     *
     * @return data bytes received in this event
     */
    public byte[] getData() {
        return data;
    }

}