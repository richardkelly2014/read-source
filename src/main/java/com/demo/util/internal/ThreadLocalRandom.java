package com.demo.util.internal;

import java.security.SecureRandom;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by jiangfei on 2019/10/17.
 */
public final class ThreadLocalRandom extends Random {

    private static final AtomicLong seedUniquifier = new AtomicLong();

    private static volatile long initialSeedUniquifier;

    private static final Thread seedGeneratorThread;
    private static final BlockingQueue<Long> seedQueue;
    private static final long seedGeneratorStartTime;
    private static volatile long seedGeneratorEndTime;

    static {
        initialSeedUniquifier = SystemPropertyUtil.getLong("io.netty.initialSeedUniquifier", 0);

        if (initialSeedUniquifier == 0) {

            boolean secureRandom = SystemPropertyUtil.getBoolean("java.util.secureRandomSeed", false);

            if (secureRandom) {

                seedQueue = new LinkedBlockingQueue<Long>();
                seedGeneratorStartTime = System.nanoTime();

                // Try to generate a real random number from /dev/random.
                // Get from a different thread to avoid blocking indefinitely on a machine without much entropy.
                seedGeneratorThread = new Thread("initialSeedUniquifierGenerator") {
                    @Override
                    public void run() {
                        final SecureRandom random = new SecureRandom(); // Get the real random seed from /dev/random
                        final byte[] seed = random.generateSeed(8);
                        seedGeneratorEndTime = System.nanoTime();
                        long s = ((long) seed[0] & 0xff) << 56 |
                                ((long) seed[1] & 0xff) << 48 |
                                ((long) seed[2] & 0xff) << 40 |
                                ((long) seed[3] & 0xff) << 32 |
                                ((long) seed[4] & 0xff) << 24 |
                                ((long) seed[5] & 0xff) << 16 |
                                ((long) seed[6] & 0xff) << 8 |
                                (long) seed[7] & 0xff;
                        seedQueue.add(s);
                    }
                };
                seedGeneratorThread.setDaemon(true);
                seedGeneratorThread.setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
                    @Override
                    public void uncaughtException(Thread t, Throwable e) {
                        //logger.debug("An exception has been raised by {}", t.getName(), e);
                    }
                });
                seedGeneratorThread.start();

            } else {
                initialSeedUniquifier = mix64(System.currentTimeMillis()) ^ mix64(System.nanoTime());
                seedGeneratorThread = null;
                seedQueue = null;
                seedGeneratorStartTime = 0L;
            }

        } else {
            seedGeneratorThread = null;
            seedQueue = null;
            seedGeneratorStartTime = 0L;
        }

    }

    public static long getInitialSeedUniquifier() {
        long initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
        if (initialSeedUniquifier != 0) {
            return initialSeedUniquifier;
        }

        synchronized (ThreadLocalRandom.class) {
            initialSeedUniquifier = ThreadLocalRandom.initialSeedUniquifier;
            if (initialSeedUniquifier != 0) {
                return initialSeedUniquifier;
            }

            // Get the random seed from the generator thread with timeout.
            final long timeoutSeconds = 3;
            final long deadLine = seedGeneratorStartTime + TimeUnit.SECONDS.toNanos(timeoutSeconds);
            boolean interrupted = false;
            for (; ; ) {
                final long waitTime = deadLine - System.nanoTime();
                try {
                    final Long seed;
                    if (waitTime <= 0) {
                        seed = seedQueue.poll();
                    } else {
                        seed = seedQueue.poll(waitTime, TimeUnit.NANOSECONDS);
                    }

                    if (seed != null) {
                        initialSeedUniquifier = seed;
                        break;
                    }
                } catch (InterruptedException e) {
                    interrupted = true;
                    break;
                }

                if (waitTime <= 0) {
                    seedGeneratorThread.interrupt();
                    break;
                }
            }

            initialSeedUniquifier ^= 0x3255ecdc33bae119L; // just a meaningless random number
            initialSeedUniquifier ^= Long.reverse(System.nanoTime());

            ThreadLocalRandom.initialSeedUniquifier = initialSeedUniquifier;

            if (interrupted) {
                // Restore the interrupt status because we don't know how to/don't need to handle it here.
                Thread.currentThread().interrupt();

                // Interrupt the generator thread if it's still running,
                // in the hope that the SecureRandom provider raises an exception on interruption.
                seedGeneratorThread.interrupt();
            }

            if (seedGeneratorEndTime == 0) {
                seedGeneratorEndTime = System.nanoTime();
            }

            return initialSeedUniquifier;
        }
    }

    private static long newSeed() {
        for (; ; ) {
            final long current = seedUniquifier.get();
            final long actualCurrent = current != 0 ? current : getInitialSeedUniquifier();

            // L'Ecuyer, "Tables of Linear Congruential Generators of Different Sizes and Good Lattice Structure", 1999
            final long next = actualCurrent * 181783497276652981L;

            if (seedUniquifier.compareAndSet(current, next)) {
                return next ^ System.nanoTime();
            }
        }
    }

    private static long mix64(long z) {
        z = (z ^ (z >>> 33)) * 0xff51afd7ed558ccdL;
        z = (z ^ (z >>> 33)) * 0xc4ceb9fe1a85ec53L;
        return z ^ (z >>> 33);
    }

    // same constants as Random, but must be redeclared because private
    private static final long multiplier = 0x5DEECE66DL;
    private static final long addend = 0xBL;
    private static final long mask = (1L << 48) - 1;

    /**
     * The random seed. We can't use super.seed.
     */
    private long rnd;

    /**
     * Initialization flag to permit calls to setSeed to succeed only
     * while executing the Random constructor.  We can't allow others
     * since it would cause setting seed in one part of a program to
     * unintentionally impact other usages by the thread.
     */
    boolean initialized;

    // Padding to help avoid memory contention among seed updates in
    // different TLRs in the common case that they are located near
    // each other.
    private long pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7;

    public ThreadLocalRandom() {
        super(newSeed());
        initialized = true;
    }

    @Override
    protected int next(int bits) {
        rnd = (rnd * multiplier + addend) & mask;
        return (int) (rnd >>> (48 - bits));
    }

    public int nextInt(int least, int bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextInt(bound - least) + least;
    }

    public long nextLong(long n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }

        long offset = 0;
        while (n >= Integer.MAX_VALUE) {
            int bits = next(2);
            long half = n >>> 1;
            long nextn = ((bits & 2) == 0) ? half : n - half;
            if ((bits & 1) == 0) {
                offset += n - nextn;
            }
            n = nextn;
        }
        return offset + nextInt((int) n);
    }

    public long nextLong(long least, long bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextLong(bound - least) + least;
    }

    public double nextDouble(double n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        }
        return nextDouble() * n;
    }

    public double nextDouble(double least, double bound) {
        if (least >= bound) {
            throw new IllegalArgumentException();
        }
        return nextDouble() * (bound - least) + least;
    }
}
