package com.pi4jl.system.impl;

import com.pi4jl.system.SystemInfoProvider;
import com.pi4jl.util.ExecUtil;
import com.pi4jl.util.StringUtil;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangfei on 2019/11/17.
 */
public abstract class DefaultSystemInfoProvider extends SystemInfoProviderBase implements SystemInfoProvider {

    private static Map<String, String> cpuInfo;


    /**
     * /proc/cpuinfo 文件存储 cpu信息
     *
     * @param target
     * @return
     * @throws IOException
     * @throws InterruptedException
     * @throws UnsupportedOperationException
     */
    protected String getCpuInfo(String target) throws IOException, InterruptedException, UnsupportedOperationException {
        // if the CPU data has not been previously acquired, then acquire it now
        if (cpuInfo == null) {
            cpuInfo = new HashMap<>();

            try (BufferedReader br = new BufferedReader(new FileReader("/proc/cpuinfo"))) {
                for (String line; (line = br.readLine()) != null; ) {
                    String parts[] = line.split(":", 2);
                    if (parts.length >= 2 && !parts[0].trim().isEmpty() && !parts[1].trim().isEmpty()) {
                        String cpuKey = parts[0].trim();
                        cpuInfo.put(cpuKey, parts[1].trim());
                    }
                }
            }
        }
        if (cpuInfo.containsKey(target)) {
            return cpuInfo.get(target);
        }
        throw new UnsupportedOperationException("Invalid target: " + target);
    }

    //bash --version
    protected String getBashVersionInfo() {
        String versionInfo = "";
        try {
            String result[] = ExecUtil.execute("bash --version");
            for (String line : result) {
                if (!line.isEmpty()) {
                    versionInfo = line; // return only first output line of version info
                    break;
                }
            }
        } catch (IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
        return versionInfo;
    }

    /*
     * this method will determine if a specified tag exists from the elf info in the '/proc/self/exe' program
     * (this method is used to help determine the HARD-FLOAT / SOFT-FLOAT ABI of the system)
     */
    protected boolean hasReadElfTag(String tag) {
        String tagValue = getReadElfTag(tag);
        return (tagValue != null && !tagValue.isEmpty());
    }

    /*
     * this method will obtain a specified tag value from the elf info in the '/proc/self/exe' program
     * (this method is used to help determine the HARD-FLOAT / SOFT-FLOAT ABI of the system)
     */
    protected String getReadElfTag(String tag) {
        String tagValue = null;
        try {
            String result[] = ExecUtil.execute("/usr/bin/readelf -A /proc/self/exe");
            if (result != null) {
                for (String line : result) {
                    line = line.trim();
                    if (line.startsWith(tag) && line.contains(":")) {
                        String lineParts[] = line.split(":", 2);
                        if (lineParts.length > 1)
                            tagValue = lineParts[1].trim();
                        break;
                    }
                }
            }
        } catch (IOException | InterruptedException ioe) {
            ioe.printStackTrace();
        }
        return tagValue;
    }

    protected List<Long> getMemory() throws IOException, InterruptedException {
        // Memory information is in the form
        // root@mypi:/home/pi# free -b
        //              total       used       free     shared    buffers     cached
        // Mem:     459771904  144654336  315117568          0   21319680   63713280
        // -/+ buffers/cache:   59621376  400150528
        // Swap:    104853504          0  104853504
        List<Long> values = new ArrayList<>();
        String result[] = ExecUtil.execute("free -b");
        if (result != null) {
            for (String line : result) {
                if (line.startsWith("Mem:")) {
                    String parts[] = line.split(" ");
                    for (String part : parts) {
                        part = part.trim();
                        if (!part.isEmpty() && !part.equalsIgnoreCase("Mem:")) {
                            values.add(new Long(part));
                        }
                    }
                }
            }
        }
        return values;
    }

    @Override
    public String getProcessor() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("processor");
    }

