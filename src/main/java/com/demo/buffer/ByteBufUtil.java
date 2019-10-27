package com.demo.buffer;

import com.demo.util.AsciiString;
import com.demo.util.CharsetUtil;
import com.demo.util.Recycler;
import com.demo.util.Recycler.Handle;
import com.demo.util.concurrent.FastThreadLocal;
import com.demo.util.internal.PlatformDependent;
import com.demo.util.internal.StringUtil;
import com.demo.util.internal.SystemPropertyUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.util.Arrays;
import java.util.Locale;

import static com.demo.util.internal.MathUtil.isOutOfBounds;
import static com.demo.util.internal.ObjectUtil.checkPositiveOrZero;
import static com.demo.util.internal.StringUtil.NEWLINE;
import static com.demo.util.internal.StringUtil.isSurrogate;

/**
 * Created by jiangfei on 2019/10/23.
 */
public final class ByteBufUtil {

    private static final FastThreadLocal<byte[]> BYTE_ARRAYS = new FastThreadLocal<byte[]>() {
        @Override
        protected byte[] initialValue() throws Exception {
            return PlatformDependent.allocateUninitializedArray(MAX_TL_ARRAY_LEN);
        }
    };

    private static final byte WRITE_UTF_UNKNOWN = (byte) '?';
    private static final int MAX_CHAR_BUFFER_SIZE;
    private static final int THREAD_LOCAL_BUFFER_SIZE;
    private static final int MAX_BYTES_PER_CHAR_UTF8 =
            (int) CharsetUtil.encoder(CharsetUtil.UTF_8).maxBytesPerChar();

    static final int WRITE_CHUNK_SIZE = 8192;
    static final ByteBufAllocator DEFAULT_ALLOCATOR;

    static final int MAX_TL_ARRAY_LEN = 1024;

    static {
        String allocType = SystemPropertyUtil.get(
                "io.netty.allocator.type", PlatformDependent.isAndroid() ? "unpooled" : "pooled");
        allocType = allocType.toLowerCase(Locale.US).trim();

        ByteBufAllocator alloc;
        if ("unpooled".equals(allocType)) {
            alloc = UnpooledByteBufAllocator.DEFAULT;
            //logger.debug("-Dio.netty.allocator.type: {}", allocType);
        } else if ("pooled".equals(allocType)) {
            //todo
            //alloc = PooledByteBufAllocator.DEFAULT;
            //    logger.debug("-Dio.netty.allocator.type: {}", allocType);
        } else {
            //alloc = PooledByteBufAllocator.DEFAULT;
            //  logger.debug("-Dio.netty.allocator.type: pooled (unknown: {})", allocType);
        }
        alloc = UnpooledByteBufAllocator.DEFAULT;

        DEFAULT_ALLOCATOR = alloc;

        THREAD_LOCAL_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.threadLocalDirectBufferSize", 0);
        //logger.debug("-Dio.netty.threadLocalDirectBufferSize: {}", THREAD_LOCAL_BUFFER_SIZE);

        MAX_CHAR_BUFFER_SIZE = SystemPropertyUtil.getInt("io.netty.maxThreadLocalCharBufferSize", 16 * 1024);
//        logger.debug("-Dio.netty.maxThreadLocalCharBufferSize: {}", MAX_CHAR_BUFFER_SIZE);
    }

    static byte[] threadLocalTempArray(int minLength) {
        return minLength <= MAX_TL_ARRAY_LEN ? BYTE_ARRAYS.get()
                : PlatformDependent.allocateUninitializedArray(minLength);
    }

    /**
     * Returns a <a href="http://en.wikipedia.org/wiki/Hex_dump">hex dump</a>
     * of the specified buffer's readable bytes.
     */
    public static String hexDump(ByteBuf buffer) {
        return hexDump(buffer, buffer.readerIndex(), buffer.readableBytes());
    }

