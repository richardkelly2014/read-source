package com.demo.util;

import com.demo.util.internal.PlatformDependent;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.Arrays;

import static com.demo.util.internal.MathUtil.isOutOfBounds;

public final class AsciiString implements CharSequence {
    private static final char MAX_CHAR_VALUE = 255;
    public static final int INDEX_NOT_FOUND = -1;


    private final byte[] value;
    private final int offset;
    private final int length;
    private int hash;
    private String string;

    public AsciiString(byte[] value) {
        this(value, true);
    }

    /**
     * Initialize this byte string based upon a byte array.
     * {@code copy} determines if a copy is made or the array is shared.
     */
    public AsciiString(byte[] value, boolean copy) {
        this(value, 0, value.length, copy);
    }

    /**
     * Construct a new instance from a {@code byte[]} array.
     *
     * @param copy {@code true} then a copy of the memory will be made. {@code false} the underlying memory
     *             will be shared.
     */
    public AsciiString(byte[] value, int start, int length, boolean copy) {
        if (copy) {
            this.value = Arrays.copyOfRange(value, start, start + length);
            this.offset = 0;
        } else {
            if (isOutOfBounds(start, length, value.length)) {
                throw new IndexOutOfBoundsException("expected: " + "0 <= start(" + start + ") <= start + length(" +
                        length + ") <= " + "value.length(" + value.length + ')');
            }
            this.value = value;
            this.offset = start;
        }
        this.length = length;
    }

    /**
     * Create a copy of {@code value} into this instance assuming ASCII encoding.
     */
    public AsciiString(char[] value) {
        this(value, 0, value.length);
    }

    /**
     * Create a copy of {@code value} into this instance assuming ASCII encoding.
     * The copy will start at index {@code start} and copy {@code length} bytes.
     */
    public AsciiString(char[] value, int start, int length) {
        if (isOutOfBounds(start, length, value.length)) {
            throw new IndexOutOfBoundsException("expected: " + "0 <= start(" + start + ") <= start + length(" + length
                    + ") <= " + "value.length(" + value.length + ')');
        }

        this.value = PlatformDependent.allocateUninitializedArray(length);
        for (int i = 0, j = start; i < length; i++, j++) {
            this.value[i] = c2b(value[j]);
        }
        this.offset = 0;
        this.length = length;
    }

    /**
     * Create a copy of {@code value} into this instance using the encoding type of {@code charset}.
     */
    public AsciiString(char[] value, Charset charset) {
        this(value, charset, 0, value.length);
    }

    /**
     * Create a copy of {@code value} into a this instance using the encoding type of {@code charset}.
     * The copy will start at index {@code start} and copy {@code length} bytes.
     */
    public AsciiString(char[] value, Charset charset, int start, int length) {
        CharBuffer cbuf = CharBuffer.wrap(value, start, length);
        CharsetEncoder encoder = CharsetUtil.encoder(charset);
        ByteBuffer nativeBuffer = ByteBuffer.allocate((int) (encoder.maxBytesPerChar() * length));
        encoder.encode(cbuf, nativeBuffer, true);
        final int bufferOffset = nativeBuffer.arrayOffset();
        this.value = Arrays.copyOfRange(nativeBuffer.array(), bufferOffset, bufferOffset + nativeBuffer.position());
        this.offset = 0;
        this.length = this.value.length;
    }

    /**
     * Create a copy of {@code value} into this instance assuming ASCII encoding.
     */
    public AsciiString(CharSequence value) {
        this(value, 0, value.length());
    }

    /**
     * Create a copy of {@code value} into this instance assuming ASCII encoding.
     * The copy will start at index {@code start} and copy {@code length} bytes.
     */
    public AsciiString(CharSequence value, int start, int length) {
        if (isOutOfBounds(start, length, value.length())) {
            throw new IndexOutOfBoundsException("expected: " + "0 <= start(" + start + ") <= start + length(" + length
                    + ") <= " + "value.length(" + value.length() + ')');
        }

        this.value = PlatformDependent.allocateUninitializedArray(length);
        for (int i = 0, j = start; i < length; i++, j++) {
            this.value[i] = c2b(value.charAt(j));
        }
        this.offset = 0;
        this.length = length;
    }

    /**
     * Create a copy of {@code value} into this instance using the encoding type of {@code charset}.
     */
    public AsciiString(CharSequence value, Charset charset) {
        this(value, charset, 0, value.length());
    }

    /**
     * Create a copy of {@code value} into this instance using the encoding type of {@code charset}.
     * The copy will start at index {@code start} and copy {@code length} bytes.
     */
    public AsciiString(CharSequence value, Charset charset, int start, int length) {
        CharBuffer cbuf = CharBuffer.wrap(value, start, start + length);
        CharsetEncoder encoder = CharsetUtil.encoder(charset);
        ByteBuffer nativeBuffer = ByteBuffer.allocate((int) (encoder.maxBytesPerChar() * length));
        encoder.encode(cbuf, nativeBuffer, true);
        final int offset = nativeBuffer.arrayOffset();
        this.value = Arrays.copyOfRange(nativeBuffer.array(), offset, offset + nativeBuffer.position());
        this.offset = 0;
        this.length = this.value.length;
    }

    @Override
    public int length() {
        return length;
    }

    @Override
    public char charAt(int index) {
        return b2c(byteAt(index));
    }

    public byte byteAt(int index) {
        // We must do a range check here to enforce the access does not go outside our sub region of the array.
        // We rely on the array access itself to pick up the array out of bounds conditions
        if (index < 0 || index >= length) {
            throw new IndexOutOfBoundsException("index: " + index + " must be in the range [0," + length + ")");
        }
        // Try to use unsafe to avoid double checking the index bounds
        if (PlatformDependent.hasUnsafe()) {
            return PlatformDependent.getByte(value, index + offset);
        }
        return value[index + offset];
    }

    @Override
    public CharSequence subSequence(int start, int end) {
        return null;
    }


    public static int indexOf(final CharSequence cs, final char searchChar, int start) {
        if (cs instanceof String) {
            return ((String) cs).indexOf(searchChar, start);
        } else if (cs instanceof AsciiString) {
            return ((AsciiString) cs).indexOf(searchChar, start);
        }
        if (cs == null) {
            return INDEX_NOT_FOUND;
        }
        final int sz = cs.length();
        for (int i = start < 0 ? 0 : start; i < sz; i++) {
            if (cs.charAt(i) == searchChar) {
                return i;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public int indexOf(char ch, int start) {
        if (ch > MAX_CHAR_VALUE) {
            return INDEX_NOT_FOUND;
        }

        if (start < 0) {
            start = 0;
        }

        final byte chAsByte = c2b0(ch);
        final int len = offset + length;
        for (int i = start + offset; i < len; ++i) {
            if (value[i] == chAsByte) {
                return i - offset;
            }
        }
        return INDEX_NOT_FOUND;
    }

    public static byte c2b(char c) {
        return (byte) ((c > MAX_CHAR_VALUE) ? '?' : c);
    }

    private static byte c2b0(char c) {
        return (byte) c;
    }

    public static char b2c(byte b) {
        return (char) (b & 0xFF);
    }
}
