package com.demo.bootstrap;

import com.demo.channel.Channel;
import com.demo.resolver.AddressResolverGroup;

import java.net.SocketAddress;

/**
 * 启动类配置文件
 * Created by jiangfei on 2019/11/2.
 */
public final class BootstrapConfig extends AbstractBootstrapConfig<Bootstrap, Channel> {

    BootstrapConfig(Bootstrap bootstrap) {
        super(bootstrap);
    }

    /**
     * Returns the configured remote address or {@code null} if non is configured yet.
     */
    public SocketAddress remoteAddress() {
        return bootstrap.remoteAddress();
    }

    /**
     * Returns the configured {@link AddressResolverGroup} or the default if non is configured yet.
     */
    public AddressResolverGroup<?> resolver() {
        return bootstrap.resolver();
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder(super.toString());
        buf.setLength(buf.length() - 1);
        buf.append(", resolver: ").append(resolver());
        SocketAddress remoteAddress = remoteAddress();
        if (remoteAddress != null) {
            buf.append(", remoteAddress: ")
                    .append(remoteAddress);
        }
        return buf.append(')').toString();
    }
}
