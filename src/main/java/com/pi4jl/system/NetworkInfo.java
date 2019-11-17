package com.pi4jl.system;

import com.pi4jl.util.ExecUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class NetworkInfo {

    // private constructor
    private NetworkInfo() {
        // forbid object construction
    }

    public static String getHostname() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --short")[0];
    }

    public static String getFQDN() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --fqdn")[0];
    }

    public static String[] getIPAddresses() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --all-ip-addresses", " ");
    }

    public static String getIPAddress() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname -i")[0];
    }

    public static String[] getFQDNs() throws IOException, InterruptedException {
        return ExecUtil.execute("hostname --all-fqdns", " ");
    }

    public static String[] getNameservers() throws IOException, InterruptedException {
        String[] nameservers = ExecUtil.execute("cat /etc/resolv.conf");
        List<String> result = new ArrayList<>();
        for (String nameserver : nameservers) {
            if (nameserver.startsWith("nameserver")) {
                result.add(nameserver.substring(11).trim());
            }
        }
        return result.toArray(new String[result.size()]);
    }

}
