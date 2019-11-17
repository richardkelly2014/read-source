package com.demo.rasp;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class Test {

    public static void main(String[] args) {
        System.out.println(System.getProperty("java.library.path"));
        System.load("/usr/lib/libpi4j.so");
    }

}
