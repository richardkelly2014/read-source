package com.demo.hashwheel.simple;

import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by jiangfei on 2019/10/18.
 */
public class WheelTimer<E> {
    /**
     * 每次走的精度 tickDuration（一个tick的持续时间）
     */
    private long tickDuration;

    /**
     * 每次需要转多少圈 l（一轮的tick数)
     */
    private int ticksPerWheel;
    private volatile int currentTickIndex = 0;
    private CopyOnWriteArrayList<ExpirationListener<E>> expirationListeners = new CopyOnWriteArrayList<ExpirationListener<E>>();
    private ArrayList<TimeSlot<E>> wheel;

    private AtomicBoolean shutdown = new AtomicBoolean(false);
    private ReadWriteLock lock = new ReentrantReadWriteLock();
    private Thread workThread;

    public WheelTimer(int tickDuration, TimeUnit timeUnit, int ticksPerWheel) {
        if (timeUnit == null) {
            throw new NullPointerException("time unit");
        }

        if (tickDuration <= 0) {
            throw new IllegalArgumentException("tickDuration must be greater than 0" + tickDuration);
        }

        this.wheel = new ArrayList<TimeSlot<E>>();
        this.tickDuration = TimeUnit.MILLISECONDS.convert(tickDuration, timeUnit);
        this.ticksPerWheel = ticksPerWheel;
        for (int i = 0; i < this.ticksPerWheel; i++) {
            wheel.add(new TimeSlot<E>(i));
        }

        wheel.trimToSize();

        workThread = new Thread(new TickWorker(), "wheel-timer");
    }

    public void start() {
        if (shutdown.get()) {
            throw new IllegalStateException("the thread is stoped");
        }

        if (!workThread.isAlive()) {
            workThread.start();
        }
    }

    public boolean stop() {
        if (!shutdown.compareAndSet(false, true)) {
            return false;
        }
        boolean interrupted = false;
        while (workThread.isAlive()) {
            workThread.interrupt();
            try {
                workThread.join(1000);
            } catch (Exception e) {
                interrupted = true;
            }
        }

        if (interrupted) {
            Thread.currentThread().interrupt();
        }

        return true;
    }

    public void addExpirationListener(ExpirationListener<E> listener) {

        expirationListeners.add(listener);
    }

    public void removeExpirationListner(ExpirationListener<E> listener) {

        expirationListeners.remove(listener);
    }

    public long add(E e) {
        synchronized (e) {
            int previousTickIndex = getPreviousTickIndex();
            TimeSlot<E> timeSlotSet = wheel.get(previousTickIndex);
            timeSlotSet.add(e);
            return (ticksPerWheel - 1) * tickDuration;
        }
    }

    public void notifyExpired(int idx) {
        TimeSlot<E> timeSlot = wheel.get(idx);
        Set<E> elements = timeSlot.getElements();
        // todo tow for loop ??
        for (E e : elements) {
            timeSlot.remove(e);
            for (ExpirationListener<E> listener : expirationListeners) {
                listener.expired(e);
            }
        }
    }

    private int getPreviousTickIndex() {
        lock.readLock().lock();
        try {
            int cti = currentTickIndex;
            if (cti == 0) {
                return ticksPerWheel - 1;
            }
            return cti - 1;
        } catch (Exception e) {

        } finally {
            lock.readLock().unlock();
        }

        return currentTickIndex - 1;
    }


    private class TickWorker implements Runnable {

        /**
         * 启动时间
         */
        private long startTime;

        /**
         * 运行次数
         */
        private long tick = 1L;

        @Override
        public void run() {
            startTime = System.currentTimeMillis();
            tick = 1; //1?
            for (int i = 0; !shutdown.get(); i++) {
                if (i == wheel.size()) {
                    i = 0;
                }

                lock.writeLock().lock();
                try {
                    currentTickIndex = i;
                } catch (Exception e) {

                } finally {
                    lock.writeLock().unlock();
                }

                notifyExpired(currentTickIndex);
                waitForNexTick();
                tick++;
            }
        }

        private void waitForNexTick() {
            for (; ; ) {
                long currentTime = System.currentTimeMillis();
                long sleepTime = tickDuration * tick - (currentTime - startTime);
                System.out.println(sleepTime);
                if (sleepTime <= 0) {
                    break;
                }

                try {
                    Thread.sleep(sleepTime);
                } catch (Exception e) {

                } finally {
                    break;
                }
            }
        }
    }
}
