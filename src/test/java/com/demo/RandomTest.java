package com.demo;

import com.demo.util.internal.ThreadLocalRandom;
import org.junit.Test;

/**
 * Created by jiangfei on 2019/10/17.
 */
public class RandomTest {

    private ThreadLocalRandom random = new ThreadLocalRandom();

    @Test
    public void test() {
        System.out.println(random.nextInt(1, 10));
        System.out.println(random.nextInt(1, 10));
        System.out.println(random.nextInt(1, 10));
        System.out.println(random.nextInt(1, 10));
    }

}
