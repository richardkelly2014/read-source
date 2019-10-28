package com.demo.util.internal;

import com.demo.util.concurrent.Promise;

/**
 * Created by jiangfei on 2019/10/28.
 */
public final class PromiseNotificationUtil {

    private PromiseNotificationUtil() {
    }

    public static void tryCancel(Promise<?> p) {
        if (!p.cancel(false)) {
            Throwable err = p.cause();
//            if (err == null) {
//                logger.warn("Failed to cancel promise because it has succeeded already: {}", p);
//            } else {
//                logger.warn(
//                        "Failed to cancel promise because it has failed already: {}, unnotified cause:",
//                        p, err);
//            }
        }
    }

    public static <V> void trySuccess(Promise<? super V> p, V result) {
        if (!p.trySuccess(result)) {
            Throwable err = p.cause();
//            if (err == null) {
//                logger.warn("Failed to mark a promise as success because it has succeeded already: {}", p);
//            } else {
//                logger.warn(
//                        "Failed to mark a promise as success because it has failed already: {}, unnotified cause:",
//                        p, err);
//            }
        }
    }

    public static void tryFailure(Promise<?> p, Throwable cause) {
        if (!p.tryFailure(cause)) {
            Throwable err = p.cause();
//            if (err == null) {
//                logger.warn("Failed to mark a promise as failure because it has succeeded already: {}", p, cause);
//            } else {
//                logger.warn(
//                        "Failed to mark a promise as failure because it has failed already: {}, unnotified cause: {}",
//                        p, ThrowableUtil.stackTraceToString(err), cause);
//            }
        }
    }

}
