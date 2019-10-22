package com.demo.buffer;

import com.demo.util.ByteProcessor;
import com.demo.util.ReferenceCounted;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.GatheringByteChannel;
import java.nio.channels.ScatteringByteChannel;
import java.nio.charset.Charset;

public abstract class ByteBuf implements ReferenceCounted, Comparable<ByteBuf> {


    /**
     * 容量
     *
     * @return
     */
    public abstract int capacity();

    /**
     * 新容量
     *
     * @param newCapacity
     * @return
     */
    public abstract ByteBuf capacity(int newCapacity);

    /**
     * 最大容量
     *
     * @return
     */
    public abstract int maxCapacity();

    /**
     * 分配器
     *
     * @return
     */
    public abstract ByteBufAllocator alloc();


    public abstract ByteOrder order();

    public abstract ByteBuf order(ByteOrder endianness);

    /**
     * 如果是包装buffer，直接返回基础buffer
     *
     * @return
     */
    public abstract ByteBuf unwrap();

    /**
     * 是否 直接
     *
     * @return
     */
    public abstract boolean isDirect();

    /**
     * 是否是只读
     *
     * @return
     */
    public abstract boolean isReadOnly();

    /**
     * 返回只读buffer
     *
     * @return
     */
    public abstract ByteBuf asReadOnly();

    /**
     * read index
     *
     * @return
     */
    public abstract int readerIndex();

    /**
     * set read index
     *
     * @param readerIndex
     * @return
     */
    public abstract ByteBuf readerIndex(int readerIndex);

    /**
     * 写索引
     *
     * @return
     */
    public abstract int writerIndex();

    /**
     * set 写索引
     *
     * @param writerIndex
     * @return
     */
    public abstract ByteBuf writerIndex(int writerIndex);


    public abstract ByteBuf setIndex(int readerIndex, int writerIndex);

    /**
     * 可读字节
     *
     * @return
     */
    public abstract int readableBytes();

    /**
     * 可写
     *
     * @return
     */
    public abstract int writableBytes();


    public abstract int maxWritableBytes();

    public int maxFastWritableBytes() {

        return writableBytes();
    }

    /**
     * 是否可读
     *
     * @return
     */
    public abstract boolean isReadable();

    public abstract boolean isReadable(int size);

    /**
     * 是否可写
     *
     * @return
     */
    public abstract boolean isWritable();

    public abstract boolean isWritable(int size);

    /**
     * 清空
     *
     * @return
     */
    public abstract ByteBuf clear();

    /**
     * 标记 read 索引
     *
     * @return
     */
    public abstract ByteBuf markReaderIndex();

    /**
     * reset read 索引
     *
     * @return
     */
    public abstract ByteBuf resetReaderIndex();

    /**
     * 标记 写 索引
     *
     * @return
     */
    public abstract ByteBuf markWriterIndex();

    public abstract ByteBuf resetWriterIndex();

    /**
     * 丢弃已读
     *
     * @return
     */
    public abstract ByteBuf discardReadBytes();

    public abstract ByteBuf discardSomeReadBytes();

    public abstract ByteBuf ensureWritable(int minWritableBytes);

    public abstract int ensureWritable(int minWritableBytes, boolean force);

    /**
     * get boolean
     *
     * @param index
     * @return
     */
    public abstract boolean getBoolean(int index);


    public abstract byte getByte(int index);

    public abstract short getUnsignedByte(int index);

    public abstract short getShort(int index);

    public abstract short getShortLE(int index);

    public abstract int getUnsignedShort(int index);

    public abstract int getUnsignedShortLE(int index);

    public abstract int getMedium(int index);

    public abstract int getMediumLE(int index);

    public abstract int getUnsignedMedium(int index);

    public abstract int getUnsignedMediumLE(int index);


    public abstract int getInt(int index);

    public abstract int getIntLE(int index);

    public abstract long getUnsignedInt(int index);

    public abstract long getUnsignedIntLE(int index);

    public abstract long getLong(int index);

    public abstract long getLongLE(int index);

    public abstract char getChar(int index);

    public abstract float getFloat(int index);


    public float getFloatLE(int index) {

        return Float.intBitsToFloat(getIntLE(index));
    }

    public abstract double getDouble(int index);

    public double getDoubleLE(int index) {

        return Double.longBitsToDouble(getLongLE(index));
    }

