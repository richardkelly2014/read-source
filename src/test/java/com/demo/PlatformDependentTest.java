package com.demo;


import com.demo.util.internal.SystemPropertyUtil;
import org.junit.Test;

public class PlatformDependentTest {

    @Test
    public void test1() {

        boolean secureRandom = SystemPropertyUtil.getBoolean("java.util.secureRandomSeed", false);

        System.out.println(secureRandom);

    }


}
