package com.demo.resolver;

import java.net.InetAddress;

public interface HostsFileEntriesResolver {

    HostsFileEntriesResolver DEFAULT = new DefaultHostsFileEntriesResolver();

    InetAddress address(String inetHost, ResolvedAddressTypes resolvedAddressTypes);
}
