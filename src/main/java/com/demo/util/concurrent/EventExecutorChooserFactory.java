package com.demo.util.concurrent;

/**
 * Created by jiangfei on 2019/10/20.
 */
public interface EventExecutorChooserFactory {

    EventExecutorChooser newChooser(EventExecutor[] executors);

    interface EventExecutorChooser {

        /**
         * Returns the new {@link EventExecutor} to use.
         */
        EventExecutor next();
    }


}
