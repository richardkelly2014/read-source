package com.demo;

import com.demo.util.NetUtil;
import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

public class NetTest {

    private static final class TestMap extends HashMap<String, String> {
        private static final long serialVersionUID = -298642816998608473L;

        TestMap(String... values) {
            for (int i = 0; i < values.length; i += 2) {
                String key = values[i];
                String value = values[i + 1];
                put(key, value);
            }
        }
    }

    @Test
    public void test1() throws UnknownHostException {
        byte[] LOCALHOST4_BYTES = {127, 0, 0, 1};
        Inet4Address localhost4 = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);

        System.out.println(localhost4);
    }

    @Test
    public void test2() {
        int CPUs = Runtime.getRuntime().availableProcessors();
    }

    private static final Map<String, String> validIpV4Hosts = new TestMap(
            "192.168.1.0", "c0a80100",
            "10.255.255.254", "0afffffe",
            "172.18.5.4", "ac120504",
            "0.0.0.0", "00000000",
            "127.0.0.1", "7f000001",
            "255.255.255.255", "ffffffff",
            "1.2.3.4", "01020304");

    @Test
    public void test3() {
        byte[] buffer = NetUtil.createByteArrayFromIpAddressString("10.254.254.254");

        String ip = NetUtil.bytesToIpAddress(buffer);

        String address = NetUtil.toSocketAddressString(ip, 8080);

        System.out.println(ip);

        System.out.println(address);
    }

}
