package com.demo;

import org.junit.Test;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetTest {

    @Test
    public void test1() throws UnknownHostException {
        byte[] LOCALHOST4_BYTES = {127, 0, 0, 1};
        Inet4Address localhost4 = (Inet4Address) InetAddress.getByAddress("localhost", LOCALHOST4_BYTES);

        System.out.println(localhost4);
    }

    @Test
    public void test2(){
        int CPUs = Runtime.getRuntime().availableProcessors();


    }

}
