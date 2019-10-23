package com.demo.gc;

import com.demo.util.concurrent.FastThreadLocalThread;
import com.demo.util.internal.ObjectUtil;
import com.demo.util.internal.SystemPropertyUtil;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.Math.max;

/**
 * Created by jiangfei on 2019/10/23.
 */
public class GCHandler {

    private static final int REFERENCE_QUEUE_POLL_TIMEOUT_MS =
            max(500, SystemPropertyUtil.getInt("io.netty.util.internal.ObjectCleaner.refQueuePollTimeout", 10000));

    // Package-private for testing
    static final String CLEANER_THREAD_NAME = GCHandler.class.getSimpleName() + "Thread";
    // This will hold a reference to the AutomaticCleanerReference which will be removed once we called cleanup()
    private static final Set<AutomaticCleanerReference> LIVE_SET =
            Collections.newSetFromMap(new ConcurrentHashMap<AutomaticCleanerReference, Boolean>());

    private static final ReferenceQueue<Object> REFERENCE_QUEUE = new ReferenceQueue<Object>();
    private static final AtomicBoolean CLEANER_RUNNING = new AtomicBoolean(false);
    private static final Runnable CLEANER_TASK = new Runnable() {
        @Override
        public void run() {
            boolean interrupted = false;
            for (; ; ) {
                // Keep on processing as long as the LIVE_SET is not empty and once it becomes empty
                // See if we can let this thread complete.
                while (!LIVE_SET.isEmpty()) {
                    final AutomaticCleanerReference reference;
                    try {
                        reference = (AutomaticCleanerReference) REFERENCE_QUEUE.remove(REFERENCE_QUEUE_POLL_TIMEOUT_MS);
                    } catch (InterruptedException ex) {
                        // Just consume and move on
                        interrupted = true;
                        continue;
                    }
                    if (reference != null) {
                        try {
                            reference.cleanup();
                        } catch (Throwable ignored) {
                            // ignore exceptions, and don't log in case the logger throws an exception, blocks, or has
                            // other unexpected side effects.
                        }
                        LIVE_SET.remove(reference);
                    }
                }
                CLEANER_RUNNING.set(false);

                // Its important to first access the LIVE_SET and then CLEANER_RUNNING to ensure correct
                // behavior in multi-threaded environments.
                if (LIVE_SET.isEmpty() || !CLEANER_RUNNING.compareAndSet(false, true)) {
                    // There was nothing added after we set STARTED to false or some other cleanup Thread
                    // was started already so its safe to let this Thread complete now.
                    break;
                }
            }
            if (interrupted) {
                // As we caught the InterruptedException above we should mark the Thread as interrupted.
                Thread.currentThread().interrupt();
            }
        }
    };

    /**
     * Register the given {@link Object} for which the {@link Runnable} will be executed once there are no references
     * to the object anymore.
     * <p>
     * This should only be used if there are no other ways to execute some cleanup once the Object is not reachable
     * anymore because it is not a cheap way to handle the cleanup.
     */
    public static void register(Object object, Runnable cleanupTask) {
        AutomaticCleanerReference reference = new AutomaticCleanerReference(object);
        // Its important to add the reference to the LIVE_SET before we access CLEANER_RUNNING to ensure correct
        // behavior in multi-threaded environments.
        LIVE_SET.add(reference);

        // Check if there is already a cleaner running.
        if (CLEANER_RUNNING.compareAndSet(false, true)) {
            final Thread cleanupThread = new FastThreadLocalThread(CLEANER_TASK);
            cleanupThread.setPriority(Thread.MIN_PRIORITY);
            // Set to null to ensure we not create classloader leaks by holding a strong reference to the inherited
            // classloader.
            // See:
            // - https://github.com/netty/netty/issues/7290
            // - https://bugs.openjdk.java.net/browse/JDK-7008595
            AccessController.doPrivileged(new PrivilegedAction<Void>() {
                @Override
                public Void run() {
                    cleanupThread.setContextClassLoader(null);
                    return null;
                }
            });
            cleanupThread.setName(CLEANER_THREAD_NAME);

            // Mark this as a daemon thread to ensure that we the JVM can exit if this is the only thread that is
            // running.
            cleanupThread.setDaemon(true);
            cleanupThread.start();
        }
    }

    public static int getLiveSetCount() {
        return LIVE_SET.size();
    }


    private static final class AutomaticCleanerReference extends WeakReference<Object> {
        //private final Runnable cleanupTask;
        private String value;

        AutomaticCleanerReference(Object referent) {
            super(referent, REFERENCE_QUEUE);
            //this.cleanupTask = cleanupTask;
            this.value = referent.toString();
        }

        void cleanup() {
            System.out.println("gc===" + this.value);
        }

        @Override
        public Thread get() {
            return null;
        }

        @Override
        public void clear() {
            LIVE_SET.remove(this);
            System.out.println("clear:" + this.value);
            super.clear();
        }
    }

}
