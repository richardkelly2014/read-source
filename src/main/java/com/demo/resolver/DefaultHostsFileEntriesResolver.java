package com.demo.resolver;

import com.demo.util.CharsetUtil;
import com.demo.util.internal.PlatformDependent;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map;

public final class DefaultHostsFileEntriesResolver implements HostsFileEntriesResolver {

    private final Map<String, Inet4Address> inet4Entries;
    private final Map<String, Inet6Address> inet6Entries;

    public DefaultHostsFileEntriesResolver() {
        this(parseEntries());
    }

    // for testing purpose only
    DefaultHostsFileEntriesResolver(HostsFileEntries entries) {
        inet4Entries = entries.inet4Entries();
        inet6Entries = entries.inet6Entries();
    }

    @Override
    public InetAddress address(String inetHost, ResolvedAddressTypes resolvedAddressTypes) {
        String normalized = normalize(inetHost);
        switch (resolvedAddressTypes) {
            case IPV4_ONLY:
                return inet4Entries.get(normalized);
            case IPV6_ONLY:
                return inet6Entries.get(normalized);
            case IPV4_PREFERRED:
                Inet4Address inet4Address = inet4Entries.get(normalized);
                return inet4Address != null ? inet4Address : inet6Entries.get(normalized);
            case IPV6_PREFERRED:
                Inet6Address inet6Address = inet6Entries.get(normalized);
                return inet6Address != null ? inet6Address : inet4Entries.get(normalized);
            default:
                throw new IllegalArgumentException("Unknown ResolvedAddressTypes " + resolvedAddressTypes);
        }
    }

    // package-private for testing purposes
    String normalize(String inetHost) {
        return inetHost.toLowerCase(Locale.ENGLISH);
    }


    private static HostsFileEntries parseEntries() {
        if (PlatformDependent.isWindows()) {

            return HostsFileParser.parseSilently(Charset.defaultCharset(), CharsetUtil.UTF_16, CharsetUtil.UTF_8);
        }
        return HostsFileParser.parseSilently();
    }

}
