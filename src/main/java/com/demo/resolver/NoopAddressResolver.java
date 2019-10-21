package com.demo.resolver;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.Promise;

import java.net.SocketAddress;
import java.util.Collections;
import java.util.List;

/**
 * Created by jiangfei on 2019/10/21.
 */
public class NoopAddressResolver extends AbstractAddressResolver<SocketAddress> {

    public NoopAddressResolver(EventExecutor executor) {
        super(executor);
    }

    @Override
    protected boolean doIsResolved(SocketAddress address) {
        return true;
    }

    @Override
    protected void doResolve(SocketAddress unresolvedAddress, Promise<SocketAddress> promise) throws Exception {
        promise.setSuccess(unresolvedAddress);
    }

    @Override
    protected void doResolveAll(
            SocketAddress unresolvedAddress, Promise<List<SocketAddress>> promise) throws Exception {
        promise.setSuccess(Collections.singletonList(unresolvedAddress));
    }
}
