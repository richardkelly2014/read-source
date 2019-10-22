package com.demo;

import org.junit.Before;
import org.junit.Test;
import sun.misc.Unsafe;

import java.lang.reflect.Field;

public class UnsafeTest {


    private Unsafe unsafe;

    @Before
    public void before() throws IllegalAccessException, NoSuchFieldException {
        Field f = Unsafe.class.getDeclaredField("theUnsafe"); //获得名为 theUnsafe 的属性 即使为私有 同样获得
        f.setAccessible(true);

        this.unsafe = (Unsafe) f.get(null);

    }

    @Test
    public void test1() throws NoSuchFieldException {
        Field field = A.class.getDeclaredField("age");

        //long l1 = unsafe.staticFieldOffset(field);
        long l2 = unsafe.objectFieldOffset(field);

        A a = new A();
        unsafe.putInt(a, l2, 10);

        System.out.println(a.toString());
    }


    private static class A {
        private int age;

        @Override
        public String toString() {
            return "A{" +
                    "age=" + age +
                    '}';
        }
    }

}
