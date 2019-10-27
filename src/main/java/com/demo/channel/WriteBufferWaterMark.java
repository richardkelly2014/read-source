package com.demo.channel;

import static com.demo.util.internal.ObjectUtil.checkPositiveOrZero;

/**
 * 写入 buffer 标记
 * Created by jiangfei on 2019/10/27.
 */
public final class WriteBufferWaterMark {

    private static final int DEFAULT_LOW_WATER_MARK = 32 * 1024;
    private static final int DEFAULT_HIGH_WATER_MARK = 64 * 1024;

    public static final WriteBufferWaterMark DEFAULT =
            new WriteBufferWaterMark(DEFAULT_LOW_WATER_MARK, DEFAULT_HIGH_WATER_MARK, false);

    private final int low;
    private final int high;

    /**
     * Create a new instance.
     *
     * @param low  low water mark for write buffer.
     * @param high high water mark for write buffer
     */
    public WriteBufferWaterMark(int low, int high) {
        this(low, high, true);
    }

    /**
     * This constructor is needed to keep backward-compatibility.
     */
    WriteBufferWaterMark(int low, int high, boolean validate) {
        if (validate) {
            checkPositiveOrZero(low, "low");
            if (high < low) {
                throw new IllegalArgumentException(
                        "write buffer's high water mark cannot be less than " +
                                " low water mark (" + low + "): " +
                                high);
            }
        }
        this.low = low;
        this.high = high;
    }

    /**
     * Returns the low water mark for the write buffer.
     */
    public int low() {
        return low;
    }

    /**
     * Returns the high water mark for the write buffer.
     */
    public int high() {
        return high;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(55)
                .append("WriteBufferWaterMark(low: ")
                .append(low)
                .append(", high: ")
                .append(high)
                .append(")");
        return builder.toString();
    }

}
