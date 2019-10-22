package com.demo.resolver;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.FutureListener;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.IdentityHashMap;
import java.util.Map;

public abstract class AddressResolverGroup<T extends SocketAddress> implements Closeable {

    private final Map<EventExecutor, AddressResolver<T>> resolvers = new IdentityHashMap<EventExecutor, AddressResolver<T>>();

    protected AddressResolverGroup() {
    }

    public AddressResolver<T> getResolver(final EventExecutor executor) {
        if (executor == null) {
            throw new NullPointerException("executor");
        }

        if (executor.isShuttingDown()) {
            throw new IllegalStateException("executor not accepting a task");
        }

        AddressResolver<T> r;
        synchronized (resolvers) {
            r = resolvers.get(executor);
            if (r == null) {
                final AddressResolver<T> newResolver;
                try {
                    newResolver = newResolver(executor);
                } catch (Exception e) {
                    throw new IllegalStateException("failed to create a new resolver", e);
                }

                resolvers.put(executor, newResolver);
                executor.terminationFuture().addListener(new FutureListener<Object>() {
                    @Override
                    public void operationComplete(Future<Object> future) throws Exception {
                        synchronized (resolvers) {
                            resolvers.remove(executor);
                        }
                        newResolver.close();
                    }
                });

                r = newResolver;
            }
        }

        return r;
    }

    protected abstract AddressResolver<T> newResolver(EventExecutor executor) throws Exception;


    @Override
    @SuppressWarnings({"unchecked", "SuspiciousToArrayCall"})
    public void close() {
        final AddressResolver<T>[] rArray;
        synchronized (resolvers) {
            rArray = (AddressResolver<T>[]) resolvers.values().toArray(new AddressResolver[0]);
            resolvers.clear();
        }

        for (AddressResolver<T> r : rArray) {
            try {
                r.close();
            } catch (Throwable t) {
                //logger.warn("Failed to close a resolver:", t);
            }
        }
    }

}
