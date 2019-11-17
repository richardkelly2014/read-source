package com.demo.rasp;

import com.pi4jl.util.Console;
import com.pi4jl.util.ConsoleColor;
import org.junit.Test;

/**
 * Created by jiangfei on 2019/11/16.
 */
public class ConsoleTest {

    @Test
    public void test1() {
        Console console = new Console();
        console.println(ConsoleColor.GREEN.build("hello word"));

        System.out.println("\033[36m" + "你好");
        System.out.println("\033[36m" + "你好");
    }

}
