package com.client;

import com.google.client.util.ClassInfo;
import com.google.client.util.NullValue;
import com.google.client.util.Value;
import org.junit.Test;

public class ClassInfoTest {

    public enum E {

        @Value
        VALUE,
        @Value("other")
        OTHER_VALUE,
        @NullValue
        NULL, IGNORED_VALUE
    }

    @Test
    public void test1() {

        ClassInfo classInfo = ClassInfo.of(E.class);

        System.out.println(classInfo.getNames());

    }

}
