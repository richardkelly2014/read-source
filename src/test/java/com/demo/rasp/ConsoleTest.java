package com.demo.rasp;


import org.junit.Test;

import java.io.Console;

/**
 * Created by jiangfei on 2019/11/16.
 */
public class ConsoleTest {

    @Test
    public void test1() {


        System.out.println("\033[36m" + "你好");
        System.out.println("\033[36m" + "你好");
    }

}
