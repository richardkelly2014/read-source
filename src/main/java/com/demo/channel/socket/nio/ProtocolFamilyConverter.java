package com.demo.channel.socket.nio;

import com.demo.channel.socket.InternetProtocolFamily;

import java.net.ProtocolFamily;
import java.net.StandardProtocolFamily;

/**
 * 协议转换
 * Created by jiangfei on 2019/11/3.
 */
final class ProtocolFamilyConverter {

    private ProtocolFamilyConverter() {
        // Utility class
    }

    public static ProtocolFamily convert(InternetProtocolFamily family) {
        switch (family) {
            case IPv4:
                return StandardProtocolFamily.INET;
            case IPv6:
                return StandardProtocolFamily.INET6;
            default:
                throw new IllegalArgumentException();
        }
    }
}
