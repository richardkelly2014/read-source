package com.demo.gc;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by jiangfei on 2019/10/23.
 */
public class GCTest {

    private Thread temporaryThread;
    private A temporaryObject;

    @Test
    public void test1() throws IOException, InterruptedException {
        final AtomicBoolean freeCalled = new AtomicBoolean();
        final CountDownLatch latch = new CountDownLatch(1);
        temporaryThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    latch.await();
                } catch (InterruptedException ignore) {
                    // just ignore
                }
            }
        });
        temporaryThread.start();
        GCHandler.register(temporaryThread, new Runnable() {
            @Override
            public void run() {
                System.out.println("gc");
                freeCalled.set(true);
            }
        });

        latch.countDown();
        temporaryThread.join();
        Assert.assertFalse(freeCalled.get());

        // Null out the temporary object to ensure it is enqueued for GC.
        temporaryThread = null;

        while (!freeCalled.get()) {
            System.gc();
            System.runFinalization();
            Thread.sleep(100);
        }
    }

    @Test
    public void test2() throws InterruptedException {
        temporaryObject = new A(100);
        GCHandler.register(temporaryObject, null);
        temporaryObject=null;

        while (true) {
            System.gc();
            System.runFinalization();
            Thread.sleep(100);
        }
    }


    private static class A {
        private int age;

        A(int age) {
            this.age = age;
        }

        @Override
        public String toString() {
            return "A{" +
                    "age=" + age +
                    '}';
        }
    }

}