    @Override
    public String getBogoMIPS() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("BogoMIPS");
    }

    @Override
    public String[] getCpuFeatures() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("Features").split(" ");
    }

    @Override
    public String getCpuImplementer() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("CPU implementer");
    }

    @Override
    public String getCpuArchitecture() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("CPU architecture");
    }

    @Override
    public String getCpuVariant() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("CPU variant");
    }

    @Override
    public String getCpuPart() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("CPU part");
    }

    @Override
    public String getCpuRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("CPU revision");
    }

    @Override
    public String getHardware() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("Hardware");
    }

    @Override
    public String getRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("Revision");
    }

    @Override
    public String getSerial() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("Serial");
    }

    @Override
    public String getOsName() throws UnsupportedOperationException {
        return System.getProperty("os.name");
    }

    @Override
    public String getOsVersion() throws UnsupportedOperationException {
        return System.getProperty("os.version");
    }

    @Override
    public String getOsArch() throws UnsupportedOperationException {
        return System.getProperty("os.arch");
    }

    @Override
    public String getJavaVendor() throws UnsupportedOperationException {
        return System.getProperty("java.vendor");
    }

    @Override
    public String getJavaVendorUrl() throws UnsupportedOperationException {
        return System.getProperty("java.vendor.url");
    }

    @Override
    public String getJavaVersion() throws UnsupportedOperationException {
        return System.getProperty("java.version");
    }

    @Override
    public String getJavaVirtualMachine() throws UnsupportedOperationException {
        return System.getProperty("java.vm.name");
    }

    @Override
    public String getJavaRuntime() throws UnsupportedOperationException {
        return AccessController.doPrivileged(new PrivilegedAction<String>() {
            public String run() {
                return System.getProperty("java.runtime.name");
            }
        });
    }

    /*
     * this method was partially derived from :: (project) jogamp / (developer) sgothel
     * https://github.com/sgothel/gluegen/blob/master/src/java/jogamp/common/os/PlatformPropsImpl.java#L160
     * https://github.com/sgothel/gluegen/blob/master/LICENSE.txt
     */
    @Override
    public boolean isHardFloatAbi() throws UnsupportedOperationException {
        return AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
            private final String[] gnueabihf = new String[]{"gnueabihf", "armhf"};

            public Boolean run() {
                return (StringUtil.contains(System.getProperty("sun.boot.library.path"), gnueabihf) ||
                        StringUtil.contains(System.getProperty("java.library.path"), gnueabihf) ||
                        StringUtil.contains(System.getProperty("java.home"), gnueabihf) ||
                        getBashVersionInfo().contains("gnueabihf") ||
                        hasReadElfTag("Tag_ABI_HardFP_use"));
            }
        });
    }

    @Override
    public long getMemoryTotal() throws IOException, InterruptedException, UnsupportedOperationException {
        List<Long> values = getMemory();
        if (!values.isEmpty() && values.size() > 0) {
            return values.get(0); // total memory value is in first position
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryUsed() throws IOException, InterruptedException, UnsupportedOperationException {
        List<Long> values = getMemory();
        if (!values.isEmpty() && values.size() > 1) {
            return values.get(1); // used memory value is in second position
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryFree() throws IOException, InterruptedException, UnsupportedOperationException {
        List<Long> values = getMemory();
        if (!values.isEmpty() && values.size() > 2) {
            return values.get(2); // free memory value is in third position
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryShared() throws IOException, InterruptedException, UnsupportedOperationException {
        List<Long> values = getMemory();
        if (!values.isEmpty() && values.size() > 3) {
            return values.get(3); // shared memory value is in fourth position
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryBuffers() throws IOException, InterruptedException, UnsupportedOperationException {
        List<Long> values = getMemory();
        if (!values.isEmpty() && values.size() > 4) {
            return values.get(4); // buffers memory value is in fifth position
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryCached() throws IOException, InterruptedException, UnsupportedOperationException {
        List<Long> values = getMemory();
        if (!values.isEmpty() && values.size() > 5) {
            return values.get(5); // cached memory value is in sixth position
        }
        throw new UnsupportedOperationException();
    }

}
