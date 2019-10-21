package com.demo.resolver;

import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.Promise;

import java.io.Closeable;
import java.util.List;

/**
 * Created by jiangfei on 2019/10/21.
 */
public interface NameResolver<T> extends Closeable {

    Future<T> resolve(String inetHost);

    Future<T> resolve(String inetHost, Promise<T> promise);

    Future<List<T>> resolveAll(String inetHost);


    Future<List<T>> resolveAll(String inetHost, Promise<List<T>> promise);

    @Override
    void close();

}
