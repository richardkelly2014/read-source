package com.demo.resolver;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.FutureListener;
import com.demo.util.concurrent.Promise;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
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
        nameResolver.resolve(unresolvedAddress.getHostName())
                .addListener(new FutureListener<InetAddress>() {
                    @Override
                    public void operationComplete(Future<InetAddress> future) throws Exception {
                        if (future.isSuccess()) {
                            promise.setSuccess(new InetSocketAddress(future.getNow(), unresolvedAddress.getPort()));
                        } else {
                            promise.setFailure(future.cause());
                        }
                    }
                });
    }

    @Override
    protected void doResolveAll(InetSocketAddress unresolvedAddress, Promise<List<InetSocketAddress>> promise) throws Exception {
        nameResolver.resolveAll(unresolvedAddress.getHostName())
                .addListener(new FutureListener<List<InetAddress>>() {
                    @Override
                    public void operationComplete(Future<List<InetAddress>> future) throws Exception {
                        if (future.isSuccess()) {
                            List<InetAddress> inetAddresses = future.getNow();
                            List<InetSocketAddress> socketAddresses =
                                    new ArrayList<InetSocketAddress>(inetAddresses.size());
                            for (InetAddress inetAddress : inetAddresses) {
                                socketAddresses.add(new InetSocketAddress(inetAddress, unresolvedAddress.getPort()));
                            }
                            promise.setSuccess(socketAddresses);
                        } else {
                            promise.setFailure(future.cause());
                        }
                    }
                });
    }
}
