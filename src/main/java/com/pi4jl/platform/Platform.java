package com.pi4jl.platform;

import com.pi4jl.system.SystemInfoProvider;
import com.pi4jl.system.impl.RaspiSystemInfoProvider;

/**
 * Created by jiangfei on 2019/11/16.
 */
public enum Platform {

    RASPBERRYPI("raspberrypi", "Raspberry Pi"),
    BANANAPI("bananapi", "BananaPi"),
    BANANAPRO("bananapro", "BananaPro"),
    BPI("bpi", "Synovoip BPI"),
    ODROID("odroid", "Odroid"),
    ORANGEPI("orangepi", "OrangePi"),
    NANOPI("nanopi", "NanoPi"),
    SIMULATED("simulated", "Simulated");

    protected String platformId = null;
    protected String label = null;

    Platform(String platformId, String label) {
        this.platformId = platformId;
        this.label = label;
    }

    public String getLabel() {
        return this.label;
    }

    /**
     * Get the platform's friendly string name/label.
     *
     * @return label of platform
     */
    public String label() {
        return getLabel();
    }

    /**
     * Get the platform's unique identifier string.
     *
     * @return platform id string
     */
    public String getId() {
        return platformId;
    }

    /**
     * Get the platform's unique identifier string.
     *
     * @return platform id string
     */
    public String id() {
        return getId();
    }

    /**
     * Lookup a platform enumeration by the platform's unique identifier string.
     *
     * @return platform enumeration
     */
    public static Platform fromId(String platformId) {
        for (Platform platform : Platform.values()) {
            if (platform.id().equalsIgnoreCase(platformId))
                return platform;
        }
        return null;
    }



    public SystemInfoProvider getSystemInfoProvider() {
        return getSystemInfoProvider(this);
    }

    public static SystemInfoProvider getSystemInfoProvider(Platform platform) {
        // return the system info provider based on the provided platform
        switch(platform) {
            case RASPBERRYPI: {
                return new RaspiSystemInfoProvider();
            }
//            case BANANAPI: {
//                return new BananaPiSystemInfoProvider();
//            }
//            case BANANAPRO: {
//                return new BananaProSystemInfoProvider();
//            }
//            case BPI: {
//                return new BpiSystemInfoProvider();
//            }
//            case ODROID: {
//                return new OdroidSystemInfoProvider();
//            }
//            case ORANGEPI: {
//                return new OrangePiSystemInfoProvider();
//            }
//            case NANOPI: {
//                return new NanoPiSystemInfoProvider();
//            }
            default: {
                // if a platform cannot be determine, then assume it's the default RaspberryPi
                return new RaspiSystemInfoProvider();
            }
        }
    }
}
