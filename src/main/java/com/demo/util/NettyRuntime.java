package com.demo.util;

import com.demo.util.internal.ObjectUtil;
import com.demo.util.internal.SystemPropertyUtil;

import java.util.Locale;

/**
 * Created by jiangfei on 2019/10/25.
 */
public final class NettyRuntime {

    static class AvailableProcessorsHolder {

        private int availableProcessors;

        /**
         * Set the number of available processors.
         *
         * @param availableProcessors the number of available processors
         * @throws IllegalArgumentException if the specified number of available processors is non-positive
         * @throws IllegalStateException    if the number of available processors is already configured
         */
        synchronized void setAvailableProcessors(final int availableProcessors) {
            ObjectUtil.checkPositive(availableProcessors, "availableProcessors");
            if (this.availableProcessors != 0) {
                final String message = String.format(
                        Locale.ROOT,
                        "availableProcessors is already set to [%d], rejecting [%d]",
                        this.availableProcessors,
                        availableProcessors);
                throw new IllegalStateException(message);
            }
            this.availableProcessors = availableProcessors;
        }

        /**
         * Get the configured number of available processors. The default is {@link Runtime#availableProcessors()}.
         * This can be overridden by setting the system property "io.netty.availableProcessors" or by invoking
         * {@link #setAvailableProcessors(int)} before any calls to this method.
         *
         * @return the configured number of available processors
         */
        //@SuppressForbidden(reason = "to obtain default number of available processors")
        synchronized int availableProcessors() {
            if (this.availableProcessors == 0) {
                //可用处理器个数
                final int availableProcessors =
                        SystemPropertyUtil.getInt(
                                "io.netty.availableProcessors",
                                Runtime.getRuntime().availableProcessors());
                setAvailableProcessors(availableProcessors);
            }
            return this.availableProcessors;
        }
    }

    private static final AvailableProcessorsHolder holder = new AvailableProcessorsHolder();

    public static void setAvailableProcessors(final int availableProcessors) {
        holder.setAvailableProcessors(availableProcessors);
    }

    public static int availableProcessors() {
        return holder.availableProcessors();
    }

    private NettyRuntime() {
    }

}
