package com.pi4jl.system;

import com.pi4jl.platform.PlatformManager;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class SystemInfoFactory {

    // we only allow a single default provider to exists
    private static SystemInfoProvider provider = null;

    // private constructor
    private SystemInfoFactory() {
        // forbid object construction
    }

    public static SystemInfoProvider getProvider() {
        // if a provider has not been created, then create a new instance
        if (provider == null) {
            // create the provider based on the PlatformManager's selected platform
            provider = PlatformManager.getPlatform().getSystemInfoProvider();
        }

        // return the provider instance
        return provider;
    }

    public static void setProvider(SystemInfoProvider provider) {
        // set the default provider instance
        SystemInfoFactory.provider = provider;
    }

}