    /**
     * Returns a <a href="http://en.wikipedia.org/wiki/Hex_dump">hex dump</a>
     * of the specified buffer's sub-region.
     */
    public static String hexDump(ByteBuf buffer, int fromIndex, int length) {
        return HexUtil.hexDump(buffer, fromIndex, length);
    }

    /**
     * Returns a <a href="http://en.wikipedia.org/wiki/Hex_dump">hex dump</a>
     * of the specified byte array.
     */
    public static String hexDump(byte[] array) {
        return hexDump(array, 0, array.length);
    }

    /**
     * Returns a <a href="http://en.wikipedia.org/wiki/Hex_dump">hex dump</a>
     * of the specified byte array's sub-region.
     */
    public static String hexDump(byte[] array, int fromIndex, int length) {
        return HexUtil.hexDump(array, fromIndex, length);
    }


    public static int hashCode(ByteBuf buffer) {
        final int aLen = buffer.readableBytes();
        final int intCount = aLen >>> 2;
        final int byteCount = aLen & 3;

        int hashCode = EmptyByteBuf.EMPTY_BYTE_BUF_HASH_CODE;
        int arrayIndex = buffer.readerIndex();
        if (buffer.order() == ByteOrder.BIG_ENDIAN) {
            for (int i = intCount; i > 0; i--) {
                hashCode = 31 * hashCode + buffer.getInt(arrayIndex);
                arrayIndex += 4;
            }
        } else {
            for (int i = intCount; i > 0; i--) {
                hashCode = 31 * hashCode + swapInt(buffer.getInt(arrayIndex));
                arrayIndex += 4;
            }
        }

        for (int i = byteCount; i > 0; i--) {
            hashCode = 31 * hashCode + buffer.getByte(arrayIndex++);
        }

        if (hashCode == 0) {
            hashCode = 1;
        }

        return hashCode;
    }

    public static short swapShort(short value) {
        return Short.reverseBytes(value);
    }

    /**
     * Toggles the endianness of the specified 24-bit medium integer.
     */
    public static int swapMedium(int value) {
        int swapped = value << 16 & 0xff0000 | value & 0xff00 | value >>> 16 & 0xff;
        if ((swapped & 0x800000) != 0) {
            swapped |= 0xff000000;
        }
        return swapped;
    }

    public static int swapInt(int value) {
        return Integer.reverseBytes(value);
    }

    public static long swapLong(long value) {
        return Long.reverseBytes(value);
    }

