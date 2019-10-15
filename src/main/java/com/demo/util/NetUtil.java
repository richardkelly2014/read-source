package com.demo.util;

import com.demo.util.internal.SystemPropertyUtil;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.NetworkInterface;

/**
 * 网络 util
 */
public final class NetUtil {
    private NetUtil() {
    }

//    public static final Inet4Address LOCALHOST4;
//
//    public static final Inet6Address LOCALHOST6;
//
//    public static final InetAddress LOCALHOST;
//
//    public static final NetworkInterface LOOPBACK_IF;
//
//    public static final int SOMAXCONN;

    private static final int IPV6_WORD_COUNT = 8;

    private static final int IPV6_MAX_CHAR_COUNT = 39;

    private static final int IPV6_BYTE_COUNT = 16;

    private static final int IPV6_MAX_CHAR_BETWEEN_SEPARATOR = 4;

    private static final int IPV6_MIN_SEPARATORS = 2;

    private static final int IPV6_MAX_SEPARATORS = 8;

    private static final int IPV4_MAX_CHAR_BETWEEN_SEPARATOR = 3;
    private static final int IPV4_SEPARATORS = 3;

    private static final boolean IPV4_PREFERRED = SystemPropertyUtil.getBoolean("java.net.preferIPv4Stack", false);


    private static final boolean IPV6_ADDRESSES_PREFERRED =
            SystemPropertyUtil.getBoolean("java.net.preferIPv6Addresses", false);

    static {
        byte[] LOCALHOST4_BYTES = {127, 0, 0, 1};
        byte[] LOCALHOST6_BYTES = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1};


    }

}
