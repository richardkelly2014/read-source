package com.demo.channel;

/**
 * 消息大小 估算器
 * Created by jiangfei on 2019/10/27.
 */
public interface MessageSizeEstimator {

    /**
     * Creates a new handle. The handle provides the actual operations.
     */
    Handle newHandle();

    interface Handle {

        /**
         * Calculate the size of the given message.
         *
         * @param msg The message for which the size should be calculated
         * @return size     The size in bytes. The returned size must be >= 0
         */
        int size(Object msg);
    }

}
