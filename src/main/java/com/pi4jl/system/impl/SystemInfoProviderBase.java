package com.pi4jl.system.impl;

import com.pi4jl.system.SystemInfo;
import com.pi4jl.system.SystemInfoProvider;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by jiangfei on 2019/11/17.
 */
public abstract class SystemInfoProviderBase implements SystemInfoProvider {

    @Override
    public String getProcessor() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getBogoMIPS() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String[] getCpuFeatures() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuImplementer() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuArchitecture() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuVariant() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuPart() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getCpuRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getHardware() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRevision() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getSerial() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsName() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsVersion() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsArch() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsFirmwareBuild() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getOsFirmwareDate() throws IOException, InterruptedException, ParseException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVendor() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVendorUrl() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVersion() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaVirtualMachine() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getJavaRuntime() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHardFloatAbi() throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryTotal() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryUsed() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryFree() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryShared() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryBuffers() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getMemoryCached() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCodecH264Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCodecMPG2Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean getCodecWVC1Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyArm() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyCore() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyH264() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyISP() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyV3D() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyUART() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyPWM() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyEMMC() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyPixel() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyVEC() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyHDMI() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public long getClockFrequencyDPI() throws IOException, InterruptedException, UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

}
