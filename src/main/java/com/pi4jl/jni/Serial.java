package com.pi4jl.jni;

import com.pi4jl.util.NativeLibraryLoader;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

/**
 * 窜行 操作？
 * Created by jiangfei on 2019/11/17.
 */
public class Serial {

    public static final String DEFAULT_COM_PORT = "/dev/ttyAMA0";
    public static final String FIRST_USB_COM_PORT = "/dev/ttyUSB0";
    public static final String SECOND_USB_COM_PORT = "/dev/ttyUSB1";

    public static int BAUD_RATE_50 = 50;
    public static int BAUD_RATE_75 = 75;
    public static int BAUD_RATE_110 = 110;
    public static int BAUD_RATE_134 = 134;
    public static int BAUD_RATE_150 = 150;
    public static int BAUD_RATE_200 = 200;
    public static int BAUD_RATE_300 = 300;
    public static int BAUD_RATE_600 = 600;
    public static int BAUD_RATE_1200 = 1200;
    public static int BAUD_RATE_1800 = 1800;
    public static int BAUD_RATE_2400 = 2400;
    public static int BAUD_RATE_4800 = 4800;
    public static int BAUD_RATE_9600 = 9600;
    public static int BAUD_RATE_19200 = 19200;
    public static int BAUD_RATE_38400 = 38400;
    public static int BAUD_RATE_57600 = 57600;
    public static int BAUD_RATE_115200 = 115200;
    public static int BAUD_RATE_230400 = 230400;

    public static int PARITY_NONE = 0;
    public static int PARITY_ODD = 1;
    public static int PARITY_EVEN = 2;
    public static int PARITY_MARK = 3;   // NOT ALL UNIX SYSTEM SUPPORT 'MARK' PARITY; THIS IS EXPERIMENTAL
    public static int PARITY_SPACE = 4;   // NOT ALL UNIX SYSTEM SUPPORT 'SPACE' PARITY; THIS IS EXPERIMENTAL

    public static int DATA_BITS_5 = 5;
    public static int DATA_BITS_6 = 6;
    public static int DATA_BITS_7 = 7;
    public static int DATA_BITS_8 = 8;

    public static int STOP_BITS_1 = 1;
    public static int STOP_BITS_2 = 2;

    public static int FLOW_CONTROL_NONE = 0;
    public static int FLOW_CONTROL_HARDWARE = 1;
    public static int FLOW_CONTROL_SOFTWARE = 2;


    // private constructor
    private Serial() {
        // forbid object construction
    }

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    public synchronized static native int open(String device, int baud, int dataBits, int parity, int stopBits,
                                               int flowControl) throws IOException;

    public synchronized static int open(String device, int baud, int dataBits, int parity, int stopBits)
            throws IOException {
        return open(device, baud, dataBits, parity, stopBits, FLOW_CONTROL_NONE);
    }

    public synchronized static int open(String device, int baud, int dataBits, int parity) throws IOException {
        return open(device, baud, dataBits, parity, STOP_BITS_1, FLOW_CONTROL_NONE);
    }

    public synchronized static int open(String device, int baud, int dataBits) throws IOException {
        return open(device, baud, dataBits, PARITY_NONE, STOP_BITS_1, FLOW_CONTROL_NONE);
    }

    public synchronized static int open(String device, int baud) throws IOException {
        return open(device, baud, DATA_BITS_8, PARITY_NONE, STOP_BITS_1, FLOW_CONTROL_NONE);
    }

    public synchronized static native void close(int fd) throws IOException;

    public synchronized static native void discardInput(int fd) throws IOException;

    public synchronized static native void discardOutput(int fd) throws IOException;

    public synchronized static native void discardAll(int fd) throws IOException;

    public synchronized static native void flush(int fd) throws IOException;

    public synchronized static native void sendBreak(int fd, int duration) throws IOException;

    public synchronized static void sendBreak(int fd) throws IOException {
        sendBreak(fd, 0);
    }

    public synchronized static native void setBreak(int fd, boolean enabled) throws IOException;

    public synchronized static native void setRTS(int fd, boolean enabled) throws IOException;

    public synchronized static native void setDTR(int fd, boolean enabled) throws IOException;

    public synchronized static native boolean getRTS(int fd) throws IOException;

    public synchronized static native boolean getDTR(int fd) throws IOException;

    public synchronized static native boolean getCTS(int fd) throws IOException;

    public synchronized static native boolean getDSR(int fd) throws IOException;

    public synchronized static native boolean getRI(int fd) throws IOException;

    public synchronized static native boolean getCD(int fd) throws IOException;

    public synchronized static native int available(int fd);

    public synchronized static native byte[] read(int fd) throws IOException;

    public synchronized static native byte[] read(int fd, int length) throws IOException;

    public synchronized static void read(int fd, ByteBuffer buffer) throws IOException {
        byte[] data = read(fd);
        buffer.put(data);
    }

    public synchronized static void read(int fd, int length, ByteBuffer buffer) throws IOException {
        buffer.put(read(fd, length));
    }

