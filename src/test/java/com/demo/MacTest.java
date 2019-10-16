package com.demo;

import com.demo.util.internal.MacAddressUtil;
import org.junit.Test;

/**
 * Created by jiangfei on 2019/10/16.
 */
public class MacTest {

    @Test
    public void test1() {

        byte[] buffer = MacAddressUtil.defaultMachineId();

        String macAddress = MacAddressUtil.formatAddress(buffer);

        System.out.println(macAddress);

    }

}
