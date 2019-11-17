package com.pi4jl.system;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class NetworkInterface {

    private final String linkEncap;
    private final String ipAddress;
    private final String macAddress;
    private final String broadcastAddress;
    private final String subnetMask;
    private final String mtu;
    private final String metric;

    public NetworkInterface(String linkEncap,String macAddress,String ipAddress,String broadcastAddress,String subnetMask,String mtu,String metric) {
        this.linkEncap = linkEncap;
        this.ipAddress = ipAddress;
        this.macAddress = macAddress;
        this.broadcastAddress = broadcastAddress;
        this.subnetMask = subnetMask;
        this.mtu = mtu;
        this.metric = metric;
    }

    public String getLinkEncap() {
        return linkEncap;
    }

    public String getIPAddress() {
        return ipAddress;
    }

    public String getMACAddress() {
        return macAddress;
    }

    public String getBroadcastAddress() {
        return broadcastAddress;
    }

    public String getSubnetMask() {
        return subnetMask;
    }

    public String getMTU() {
        return mtu;
    }

    public String getMetric() {
        return metric;
    }
}
