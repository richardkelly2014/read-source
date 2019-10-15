package com.demo.util.internal;

import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.util.Deque;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class PlatformDependent {


    private static final Pattern MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN = Pattern.compile(
            "\\s*-XX:MaxDirectMemorySize\\s*=\\s*([0-9]+)\\s*([kKmMgG]?)\\s*$");

    private static final boolean IS_WINDOWS = isWindows0();
    private static final boolean IS_OSX = isOsx0();
    private static final boolean IS_J9_JVM = isJ9Jvm0();
    private static final boolean IS_IVKVM_DOT_NET = isIkvmDotNet0();

    private static final boolean MAYBE_SUPER_USER;

    private static final boolean CAN_ENABLE_TCP_NODELAY_BY_DEFAULT = !isAndroid();

    private static final Throwable UNSAFE_UNAVAILABILITY_CAUSE = unsafeUnavailabilityCause0();
    //private static final boolean DIRECT_BUFFER_PREFERRED;
    private static final long MAX_DIRECT_MEMORY = maxDirectMemory0();


    private static final ThreadLocalRandomProvider RANDOM_PROVIDER;
    private static final Cleaner CLEANER;
    private static final int UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD;


    private static final boolean USE_DIRECT_BUFFER_NO_CLEANER;
    private static final AtomicLong DIRECT_MEMORY_COUNTER;
    private static final long DIRECT_MEMORY_LIMIT;

    private static final Cleaner NOOP = new Cleaner() {
        @Override
        public void freeDirectBuffer(ByteBuffer buffer) {
            // NOOP
        }
    };

    static {
        RANDOM_PROVIDER = new ThreadLocalRandomProvider() {
            @Override
            public Random current() {
                return java.util.concurrent.ThreadLocalRandom.current();
            }
        };

        long maxDirectMemory = SystemPropertyUtil.getLong("io.netty.maxDirectMemory", -1);
        if (maxDirectMemory == 0 || !hasUnsafe() || !PlatformDependent0.hasDirectBufferNoCleanerConstructor()) {
            USE_DIRECT_BUFFER_NO_CLEANER = false;
            DIRECT_MEMORY_COUNTER = null;
        } else {
            USE_DIRECT_BUFFER_NO_CLEANER = true;
            if (maxDirectMemory < 0) {
                maxDirectMemory = MAX_DIRECT_MEMORY;
                if (maxDirectMemory <= 0) {
                    DIRECT_MEMORY_COUNTER = null;
                } else {
                    DIRECT_MEMORY_COUNTER = new AtomicLong();
                }
            } else {
                DIRECT_MEMORY_COUNTER = new AtomicLong();
            }
        }

        DIRECT_MEMORY_LIMIT = maxDirectMemory >= 1 ? maxDirectMemory : MAX_DIRECT_MEMORY;

        int tryAllocateUninitializedArray =
                SystemPropertyUtil.getInt("io.netty.uninitializedArrayAllocationThreshold", 1024);
        UNINITIALIZED_ARRAY_ALLOCATION_THRESHOLD = javaVersion() >= 9 && PlatformDependent0.hasAllocateArrayMethod() ?
                tryAllocateUninitializedArray : -1;

        MAYBE_SUPER_USER = maybeSuperUser0();

        if (!isAndroid()) {
            // only direct to method if we are not running on android.
            // See https://github.com/netty/netty/issues/2604
            if (javaVersion() >= 9) {
                CLEANER = CleanerJava9.isSupported() ? new CleanerJava9() : NOOP;
            } else {
                CLEANER = CleanerJava6.isSupported() ? new CleanerJava6() : NOOP;
            }
        } else {
            CLEANER = NOOP;
        }


    }

    public static boolean isAndroid() {

        return PlatformDependent0.isAndroid();
    }

    public static boolean isWindows() {

        return IS_WINDOWS;
    }

    public static boolean isOsx() {

        return IS_OSX;
    }

    public static boolean maybeSuperUser() {

        return MAYBE_SUPER_USER;
    }

    public static int javaVersion() {

        return PlatformDependent0.javaVersion();
    }

    public static boolean canEnableTcpNoDelayByDefault() {

        return CAN_ENABLE_TCP_NODELAY_BY_DEFAULT;
    }

    public static boolean hasUnsafe() {
        return UNSAFE_UNAVAILABILITY_CAUSE == null;
    }

    /**
     * Return the {@link ClassLoader} for the given {@link Class}.
     */
    public static ClassLoader getClassLoader(final Class<?> clazz) {
        return PlatformDependent0.getClassLoader(clazz);
    }

    /**
     * Return the context {@link ClassLoader} for the current {@link Thread}.
     */
    public static ClassLoader getContextClassLoader() {
        return PlatformDependent0.getContextClassLoader();
    }

    /**
     * Return the system {@link ClassLoader}.
     */
    public static ClassLoader getSystemClassLoader() {
        return PlatformDependent0.getSystemClassLoader();
    }

    /**
     * get Queue
     *
     * @param <C>
     * @return
     */
    public static <C> Deque<C> newConcurrentDeque() {
        if (javaVersion() < 7) {
            return new LinkedBlockingDeque<C>();
        } else {
            return new ConcurrentLinkedDeque<C>();
        }
    }

    /**
     * Return a {@link Random} which is not-threadsafe and so can only be used from the same thread.
     */
    public static Random threadLocalRandom() {
        return RANDOM_PROVIDER.current();
    }


    private static boolean isWindows0() {
        boolean windows = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US).contains("win");
        return windows;
    }

    private static boolean isOsx0() {
        String osname = SystemPropertyUtil.get("os.name", "").toLowerCase(Locale.US)
                .replaceAll("[^a-z0-9]+", "");
        boolean osx = osname.startsWith("macosx") || osname.startsWith("osx");
        return osx;
    }

    private static boolean maybeSuperUser0() {
        String username = SystemPropertyUtil.get("user.name");
        if (isWindows()) {
            return "Administrator".equals(username);
        }
        // Check for root and toor as some BSDs have a toor user that is basically the same as root.
        return "root".equals(username) || "toor".equals(username);
    }

    private static Throwable unsafeUnavailabilityCause0() {
        if (isAndroid()) {
            return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (Android)");
        }

        if (isIkvmDotNet()) {
            return new UnsupportedOperationException("sun.misc.Unsafe: unavailable (IKVM.NET)");
        }

        Throwable cause = PlatformDependent0.getUnsafeUnavailabilityCause();
        if (cause != null) {
            return cause;
        }

        try {
            boolean hasUnsafe = PlatformDependent0.hasUnsafe();
            return hasUnsafe ? null : PlatformDependent0.getUnsafeUnavailabilityCause();
        } catch (Throwable t) {
            // Probably failed to initialize PlatformDependent0.
            return new UnsupportedOperationException("Could not determine if Unsafe is available", t);
        }
    }

    /**
     * Returns {@code true} if the running JVM is either <a href="https://developer.ibm.com/javasdk/">IBM J9</a> or
     * <a href="https://www.eclipse.org/openj9/">Eclipse OpenJ9</a>, {@code false} otherwise.
     */
    public static boolean isJ9Jvm() {
        return IS_J9_JVM;
    }

    private static boolean isJ9Jvm0() {
        String vmName = SystemPropertyUtil.get("java.vm.name", "").toLowerCase();
        return vmName.startsWith("ibm j9") || vmName.startsWith("eclipse openj9");
    }

    /**
     * Returns {@code true} if the running JVM is <a href="https://www.ikvm.net">IKVM.NET</a>, {@code false} otherwise.
     */
    public static boolean isIkvmDotNet() {
        return IS_IVKVM_DOT_NET;
    }

    private static boolean isIkvmDotNet0() {
        String vmName = SystemPropertyUtil.get("java.vm.name", "").toUpperCase(Locale.US);
        return vmName.equals("IKVM.NET");
    }

    private static long maxDirectMemory0() {
        long maxDirectMemory = 0;

        ClassLoader systemClassLoader = null;
        try {
            systemClassLoader = getSystemClassLoader();

            // When using IBM J9 / Eclipse OpenJ9 we should not use VM.maxDirectMemory() as it not reflects the
            // correct value.
            // See:
            //  - https://github.com/netty/netty/issues/7654
            String vmName = SystemPropertyUtil.get("java.vm.name", "").toLowerCase();
            if (!vmName.startsWith("ibm j9") &&
                    // https://github.com/eclipse/openj9/blob/openj9-0.8.0/runtime/include/vendor_version.h#L53
                    !vmName.startsWith("eclipse openj9")) {
                // Try to get from sun.misc.VM.maxDirectMemory() which should be most accurate.
                Class<?> vmClass = Class.forName("sun.misc.VM", true, systemClassLoader);
                Method m = vmClass.getDeclaredMethod("maxDirectMemory");
                maxDirectMemory = ((Number) m.invoke(null)).longValue();
            }
        } catch (Throwable ignored) {
            // Ignore
        }

        if (maxDirectMemory > 0) {
            return maxDirectMemory;
        }

        try {
            // Now try to get the JVM option (-XX:MaxDirectMemorySize) and parse it.
            // Note that we are using reflection because Android doesn't have these classes.
            Class<?> mgmtFactoryClass = Class.forName(
                    "java.lang.management.ManagementFactory", true, systemClassLoader);
            Class<?> runtimeClass = Class.forName(
                    "java.lang.management.RuntimeMXBean", true, systemClassLoader);

            Object runtime = mgmtFactoryClass.getDeclaredMethod("getRuntimeMXBean").invoke(null);

            @SuppressWarnings("unchecked")
            List<String> vmArgs = (List<String>) runtimeClass.getDeclaredMethod("getInputArguments").invoke(runtime);
            for (int i = vmArgs.size() - 1; i >= 0; i--) {
                Matcher m = MAX_DIRECT_MEMORY_SIZE_ARG_PATTERN.matcher(vmArgs.get(i));
                if (!m.matches()) {
                    continue;
                }

                maxDirectMemory = Long.parseLong(m.group(1));
                switch (m.group(2).charAt(0)) {
                    case 'k':
                    case 'K':
                        maxDirectMemory *= 1024;
                        break;
                    case 'm':
                    case 'M':
                        maxDirectMemory *= 1024 * 1024;
                        break;
                    case 'g':
                    case 'G':
                        maxDirectMemory *= 1024 * 1024 * 1024;
                        break;
                }
                break;
            }
        } catch (Throwable ignored) {
            // Ignore
        }

        if (maxDirectMemory <= 0) {
            maxDirectMemory = Runtime.getRuntime().maxMemory();
        }

        return maxDirectMemory;
    }


    private static String normalizeOsReleaseVariableValue(String value) {
        // Variable assignment values may be enclosed in double or single quotes.
        return value.trim().replaceAll("[\"']", "");
    }

    private static String normalize(String value) {
        return value.toLowerCase(Locale.US).replaceAll("[^a-z0-9]+", "");
    }

    private static String normalizeArch(String value) {
        value = normalize(value);
        if (value.matches("^(x8664|amd64|ia32e|em64t|x64)$")) {
            return "x86_64";
        }
        if (value.matches("^(x8632|x86|i[3-6]86|ia32|x32)$")) {
            return "x86_32";
        }
        if (value.matches("^(ia64|itanium64)$")) {
            return "itanium_64";
        }
        if (value.matches("^(sparc|sparc32)$")) {
            return "sparc_32";
        }
        if (value.matches("^(sparcv9|sparc64)$")) {
            return "sparc_64";
        }
        if (value.matches("^(arm|arm32)$")) {
            return "arm_32";
        }
        if ("aarch64".equals(value)) {
            return "aarch_64";
        }
        if (value.matches("^(ppc|ppc32)$")) {
            return "ppc_32";
        }
        if ("ppc64".equals(value)) {
            return "ppc_64";
        }
        if ("ppc64le".equals(value)) {
            return "ppcle_64";
        }
        if ("s390".equals(value)) {
            return "s390_32";
        }
        if ("s390x".equals(value)) {
            return "s390_64";
        }

        return "unknown";
    }

    private static String normalizeOs(String value) {
        value = normalize(value);
        if (value.startsWith("aix")) {
            return "aix";
        }
        if (value.startsWith("hpux")) {
            return "hpux";
        }
        if (value.startsWith("os400")) {
            // Avoid the names such as os4000
            if (value.length() <= 5 || !Character.isDigit(value.charAt(5))) {
                return "os400";
            }
        }
        if (value.startsWith("linux")) {
            return "linux";
        }
        if (value.startsWith("macosx") || value.startsWith("osx")) {
            return "osx";
        }
        if (value.startsWith("freebsd")) {
            return "freebsd";
        }
        if (value.startsWith("openbsd")) {
            return "openbsd";
        }
        if (value.startsWith("netbsd")) {
            return "netbsd";
        }
        if (value.startsWith("solaris") || value.startsWith("sunos")) {
            return "sunos";
        }
        if (value.startsWith("windows")) {
            return "windows";
        }

        return "unknown";
    }

    private static final class AtomicLongCounter extends AtomicLong implements LongCounter {
        private static final long serialVersionUID = 4074772784610639305L;

        @Override
        public void add(long delta) {
            addAndGet(delta);
        }

        @Override
        public void increment() {
            incrementAndGet();
        }

        @Override
        public void decrement() {
            decrementAndGet();
        }

        @Override
        public long value() {
            return get();
        }
    }

    private interface ThreadLocalRandomProvider {
        Random current();
    }

    private PlatformDependent() {
        // only static method supported
    }
}
