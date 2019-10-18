package com.demo;

import com.demo.hashwheel.simple.ExpirationListener;
import com.demo.hashwheel.simple.WheelTimer;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by jiangfei on 2019/10/18.
 */
public class SimpleWheelTest {

    @Test
    public void test1() throws InterruptedException, IOException {
        WheelTimer<String> wheelTimer = new WheelTimer<String>(1, TimeUnit.SECONDS, 60);
        wheelTimer.start();
        wheelTimer.addExpirationListener(new ExpirationListener<String>() {
            @Override
            public void expired(String expireObject) {
                System.out.println(expireObject);
            }
        });

        Thread.sleep(2000);
        wheelTimer.add("1111");

        System.in.read();
    }

}
