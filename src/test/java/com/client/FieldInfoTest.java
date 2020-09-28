package com.client;

import org.junit.Test;


public class FieldInfoTest {

    public enum A {
        INFO,
        DEBUG;
    }

    @Test
    public void test1() throws NoSuchFieldException {

        A i = A.INFO;

        System.out.println(i.getClass().getField(i.name()));
    }

}
