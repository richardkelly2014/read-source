package com.demo.resolver;

import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.Promise;

import java.io.Closeable;
import java.net.SocketAddress;
import java.util.List;

/**
 * Created by jiangfei on 2019/10/21.
 */
public interface AddressResolver<T extends SocketAddress> extends Closeable {

    boolean isSupported(SocketAddress address);

    boolean isResolved(SocketAddress address);

    Future<T> resolve(SocketAddress address);

    Future<T> resolve(SocketAddress address, Promise<T> promise);

    Future<List<T>> resolveAll(SocketAddress address);

    Future<List<T>> resolveAll(SocketAddress address, Promise<List<T>> promise);

    @Override
    void close();
}
