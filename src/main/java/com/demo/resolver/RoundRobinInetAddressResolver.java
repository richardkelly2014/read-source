package com.demo.resolver;

import com.demo.util.concurrent.EventExecutor;
import com.demo.util.concurrent.Future;
import com.demo.util.concurrent.FutureListener;
import com.demo.util.concurrent.Promise;
import com.demo.util.internal.PlatformDependent;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RoundRobinInetAddressResolver extends InetNameResolver {

    private final NameResolver<InetAddress> nameResolver;

    public RoundRobinInetAddressResolver(EventExecutor executor, NameResolver<InetAddress> nameResolver) {
        super(executor);
        this.nameResolver = nameResolver;
    }


    @Override
    protected void doResolve(final String inetHost, final Promise<InetAddress> promise) throws Exception {
        
        nameResolver.resolveAll(inetHost).addListener(new FutureListener<List<InetAddress>>() {
            @Override
            public void operationComplete(Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    List<InetAddress> inetAddresses = future.getNow();
                    int numAddresses = inetAddresses.size();
                    if (numAddresses > 0) {
                        // if there are multiple addresses: we shall pick one by one
                        // to support the round robin distribution
                        promise.setSuccess(inetAddresses.get(randomIndex(numAddresses)));
                    } else {
                        promise.setFailure(new UnknownHostException(inetHost));
                    }
                } else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }

    @Override
    protected void doResolveAll(String inetHost, final Promise<List<InetAddress>> promise) throws Exception {
        nameResolver.resolveAll(inetHost).addListener(new FutureListener<List<InetAddress>>() {
            @Override
            public void operationComplete(Future<List<InetAddress>> future) throws Exception {
                if (future.isSuccess()) {
                    List<InetAddress> inetAddresses = future.getNow();
                    if (!inetAddresses.isEmpty()) {
                        // create a copy to make sure that it's modifiable random access collection
                        List<InetAddress> result = new ArrayList<InetAddress>(inetAddresses);
                        // rotate by different distance each time to force round robin distribution
                        Collections.rotate(result, randomIndex(inetAddresses.size()));
                        promise.setSuccess(result);
                    } else {
                        promise.setSuccess(inetAddresses);
                    }
                } else {
                    promise.setFailure(future.cause());
                }
            }
        });
    }

    private static int randomIndex(int numAddresses) {
        return numAddresses == 1 ? 0 : PlatformDependent.threadLocalRandom().nextInt(numAddresses);
    }

    @Override
    public void close() {
        nameResolver.close();
    }

}
