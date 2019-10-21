package com.demo.resolver;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.Promise;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.List;

/**
 * Created by jiangfei on 2019/10/21.
 */
public class InetSocketAddressResolver extends AbstractAddressResolver<InetSocketAddress> {

    final NameResolver<InetAddress> nameResolver;

    public InetSocketAddressResolver(EventExecutor executor, NameResolver<InetAddress> nameResolver) {
        super(executor, InetSocketAddress.class);
        this.nameResolver = nameResolver;
    }

    @Override
    protected boolean doIsResolved(InetSocketAddress address) {

        return !address.isUnresolved();
    }

    @Override
    protected void doResolve(InetSocketAddress unresolvedAddress, Promise<InetSocketAddress> promise) throws Exception {

    }

    @Override
    protected void doResolveAll(InetSocketAddress unresolvedAddress, Promise<List<InetSocketAddress>> promise) throws Exception {

    }
}
