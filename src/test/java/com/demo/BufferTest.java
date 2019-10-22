package com.demo;

import com.demo.util.internal.PlatformDependent;
import org.junit.Test;

import java.nio.ByteBuffer;

public class BufferTest {

    @Test
    public void test1() {
        ByteBuffer EMPTY_BYTE_BUFFER = ByteBuffer.allocateDirect(0);

        System.out.println(PlatformDependent.directBufferAddress(EMPTY_BYTE_BUFFER));
    }
}