    public static boolean equals(ByteBuf a, int aStartIndex, ByteBuf b, int bStartIndex, int length) {
        if (aStartIndex < 0 || bStartIndex < 0 || length < 0) {
            throw new IllegalArgumentException("All indexes and lengths must be non-negative");
        }
        if (a.writerIndex() - length < aStartIndex || b.writerIndex() - length < bStartIndex) {
            return false;
        }

        final int longCount = length >>> 3;
        final int byteCount = length & 7;

        if (a.order() == b.order()) {
            for (int i = longCount; i > 0; i--) {
                if (a.getLong(aStartIndex) != b.getLong(bStartIndex)) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        } else {
            for (int i = longCount; i > 0; i--) {
                if (a.getLong(aStartIndex) != swapLong(b.getLong(bStartIndex))) {
                    return false;
                }
                aStartIndex += 8;
                bStartIndex += 8;
            }
        }

        for (int i = byteCount; i > 0; i--) {
            if (a.getByte(aStartIndex) != b.getByte(bStartIndex)) {
                return false;
            }
            aStartIndex++;
            bStartIndex++;
        }

        return true;
    }

    public static boolean equals(ByteBuf bufferA, ByteBuf bufferB) {
        final int aLen = bufferA.readableBytes();
        if (aLen != bufferB.readableBytes()) {
            return false;
        }
        return equals(bufferA, bufferA.readerIndex(), bufferB, bufferB.readerIndex(), aLen);
    }

    public static int compare(ByteBuf bufferA, ByteBuf bufferB) {
        final int aLen = bufferA.readableBytes();
        final int bLen = bufferB.readableBytes();
        final int minLength = Math.min(aLen, bLen);
        final int uintCount = minLength >>> 2;
        final int byteCount = minLength & 3;
        int aIndex = bufferA.readerIndex();
        int bIndex = bufferB.readerIndex();

        if (uintCount > 0) {
            boolean bufferAIsBigEndian = bufferA.order() == ByteOrder.BIG_ENDIAN;
            final long res;
            int uintCountIncrement = uintCount << 2;

            if (bufferA.order() == bufferB.order()) {
                res = bufferAIsBigEndian ? compareUintBigEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) :
                        compareUintLittleEndian(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            } else {
                res = bufferAIsBigEndian ? compareUintBigEndianA(bufferA, bufferB, aIndex, bIndex, uintCountIncrement) :
                        compareUintBigEndianB(bufferA, bufferB, aIndex, bIndex, uintCountIncrement);
            }
            if (res != 0) {
                // Ensure we not overflow when cast
                return (int) Math.min(Integer.MAX_VALUE, Math.max(Integer.MIN_VALUE, res));
            }
            aIndex += uintCountIncrement;
            bIndex += uintCountIncrement;
        }

        for (int aEnd = aIndex + byteCount; aIndex < aEnd; ++aIndex, ++bIndex) {
            int comp = bufferA.getUnsignedByte(aIndex) - bufferB.getUnsignedByte(bIndex);
            if (comp != 0) {
                return comp;
            }
        }

        return aLen - bLen;
    }

    private static long compareUintBigEndian(
            ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

    private static long compareUintLittleEndian(
            ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedIntLE(bIndex);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

    private static long compareUintBigEndianA(
            ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            long comp = bufferA.getUnsignedInt(aIndex) - bufferB.getUnsignedIntLE(bIndex);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

    private static long compareUintBigEndianB(
            ByteBuf bufferA, ByteBuf bufferB, int aIndex, int bIndex, int uintCountIncrement) {
        for (int aEnd = aIndex + uintCountIncrement; aIndex < aEnd; aIndex += 4, bIndex += 4) {
            long comp = bufferA.getUnsignedIntLE(aIndex) - bufferB.getUnsignedInt(bIndex);
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }

    /**
     * ByteBuf to byte[]
     *
     * @param buf
     * @param start
     * @param length
     * @param copy
     * @return
     */
    public static byte[] getBytes(ByteBuf buf, int start, int length, boolean copy) {
        int capacity = buf.capacity();
        if (isOutOfBounds(start, length, capacity)) {
            throw new IndexOutOfBoundsException("expected: " + "0 <= start(" + start + ") <= start + length(" + length
                    + ") <= " + "buf.capacity(" + capacity + ')');
        }

        if (buf.hasArray()) {
            if (copy || start != 0 || length != capacity) {
                int baseOffset = buf.arrayOffset() + start;
                return Arrays.copyOfRange(buf.array(), baseOffset, baseOffset + length);
            } else {
                return buf.array();
            }
        }

        byte[] v = PlatformDependent.allocateUninitializedArray(length);
        buf.getBytes(start, v);
        return v;
    }


    static int writeAscii(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len) {

        // We can use the _set methods as these not need to do any index checks and reference checks.
        // This is possible as we called ensureWritable(...) before.
        for (int i = 0; i < len; i++) {
            buffer._setByte(writerIndex++, AsciiString.c2b(seq.charAt(i)));
        }
        return len;
    }

    static int writeUtf8(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int len) {
        return writeUtf8(buffer, writerIndex, seq, 0, len);
    }

    // Fast-Path implementation
    static int writeUtf8(AbstractByteBuf buffer, int writerIndex, CharSequence seq, int start, int end) {
        int oldWriterIndex = writerIndex;

        // We can use the _set methods as these not need to do any index checks and reference checks.
        // This is possible as we called ensureWritable(...) before.
        for (int i = start; i < end; i++) {
            char c = seq.charAt(i);
            if (c < 0x80) {
                buffer._setByte(writerIndex++, (byte) c);
            } else if (c < 0x800) {
                buffer._setByte(writerIndex++, (byte) (0xc0 | (c >> 6)));
                buffer._setByte(writerIndex++, (byte) (0x80 | (c & 0x3f)));
            } else if (isSurrogate(c)) {
                if (!Character.isHighSurrogate(c)) {
                    buffer._setByte(writerIndex++, WRITE_UTF_UNKNOWN);
                    continue;
                }
                // Surrogate Pair consumes 2 characters.
                if (++i == end) {
                    buffer._setByte(writerIndex++, WRITE_UTF_UNKNOWN);
                    break;
                }
                // Extra method to allow inlining the rest of writeUtf8 which is the most likely code path.
                writerIndex = writeUtf8Surrogate(buffer, writerIndex, c, seq.charAt(i));
            } else {
                buffer._setByte(writerIndex++, (byte) (0xe0 | (c >> 12)));
                buffer._setByte(writerIndex++, (byte) (0x80 | ((c >> 6) & 0x3f)));
                buffer._setByte(writerIndex++, (byte) (0x80 | (c & 0x3f)));
            }
        }
        return writerIndex - oldWriterIndex;
    }

    private static int writeUtf8Surrogate(AbstractByteBuf buffer, int writerIndex, char c, char c2) {
        if (!Character.isLowSurrogate(c2)) {
            buffer._setByte(writerIndex++, WRITE_UTF_UNKNOWN);
            buffer._setByte(writerIndex++, Character.isHighSurrogate(c2) ? WRITE_UTF_UNKNOWN : c2);
            return writerIndex;
        }
        int codePoint = Character.toCodePoint(c, c2);
        // See http://www.unicode.org/versions/Unicode7.0.0/ch03.pdf#G2630.
        buffer._setByte(writerIndex++, (byte) (0xf0 | (codePoint >> 18)));
        buffer._setByte(writerIndex++, (byte) (0x80 | ((codePoint >> 12) & 0x3f)));
        buffer._setByte(writerIndex++, (byte) (0x80 | ((codePoint >> 6) & 0x3f)));
        buffer._setByte(writerIndex++, (byte) (0x80 | (codePoint & 0x3f)));
        return writerIndex;
    }

    public static int utf8MaxBytes(final int seqLength) {
        return seqLength * MAX_BYTES_PER_CHAR_UTF8;
    }

    /**
     * Returns max bytes length of UTF8 character sequence.
     * <p>
     * It behaves like {@link #utf8MaxBytes(int)} applied to {@code seq} {@link CharSequence#length()}.
     */
    public static int utf8MaxBytes(CharSequence seq) {
        return utf8MaxBytes(seq.length());
    }

    public static ByteBuf encodeString(ByteBufAllocator alloc, CharBuffer src, Charset charset, int extraCapacity) {
        return encodeString0(alloc, false, src, charset, extraCapacity);
    }

    static ByteBuf encodeString0(ByteBufAllocator alloc, boolean enforceHeap, CharBuffer src, Charset charset,
                                 int extraCapacity) {
        final CharsetEncoder encoder = CharsetUtil.encoder(charset);
        int length = (int) ((double) src.remaining() * encoder.maxBytesPerChar()) + extraCapacity;
        boolean release = true;
        final ByteBuf dst;
        if (enforceHeap) {
            dst = alloc.heapBuffer(length);
        } else {
            dst = alloc.buffer(length);
        }
        try {
            final ByteBuffer dstBuf = dst.internalNioBuffer(dst.readerIndex(), length);
            final int pos = dstBuf.position();
            CoderResult cr = encoder.encode(src, dstBuf, true);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            cr = encoder.flush(dstBuf);
            if (!cr.isUnderflow()) {
                cr.throwException();
            }
            dst.writerIndex(dst.writerIndex() + dstBuf.position() - pos);
            release = false;
            return dst;
        } catch (CharacterCodingException x) {
            throw new IllegalStateException(x);
        } finally {
            if (release) {
                dst.release();
            }
        }
    }

    static String decodeString(ByteBuf src, int readerIndex, int len, Charset charset) {
        if (len == 0) {
            return StringUtil.EMPTY_STRING;
        }
        final byte[] array;
        final int offset;

        if (src.hasArray()) {
            array = src.array();
            offset = src.arrayOffset() + readerIndex;
        } else {
            array = threadLocalTempArray(len);
            offset = 0;
            src.getBytes(readerIndex, array, 0, len);
        }
        if (CharsetUtil.US_ASCII.equals(charset)) {
            // Fast-path for US-ASCII which is used frequently.
            return new String(array, 0, offset, len);
        }
        return new String(array, offset, len, charset);
    }

    public static ByteBuf threadLocalDirectBuffer() {
        if (THREAD_LOCAL_BUFFER_SIZE <= 0) {
            return null;
        }

        if (PlatformDependent.hasUnsafe()) {
            return ThreadLocalUnsafeDirectByteBuf.newInstance();
        } else {
            return ThreadLocalDirectByteBuf.newInstance();
        }
    }


    static void readBytes(ByteBufAllocator allocator, ByteBuffer buffer, int position, int length, OutputStream out)
            throws IOException {
        if (buffer.hasArray()) {
            out.write(buffer.array(), position + buffer.arrayOffset(), length);
        } else {
            int chunkLen = Math.min(length, WRITE_CHUNK_SIZE);
            buffer.clear().position(position);

            if (length <= MAX_TL_ARRAY_LEN || !allocator.isDirectBufferPooled()) {
                getBytes(buffer, threadLocalTempArray(chunkLen), 0, chunkLen, out, length);
            } else {
                // if direct buffers are pooled chances are good that heap buffers are pooled as well.
                ByteBuf tmpBuf = allocator.heapBuffer(chunkLen);
                try {
                    byte[] tmp = tmpBuf.array();
                    int offset = tmpBuf.arrayOffset();
                    getBytes(buffer, tmp, offset, chunkLen, out, length);
                } finally {
                    tmpBuf.release();
                }
            }
        }
    }

    private static void getBytes(ByteBuffer inBuffer, byte[] in, int inOffset, int inLen, OutputStream out, int outLen)
            throws IOException {
        do {
            int len = Math.min(inLen, outLen);
            inBuffer.get(in, inOffset, len);
            out.write(in, inOffset, len);
            outLen -= len;
        } while (outLen > 0);
    }


    private static final class HexUtil {

        private static final char[] BYTE2CHAR = new char[256];
        private static final char[] HEXDUMP_TABLE = new char[256 * 4];
        private static final String[] HEXPADDING = new String[16];
        private static final String[] HEXDUMP_ROWPREFIXES = new String[65536 >>> 4];
        private static final String[] BYTE2HEX = new String[256];
        private static final String[] BYTEPADDING = new String[16];

        static {
            final char[] DIGITS = "0123456789abcdef".toCharArray();
            for (int i = 0; i < 256; i ++) {
                HEXDUMP_TABLE[ i << 1     ] = DIGITS[i >>> 4 & 0x0F];
                HEXDUMP_TABLE[(i << 1) + 1] = DIGITS[i       & 0x0F];
            }

            int i;

            // Generate the lookup table for hex dump paddings
            for (i = 0; i < HEXPADDING.length; i ++) {
                int padding = HEXPADDING.length - i;
                StringBuilder buf = new StringBuilder(padding * 3);
                for (int j = 0; j < padding; j ++) {
                    buf.append("   ");
                }
                HEXPADDING[i] = buf.toString();
            }

            // Generate the lookup table for the start-offset header in each row (up to 64KiB).
            for (i = 0; i < HEXDUMP_ROWPREFIXES.length; i ++) {
                StringBuilder buf = new StringBuilder(12);
                buf.append(NEWLINE);
                buf.append(Long.toHexString(i << 4 & 0xFFFFFFFFL | 0x100000000L));
                buf.setCharAt(buf.length() - 9, '|');
                buf.append('|');
                HEXDUMP_ROWPREFIXES[i] = buf.toString();
            }

            // Generate the lookup table for byte-to-hex-dump conversion
            for (i = 0; i < BYTE2HEX.length; i ++) {
                BYTE2HEX[i] = ' ' + StringUtil.byteToHexStringPadded(i);
            }

            // Generate the lookup table for byte dump paddings
            for (i = 0; i < BYTEPADDING.length; i ++) {
                int padding = BYTEPADDING.length - i;
                StringBuilder buf = new StringBuilder(padding);
                for (int j = 0; j < padding; j ++) {
                    buf.append(' ');
                }
                BYTEPADDING[i] = buf.toString();
            }

            // Generate the lookup table for byte-to-char conversion
            for (i = 0; i < BYTE2CHAR.length; i ++) {
                if (i <= 0x1f || i >= 0x7f) {
                    BYTE2CHAR[i] = '.';
                } else {
                    BYTE2CHAR[i] = (char) i;
                }
            }
        }

        private static String hexDump(ByteBuf buffer, int fromIndex, int length) {
            checkPositiveOrZero(length, "length");
            if (length == 0) {
                return "";
            }

            int endIndex = fromIndex + length;
            char[] buf = new char[length << 1];

            int srcIdx = fromIndex;
            int dstIdx = 0;
            for (; srcIdx < endIndex; srcIdx ++, dstIdx += 2) {
                System.arraycopy(
                        HEXDUMP_TABLE, buffer.getUnsignedByte(srcIdx) << 1,
                        buf, dstIdx, 2);
            }

            return new String(buf);
        }

        private static String hexDump(byte[] array, int fromIndex, int length) {
            checkPositiveOrZero(length, "length");
            if (length == 0) {
                return "";
            }

            int endIndex = fromIndex + length;
            char[] buf = new char[length << 1];

            int srcIdx = fromIndex;
            int dstIdx = 0;
            for (; srcIdx < endIndex; srcIdx ++, dstIdx += 2) {
                System.arraycopy(
                        HEXDUMP_TABLE, (array[srcIdx] & 0xFF) << 1,
                        buf, dstIdx, 2);
            }

            return new String(buf);
        }

        private static String prettyHexDump(ByteBuf buffer, int offset, int length) {
            if (length == 0) {
                return StringUtil.EMPTY_STRING;
            } else {
                int rows = length / 16 + ((length & 15) == 0? 0 : 1) + 4;
                StringBuilder buf = new StringBuilder(rows * 80);
                appendPrettyHexDump(buf, buffer, offset, length);
                return buf.toString();
            }
        }

        private static void appendPrettyHexDump(StringBuilder dump, ByteBuf buf, int offset, int length) {
            if (isOutOfBounds(offset, length, buf.capacity())) {
                throw new IndexOutOfBoundsException(
                        "expected: " + "0 <= offset(" + offset + ") <= offset + length(" + length
                                + ") <= " + "buf.capacity(" + buf.capacity() + ')');
            }
            if (length == 0) {
                return;
            }
            dump.append(
                    "         +-------------------------------------------------+" +
                            NEWLINE + "         |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |" +
                            NEWLINE + "+--------+-------------------------------------------------+----------------+");

            final int startIndex = offset;
            final int fullRows = length >>> 4;
            final int remainder = length & 0xF;

            // Dump the rows which have 16 bytes.
            for (int row = 0; row < fullRows; row ++) {
                int rowStartIndex = (row << 4) + startIndex;

                // Per-row prefix.
                appendHexDumpRowPrefix(dump, row, rowStartIndex);

                // Hex dump
                int rowEndIndex = rowStartIndex + 16;
                for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                    dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
                }
                dump.append(" |");

                // ASCII dump
                for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                    dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
                }
                dump.append('|');
            }

            // Dump the last row which has less than 16 bytes.
            if (remainder != 0) {
                int rowStartIndex = (fullRows << 4) + startIndex;
                appendHexDumpRowPrefix(dump, fullRows, rowStartIndex);

                // Hex dump
                int rowEndIndex = rowStartIndex + remainder;
                for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                    dump.append(BYTE2HEX[buf.getUnsignedByte(j)]);
                }
                dump.append(HEXPADDING[remainder]);
                dump.append(" |");

                // Ascii dump
                for (int j = rowStartIndex; j < rowEndIndex; j ++) {
                    dump.append(BYTE2CHAR[buf.getUnsignedByte(j)]);
                }
                dump.append(BYTEPADDING[remainder]);
                dump.append('|');
            }

            dump.append(NEWLINE +
                    "+--------+-------------------------------------------------+----------------+");
        }

        private static void appendHexDumpRowPrefix(StringBuilder dump, int row, int rowStartIndex) {
            if (row < HEXDUMP_ROWPREFIXES.length) {
                dump.append(HEXDUMP_ROWPREFIXES[row]);
            } else {
                dump.append(NEWLINE);
                dump.append(Long.toHexString(rowStartIndex & 0xFFFFFFFFL | 0x100000000L));
                dump.setCharAt(dump.length() - 9, '|');
                dump.append('|');
            }
        }
    }



    static final class ThreadLocalUnsafeDirectByteBuf extends UnpooledUnsafeDirectByteBuf {

        private static final Recycler<ThreadLocalUnsafeDirectByteBuf> RECYCLER =
                new Recycler<ThreadLocalUnsafeDirectByteBuf>() {
                    @Override
                    protected ThreadLocalUnsafeDirectByteBuf newObject(Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
                        return new ThreadLocalUnsafeDirectByteBuf(handle);
                    }
                };

        static ThreadLocalUnsafeDirectByteBuf newInstance() {
            ThreadLocalUnsafeDirectByteBuf buf = RECYCLER.get();
            buf.resetRefCnt();
            return buf;
        }

        private final Handle<ThreadLocalUnsafeDirectByteBuf> handle;

        private ThreadLocalUnsafeDirectByteBuf(Handle<ThreadLocalUnsafeDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }

        @Override
        protected void deallocate() {
            if (capacity() > THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                clear();
                handle.recycle(this);
            }
        }
    }

    static final class ThreadLocalDirectByteBuf extends UnpooledDirectByteBuf {

        private static final Recycler<ThreadLocalDirectByteBuf> RECYCLER = new Recycler<ThreadLocalDirectByteBuf>() {
            @Override
            protected ThreadLocalDirectByteBuf newObject(Handle<ThreadLocalDirectByteBuf> handle) {
                return new ThreadLocalDirectByteBuf(handle);
            }
        };

        static ThreadLocalDirectByteBuf newInstance() {
            ThreadLocalDirectByteBuf buf = RECYCLER.get();
            buf.resetRefCnt();
            return buf;
        }

        private final Recycler.Handle<ThreadLocalDirectByteBuf> handle;

        private ThreadLocalDirectByteBuf(Handle<ThreadLocalDirectByteBuf> handle) {
            super(UnpooledByteBufAllocator.DEFAULT, 256, Integer.MAX_VALUE);
            this.handle = handle;
        }

        @Override
        protected void deallocate() {
            if (capacity() > THREAD_LOCAL_BUFFER_SIZE) {
                super.deallocate();
            } else {
                clear();
                handle.recycle(this);
            }
        }
    }

}