    public synchronized static void read(int fd, OutputStream stream) throws IOException {
        stream.write(read(fd));
    }

    public synchronized static void read(int fd, int length, OutputStream stream) throws IOException {
        stream.write(read(fd, length));
    }

    public synchronized static void read(int fd, Collection<ByteBuffer> collection) throws IOException {
        collection.add(ByteBuffer.wrap(read(fd)));
    }

    public synchronized static void read(int fd, int length, Collection<ByteBuffer> collection) throws IOException {
        collection.add(ByteBuffer.wrap(read(fd)));
    }

    public synchronized static CharBuffer read(int fd, Charset charset) throws IOException {
        return charset.decode(ByteBuffer.wrap(read(fd)));
    }

    public synchronized static CharBuffer read(int fd, int length, Charset charset) throws IOException {
        return charset.decode(ByteBuffer.wrap(read(fd, length)));
    }

    public synchronized static void read(int fd, Charset charset, Writer writer) throws IOException {
        writer.write(read(fd, charset).toString());
    }

    public synchronized static void read(int fd, int length, Charset charset, Writer writer) throws IOException {
        writer.write(read(fd, length, charset).toString());
    }

    private synchronized static native void write(int fd, byte[] data, long length) throws IOException;

    public synchronized static void write(int fd, byte[] data, int offset, int length) throws IOException {

        // we make a copy of the data argument because we don't want to modify the original source data
        byte[] buffer = new byte[length];
        System.arraycopy(data, offset, buffer, 0, length);

        // write the buffer contents to the serial port via JNI native method
        write(fd, buffer, length);
    }

    public synchronized static void write(int fd, byte... data) throws IOException {

        // write the data contents to the serial port via JNI native method
        write(fd, data, data.length);
    }

    public synchronized static void write(int fd, byte[]... data) throws IOException {
        for (byte[] single : data) {
            // write the data contents to the serial port via JNI native method
            write(fd, single, single.length);
        }
    }

    public synchronized static void write(int fd, ByteBuffer... data) throws IOException {

        // write each byte buffer to the serial port
        for (ByteBuffer single : data) {

            // read the byte buffer from the current position up to the limit
            byte[] payload = new byte[single.remaining()];
            single.get(payload);

            // write the data contents to the serial port via JNI native method
            write(fd, payload, payload.length);
        }
    }

    public synchronized static void write(int fd, InputStream input) throws IOException {

        // ensure bytes are available
        if (input.available() <= 0) {
            throw new IOException("No available bytes in input stream to write to serial port.");
        }

        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int length;
        byte[] data = new byte[1024];
        while ((length = input.read(data, 0, data.length)) != -1) {
            buffer.write(data, 0, length);
        }
        buffer.flush();

        // write bytes to serial port
        write(fd, buffer.toByteArray(), buffer.size());
    }

    public synchronized static void write(int fd, Charset charset, char[] data, int offset, int length) throws IOException {

        // write the buffer contents to the serial port via JNI native method
        write(fd, charset, CharBuffer.wrap(data, offset, length));
    }

    public synchronized static void write(int fd, Charset charset, char... data) throws IOException {

        // write the buffer contents to the serial port via JNI native method
        write(fd, charset, CharBuffer.wrap(data));
    }

    public synchronized static void write(int fd, char... data) throws IOException {

        // write the buffer contents to the serial port via JNI native method
        write(fd, StandardCharsets.US_ASCII, CharBuffer.wrap(data));
    }

    public synchronized static void write(int fd, Charset charset, CharBuffer... data) throws IllegalStateException, IOException {
        for (CharBuffer single : data) {
            write(fd, charset.encode(single));
        }
    }

    public synchronized static void write(int fd, CharBuffer... data) throws IllegalStateException, IOException {
        write(fd, StandardCharsets.US_ASCII, data);
    }

    public synchronized static void write(int fd, Charset charset, CharSequence... data) throws IllegalStateException, IOException {
        for (CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single)));
        }
    }

    public synchronized static void write(int fd, CharSequence... data) throws IllegalStateException, IOException {
        write(fd, StandardCharsets.US_ASCII, data);
    }

    public synchronized static void write(int fd, Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        for (CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single)));
        }
    }

    public synchronized static void write(int fd, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        write(fd, StandardCharsets.US_ASCII, data);
    }

    public synchronized static void writeln(int fd, Charset charset, CharSequence... data) throws IllegalStateException, IOException {
        for (CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single + "\r\n")));
        }
    }

    public synchronized static void writeln(int fd, CharSequence... data) throws IllegalStateException, IOException {
        writeln(fd, StandardCharsets.US_ASCII, data);
    }

    public synchronized static void writeln(int fd, Charset charset, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        for (CharSequence single : data) {
            write(fd, charset.encode(CharBuffer.wrap(single + "\r\n")));
        }
    }

    public synchronized static void writeln(int fd, Collection<? extends CharSequence> data) throws IllegalStateException, IOException {
        writeln(fd, StandardCharsets.US_ASCII, data);
    }
}
