package com.demo.util.concurrent;

import com.demo.util.internal.ObjectUtil;

/**
 * Created by jiangfei on 2019/10/17.
 */
class FastThreadLocalRunnable implements Runnable {

    private final Runnable runnable;

    private FastThreadLocalRunnable(Runnable runnable) {
        this.runnable = ObjectUtil.checkNotNull(runnable, "runnable");
    }

    @Override
    public void run() {
        try {
            runnable.run();
        } finally {
            FastThreadLocal.removeAll();
        }
    }

    static Runnable wrap(Runnable runnable) {
        return runnable instanceof FastThreadLocalRunnable ? runnable : new FastThreadLocalRunnable(runnable);
    }

}
