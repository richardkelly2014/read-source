package com.demo.resolver;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.Promise;

import java.util.List;

import static com.demo.util.internal.ObjectUtil.checkNotNull;

public abstract class SimpleNameResolver<T> implements NameResolver<T>  {

    private final EventExecutor executor;

    protected SimpleNameResolver(EventExecutor executor) {

        this.executor = checkNotNull(executor, "executor");
    }

    protected EventExecutor executor() {

        return executor;
    }

    @Override
    public final Future<T> resolve(String inetHost) {
        final Promise<T> promise = executor().newPromise();
        return resolve(inetHost, promise);
    }

    @Override
    public Future<T> resolve(String inetHost, Promise<T> promise) {
        checkNotNull(promise, "promise");

        try {
            doResolve(inetHost, promise);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    @Override
    public final Future<List<T>> resolveAll(String inetHost) {
        final Promise<List<T>> promise = executor().newPromise();
        return resolveAll(inetHost, promise);
    }

    @Override
    public Future<List<T>> resolveAll(String inetHost, Promise<List<T>> promise) {
        checkNotNull(promise, "promise");

        try {
            doResolveAll(inetHost, promise);
            return promise;
        } catch (Exception e) {
            return promise.setFailure(e);
        }
    }

    /**
     * Invoked by {@link #resolve(String)} to perform the actual name resolution.
     */
    protected abstract void doResolve(String inetHost, Promise<T> promise) throws Exception;

    /**
     * Invoked by {@link #resolveAll(String)} to perform the actual name resolution.
     */
    protected abstract void doResolveAll(String inetHost, Promise<List<T>> promise) throws Exception;

    @Override
    public void close() { }

}