    public abstract ByteBuf getBytes(int index, ByteBuf dst);

    public abstract ByteBuf getBytes(int index, ByteBuf dst, int length);

    public abstract ByteBuf getBytes(int index, ByteBuf dst, int dstIndex, int length);

    public abstract ByteBuf getBytes(int index, byte[] dst);

    public abstract ByteBuf getBytes(int index, byte[] dst, int dstIndex, int length);

    /**
     * NIO ByteBuffer
     *
     * @param index
     * @param dst
     * @return
     */
    public abstract ByteBuf getBytes(int index, ByteBuffer dst);

    /**
     * output stream
     *
     * @param index
     * @param out
     * @param length
     * @return
     * @throws IOException
     */
    public abstract ByteBuf getBytes(int index, OutputStream out, int length) throws IOException;

    public abstract int getBytes(int index, GatheringByteChannel out, int length) throws IOException;

    public abstract int getBytes(int index, FileChannel out, long position, int length) throws IOException;

    public abstract CharSequence getCharSequence(int index, int length, Charset charset);


    public abstract ByteBuf setBoolean(int index, boolean value);

    public abstract ByteBuf setByte(int index, int value);

    public abstract ByteBuf setShort(int index, int value);

    public abstract ByteBuf setShortLE(int index, int value);

    public abstract ByteBuf setMedium(int index, int value);

    public abstract ByteBuf setMediumLE(int index, int value);

    public abstract ByteBuf setInt(int index, int value);

    public abstract ByteBuf setIntLE(int index, int value);

    public abstract ByteBuf setLong(int index, long value);

    public abstract ByteBuf setLongLE(int index, long value);

    public abstract ByteBuf setChar(int index, int value);

    public abstract ByteBuf setFloat(int index, float value);

    public ByteBuf setFloatLE(int index, float value) {

        return setIntLE(index, Float.floatToRawIntBits(value));
    }

    public abstract ByteBuf setDouble(int index, double value);

    public ByteBuf setDoubleLE(int index, double value) {

        return setLongLE(index, Double.doubleToRawLongBits(value));
    }

    public abstract ByteBuf setBytes(int index, ByteBuf src);

    public abstract ByteBuf setBytes(int index, ByteBuf src, int length);

    public abstract ByteBuf setBytes(int index, ByteBuf src, int srcIndex, int length);

    public abstract ByteBuf setBytes(int index, byte[] src);

    public abstract ByteBuf setBytes(int index, byte[] src, int srcIndex, int length);

    public abstract ByteBuf setBytes(int index, ByteBuffer src);

    public abstract int setBytes(int index, InputStream in, int length) throws IOException;

    public abstract int setBytes(int index, ScatteringByteChannel in, int length) throws IOException;

    public abstract int setBytes(int index, FileChannel in, long position, int length) throws IOException;

    public abstract ByteBuf setZero(int index, int length);

    public abstract int setCharSequence(int index, CharSequence sequence, Charset charset);

    public abstract boolean readBoolean();

    public abstract byte readByte();

    public abstract short readUnsignedByte();

    public abstract short readShort();

    public abstract short readShortLE();

    public abstract int readUnsignedShort();

    public abstract int readUnsignedShortLE();

    public abstract int readMedium();

    public abstract int readMediumLE();

    public abstract int readUnsignedMedium();

    public abstract int readUnsignedMediumLE();

    public abstract int readInt();

    public abstract int readIntLE();

    public abstract long readUnsignedInt();

    public abstract long readUnsignedIntLE();

    public abstract long readLong();

    public abstract long readLongLE();

    public abstract char readChar();

    public abstract float readFloat();

    public float readFloatLE() {

        return Float.intBitsToFloat(readIntLE());
    }

    public abstract double readDouble();

    public double readDoubleLE() {

        return Double.longBitsToDouble(readLongLE());
    }

    public abstract ByteBuf readBytes(int length);

    public abstract ByteBuf readSlice(int length);

    public abstract ByteBuf readRetainedSlice(int length);

    public abstract ByteBuf readBytes(ByteBuf dst);

    public abstract ByteBuf readBytes(ByteBuf dst, int length);

    public abstract ByteBuf readBytes(ByteBuf dst, int dstIndex, int length);

    public abstract ByteBuf readBytes(byte[] dst);

    public abstract ByteBuf readBytes(byte[] dst, int dstIndex, int length);

