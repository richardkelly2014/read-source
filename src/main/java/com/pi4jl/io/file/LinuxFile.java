package com.pi4jl.io.file;

import com.pi4jl.util.NativeLibraryLoader;
import sun.misc.Cleaner;
import sun.misc.SharedSecrets;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.nio.*;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class LinuxFile extends RandomAccessFile {

    public LinuxFile(String name, String mode) throws FileNotFoundException {
        super(name, mode);
    }

    public static final int wordSize = getWordSize();
    public static final int localBufferSize = 2048; //about 1 page

    public static final ThreadLocal<ByteBuffer> localDataBuffer = new ThreadLocal<>();
    public static final ThreadLocal<IntBuffer> localOffsetsBuffer = new ThreadLocal<>();

    private static final Constructor<?> directByteBufferConstructor;

    private static final Field addressField;
    private static final Field capacityField;
    private static final Field cleanerField;

    static {
        try {
            // Load the platform library
            NativeLibraryLoader.load("libpi4j.so");

            Class<?> dbb = Class.forName("java.nio.DirectByteBuffer");

            addressField = Buffer.class.getDeclaredField("address");
            capacityField = Buffer.class.getDeclaredField("capacity");
            cleanerField = dbb.getDeclaredField("cleaner");
            directByteBufferConstructor = dbb.getDeclaredConstructor(
                    new Class[]{int.class, long.class, FileDescriptor.class, Runnable.class});

            addressField.setAccessible(true);
            capacityField.setAccessible(true);
            cleanerField.setAccessible(true);
            directByteBufferConstructor.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new InternalError(e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new InternalError(e.getMessage());
        } catch (NoSuchMethodException e) {
            throw new InternalError(e.getMessage());
        }
    }

    public void ioctl(long command, int value) throws IOException {
        final int response = directIOCTL(getFileDescriptor(), command, value);

        if (response < 0)
            throw new LinuxFileException();
    }

    public void ioctl(final long command, ByteBuffer data, IntBuffer offsets) throws IOException {
        ByteBuffer originalData = data;

        if (data == null || offsets == null)
            throw new NullPointerException("data and offsets required!");

        if (offsets.order() != ByteOrder.nativeOrder())
            throw new IllegalArgumentException("provided IntBuffer offsets ByteOrder must be native!");

        //buffers must be direct
        try {
            if (!data.isDirect()) {
                ByteBuffer newBuf = getDataBuffer(data.limit());
                int pos = data.position(); //keep position

                data.rewind();
                newBuf.clear();
                newBuf.put(data);
                newBuf.position(pos); //restore position

                data = newBuf;
            }

            if (!offsets.isDirect()) {
                IntBuffer newBuf = getOffsetsBuffer(offsets.remaining());

                newBuf.clear();
                newBuf.put(offsets);
                newBuf.flip();

                offsets = newBuf;
            }
        } catch (BufferOverflowException e) {
            throw new ScratchBufferOverrun();
        }

        if ((offsets.remaining() & 1) != 0)
            throw new IllegalArgumentException("offset buffer must be even length!");

        for (int i = offsets.position(); i < offsets.limit(); i += 2) {
            final int ptrOffset = offsets.get(i);
            final int dataOffset = offsets.get(i + 1);

            if (dataOffset >= data.capacity() || dataOffset < 0)
                throw new IndexOutOfBoundsException("invalid data offset specified in buffer: " + dataOffset);

            if ((ptrOffset + wordSize) > data.capacity() || ptrOffset < 0)
                throw new IndexOutOfBoundsException("invalid pointer offset specified in buffer: " + ptrOffset);
        }

        final int response = directIOCTLStructure(getFileDescriptor(), command, data,
                data.position(), offsets, offsets.position(), offsets.remaining());

        if (response < 0)
            throw new LinuxFileException();

        //fast forward positions
        offsets.position(offsets.limit());
        data.rewind();

        //if original data wasnt direct, copy it back in.
        if (originalData != data) {
            originalData.rewind();
            originalData.put(data);
            originalData.rewind();
        }
    }


    private int getFileDescriptor() throws IOException {
        final int fd = SharedSecrets.getJavaIOFileDescriptorAccess().get(getFD());

        if (fd < 1)
            throw new IOException("failed to get POSIX file descriptor!");

        return fd;
    }

    private static int getWordSize() {
        //TODO: there has to be a better way...
        final String archDataModel = System.getProperty("sun.arch.data.model");
        return "64".equals(archDataModel) ? 8 : 4;
    }

    @Override
    protected void finalize() throws Throwable {
        close();

        super.finalize();
    }

    private synchronized IntBuffer getOffsetsBuffer(int size) {
        final int byteSize = size * 4;
        IntBuffer buf = localOffsetsBuffer.get();

        if (byteSize > localBufferSize)
            throw new ScratchBufferOverrun();

        if (buf == null) {
            ByteBuffer bb = ByteBuffer.allocateDirect(localBufferSize);

            //keep native order, set before cast to IntBuffer
            bb.order(ByteOrder.nativeOrder());

            buf = bb.asIntBuffer();
            localOffsetsBuffer.set(buf);
        }

        return buf;
    }

    private synchronized ByteBuffer getDataBuffer(int size) {
        ByteBuffer buf = localDataBuffer.get();

        if (size > localBufferSize)
            throw new ScratchBufferOverrun();

        if (buf == null) {
            buf = ByteBuffer.allocateDirect(localBufferSize);
            localDataBuffer.set(buf);
        }

        return buf;
    }

    public static class ScratchBufferOverrun extends IllegalArgumentException {
        private static final long serialVersionUID = -418203522640826177L;

        public ScratchBufferOverrun() {
            super("Scratch buffer overrun! Provide direct ByteBuffer for data larger than " + localBufferSize + " bytes");
        }
    }

    public ByteBuffer mmap(int length, MMAPProt prot, MMAPFlags flags, int offset) throws IOException {
        long pointer = mmap(getFileDescriptor(), length, prot.flag, flags.flag, offset);

        if (pointer == -1)
            throw new LinuxFileException();

        return newMappedByteBuffer(length, pointer, () -> {
            munmapDirect(pointer, length);
        });
    }

    public static void munmap(ByteBuffer mappedBuffer) throws IOException {
        if (!mappedBuffer.isDirect())
            throw new IllegalArgumentException("Must be a mapped direct buffer");

        try {
            long address = addressField.getLong(mappedBuffer);
            int capacity = capacityField.getInt(mappedBuffer);

            if (address == 0 || capacity == 0) return;

            //reset address field first
            addressField.setLong(mappedBuffer, 0);
            capacityField.setInt(mappedBuffer, 0);

            //reset mark and position to new 0 capacity
            mappedBuffer.clear();

            //clean object so it doesnt clean on collection
            ((Cleaner) cleanerField.get(mappedBuffer)).clean();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw new InternalError(e.getMessage());
        }
    }

    private MappedByteBuffer newMappedByteBuffer(int size, long addr, Runnable unmapper) throws IOException {
        MappedByteBuffer dbb;
        try {
            dbb = (MappedByteBuffer) directByteBufferConstructor.newInstance(
                    new Object[]{new Integer(size), new Long(addr), this.getFD(), unmapper});
        } catch (InstantiationException e) {
            throw new InternalError(e.getMessage());
        } catch (IllegalAccessException e) {
            throw new InternalError(e.getMessage());
        } catch (InvocationTargetException e) {
            throw new InternalError(e.getMessage());
        }
        return dbb;
    }


    public static class LinuxFileException extends IOException {
        private static final long serialVersionUID = -2581606746434701394L;
        int code;

        public LinuxFileException() {
            this(errno());
        }

        LinuxFileException(int code) {
            super(strerror(code));

            this.code = code;
        }

        /**
         * Gets the POSIX code associated with this IO error
         *
         * @return POSIX error code
         */
        public int getCode() {
            return code;
        }
    }

    public enum MMAPProt {
        NONE(0),
        READ(1),
        WRITE(2),
        EXEC(4),
        RW(READ.flag | WRITE.flag),
        RX(READ.flag | EXEC.flag),
        RWX(READ.flag | WRITE.flag | EXEC.flag),
        WX(WRITE.flag | EXEC.flag);

        public final int flag;

        MMAPProt(int flag) {
            this.flag = flag;
        }
    }

    public enum MMAPFlags {
        SHARED(1),
        PRIVATE(2),
        SHARED_PRIVATE(SHARED.flag | PRIVATE.flag);

        public final int flag;

        MMAPFlags(int flag) {
            this.flag = flag;
        }
    }

    public static native int errno();

    public static native String strerror(int code);

    protected static native int directIOCTL(int fd, long command, int value);

    protected static native long mmap(int fd, int length, int prot, int flags, int offset);

    protected static native int munmapDirect(long address, long capacity);

    protected static native int directIOCTLStructure(int fd, long command, ByteBuffer data, int dataOffset, IntBuffer offsetMap, int offsetMapOffset, int offsetCapacity);

}
