package com.demo;

import com.demo.util.concurrent.FastThreadLocal;
import org.junit.Test;

/**
 * Created by jiangfei on 2019/10/17.
 */
public class FastThreadLocalTest {

    @Test
    public void test1() {
        FastThreadLocal<String> fastThreadLocal = new FastThreadLocal<String>() {
            @Override
            protected String initialValue() throws Exception {
                return "hha";
            }

            @Override
            protected void onRemoval(String value) throws Exception {
                System.out.println("delete : " + value);
            }
        };
        System.out.println(fastThreadLocal.get());
        fastThreadLocal.set("111");
        String value = fastThreadLocal.get();
        System.out.println(value);
    }

}
