package com.demo;

import com.demo.util.concurrent.GlobalEventExecutor;
import org.junit.Test;

import java.io.IOException;

/**
 * Created by jiangfei on 2019/10/20.
 */
public class GlobalEventExectorTest {
    @Test
    public void test1() throws IOException, InterruptedException {

        GlobalEventExecutor eventExecutors = GlobalEventExecutor.INSTANCE;
        for (int i = 0; i < 10; i++) {
            final int v = i;
            eventExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("====>" + v);
                }
            });

        }
        //System.in.read();
        Thread.sleep(5000);

        for (int i = 0; i < 10; i++) {
            final int v = i;
            eventExecutors.execute(new Runnable() {
                @Override
                public void run() {
                    System.out.println("====>" + v);
                }
            });

        }
    }

}
