package com.demo.util;

import com.demo.util.internal.ObjectUtil;
import com.demo.util.internal.PlatformDependent;
import com.demo.util.internal.SystemPropertyUtil;

import java.lang.reflect.Constructor;

/**
 * Created by jiangfei on 2019/10/23.
 */
public abstract class ResourceLeakDetectorFactory {

    private static volatile ResourceLeakDetectorFactory factoryInstance = new DefaultResourceLeakDetectorFactory();

    public static ResourceLeakDetectorFactory instance() {
        return factoryInstance;
    }

    public static void setResourceLeakDetectorFactory(ResourceLeakDetectorFactory factory) {
        factoryInstance = ObjectUtil.checkNotNull(factory, "factory");
    }


    public final <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource) {
        return newResourceLeakDetector(resource, ResourceLeakDetector.SAMPLING_INTERVAL);
    }

    public abstract <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval, long maxActive);

    public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval) {
        return newResourceLeakDetector(resource, ResourceLeakDetector.SAMPLING_INTERVAL, Long.MAX_VALUE);
    }


    private static final class DefaultResourceLeakDetectorFactory extends ResourceLeakDetectorFactory {
        private final Constructor<?> obsoleteCustomClassConstructor;
        private final Constructor<?> customClassConstructor;

        DefaultResourceLeakDetectorFactory() {
            String customLeakDetector;
            try {
                customLeakDetector = SystemPropertyUtil.get("io.netty.customResourceLeakDetector");
            } catch (Throwable cause) {
                //logger.error("Could not access System property: io.netty.customResourceLeakDetector", cause);
                customLeakDetector = null;
            }
            if (customLeakDetector == null) {
                obsoleteCustomClassConstructor = customClassConstructor = null;
            } else {
                obsoleteCustomClassConstructor = obsoleteCustomClassConstructor(customLeakDetector);
                customClassConstructor = customClassConstructor(customLeakDetector);
            }
        }

        private static Constructor<?> obsoleteCustomClassConstructor(String customLeakDetector) {
            try {
                final Class<?> detectorClass = Class.forName(customLeakDetector, true,
                        PlatformDependent.getSystemClassLoader());

                if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
                    return detectorClass.getConstructor(Class.class, int.class, long.class);
                } else {
                    //logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
                }
            } catch (Throwable t) {
//                logger.error("Could not load custom resource leak detector class provided: {}",
//                        customLeakDetector, t);
            }
            return null;
        }

        private static Constructor<?> customClassConstructor(String customLeakDetector) {
            try {
                final Class<?> detectorClass = Class.forName(customLeakDetector, true,
                        PlatformDependent.getSystemClassLoader());

                if (ResourceLeakDetector.class.isAssignableFrom(detectorClass)) {
                    return detectorClass.getConstructor(Class.class, int.class);
                } else {
                    //logger.error("Class {} does not inherit from ResourceLeakDetector.", customLeakDetector);
                }
            } catch (Throwable t) {
//                logger.error("Could not load custom resource leak detector class provided: {}",
//                        customLeakDetector, t);
            }
            return null;
        }

        @SuppressWarnings("deprecation")
        @Override
        public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval,
                                                                   long maxActive) {
            if (obsoleteCustomClassConstructor != null) {
                try {
                    @SuppressWarnings("unchecked")
                    ResourceLeakDetector<T> leakDetector =
                            (ResourceLeakDetector<T>) obsoleteCustomClassConstructor.newInstance(
                                    resource, samplingInterval, maxActive);
                    return leakDetector;
                } catch (Throwable t) {

                }
            }

            ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<T>(resource, samplingInterval,
                    maxActive);
            return resourceLeakDetector;
        }

        @Override
        public <T> ResourceLeakDetector<T> newResourceLeakDetector(Class<T> resource, int samplingInterval) {
            if (customClassConstructor != null) {
                try {
                    @SuppressWarnings("unchecked")
                    ResourceLeakDetector<T> leakDetector =
                            (ResourceLeakDetector<T>) customClassConstructor.newInstance(resource, samplingInterval);

                    return leakDetector;
                } catch (Throwable t) {

                }
            }

            ResourceLeakDetector<T> resourceLeakDetector = new ResourceLeakDetector<T>(resource, samplingInterval);
            
            return resourceLeakDetector;
        }
    }
}
