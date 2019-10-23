package com.demo.buffer;


import java.nio.ByteOrder;

/**
 * 可交换 ByteBuf
 * Created by jiangfei on 2019/10/23.
 */
public class SwappedByteBuf extends ByteBuf {

    private final ByteBuf buf;
    private final ByteOrder order;

    public SwappedByteBuf(ByteBuf buf) {
        if (buf == null) {
            throw new NullPointerException("buf");
        }
        this.buf = buf;
        if (buf.order() == ByteOrder.BIG_ENDIAN) {
            order = ByteOrder.LITTLE_ENDIAN;
        } else {
            order = ByteOrder.BIG_ENDIAN;
        }
    }

    @Override
    public ByteOrder order() {
        return order;
    }

    @Override
    public ByteBuf order(ByteOrder endianness) {
        if (endianness == null) {
            throw new NullPointerException("endianness");
        }
        if (endianness == order) {
            return this;
        }
        return buf;
    }

}