    public abstract ByteBuf readBytes(ByteBuffer dst);


    public abstract ByteBuf readBytes(OutputStream out, int length) throws IOException;


    public abstract int readBytes(GatheringByteChannel out, int length) throws IOException;

    public abstract CharSequence readCharSequence(int length, Charset charset);

    public abstract int readBytes(FileChannel out, long position, int length) throws IOException;

    public abstract ByteBuf skipBytes(int length);

    public abstract ByteBuf writeBoolean(boolean value);

    public abstract ByteBuf writeByte(int value);

    public abstract ByteBuf writeShort(int value);

    public abstract ByteBuf writeShortLE(int value);

    public abstract ByteBuf writeMedium(int value);

    public abstract ByteBuf writeMediumLE(int value);

    public abstract ByteBuf writeInt(int value);

    public abstract ByteBuf writeIntLE(int value);

    public abstract ByteBuf writeLong(long value);

    public abstract ByteBuf writeLongLE(long value);

    public abstract ByteBuf writeChar(int value);

    public abstract ByteBuf writeFloat(float value);

    public ByteBuf writeFloatLE(float value) {

        return writeIntLE(Float.floatToRawIntBits(value));
    }

    public abstract ByteBuf writeDouble(double value);

    public ByteBuf writeDoubleLE(double value) {

        return writeLongLE(Double.doubleToRawLongBits(value));
    }

    public abstract ByteBuf writeBytes(ByteBuf src);

    public abstract ByteBuf writeBytes(ByteBuf src, int length);

    public abstract ByteBuf writeBytes(ByteBuf src, int srcIndex, int length);

    public abstract ByteBuf writeBytes(byte[] src);

    public abstract ByteBuf writeBytes(byte[] src, int srcIndex, int length);

    public abstract ByteBuf writeBytes(ByteBuffer src);

    public abstract int writeBytes(InputStream in, int length) throws IOException;

    public abstract int writeBytes(ScatteringByteChannel in, int length) throws IOException;

    public abstract int writeBytes(FileChannel in, long position, int length) throws IOException;

    public abstract ByteBuf writeZero(int length);

    public abstract int writeCharSequence(CharSequence sequence, Charset charset);

    public abstract int indexOf(int fromIndex, int toIndex, byte value);

    public abstract int bytesBefore(byte value);

    public abstract int bytesBefore(int length, byte value);

    public abstract int bytesBefore(int index, int length, byte value);

    public abstract int forEachByte(ByteProcessor processor);

    public abstract int forEachByte(int index, int length, ByteProcessor processor);

    public abstract int forEachByteDesc(ByteProcessor processor);

    public abstract int forEachByteDesc(int index, int length, ByteProcessor processor);

    public abstract ByteBuf copy();

    public abstract ByteBuf copy(int index, int length);

    public abstract ByteBuf slice();

    public abstract ByteBuf retainedSlice();

    public abstract ByteBuf slice(int index, int length);

    public abstract ByteBuf retainedSlice(int index, int length);

    /**
     * 复制
     *
     * @return
     */
    public abstract ByteBuf duplicate();

    /**
     * 保留复制
     *
     * @return
     */
    public abstract ByteBuf retainedDuplicate();

    public abstract int nioBufferCount();

    /**
     * to ByteBuffer
     *
     * @return
     */
    public abstract ByteBuffer nioBuffer();

    public abstract ByteBuffer nioBuffer(int index, int length);

    public abstract ByteBuffer internalNioBuffer(int index, int length);

    public abstract ByteBuffer[] nioBuffers();


    public abstract ByteBuffer[] nioBuffers(int index, int length);

    public abstract boolean hasArray();

    public abstract byte[] array();

    public abstract int arrayOffset();

    public abstract boolean hasMemoryAddress();


    public abstract long memoryAddress();

    public abstract String toString(Charset charset);

    public abstract String toString(int index, int length, Charset charset);

    @Override
    public abstract int hashCode();

    @Override
    public abstract boolean equals(Object obj);

    @Override
    public abstract int compareTo(ByteBuf buffer);

    @Override
    public abstract ByteBuf retain(int increment);

    @Override
    public abstract ByteBuf retain();

    @Override
    public abstract ByteBuf touch();

    @Override
    public abstract ByteBuf touch(Object hint);

    boolean isAccessible() {
        return refCnt() != 0;
    }
}
