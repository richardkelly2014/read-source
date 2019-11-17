package com.pi4jl.system.impl;

import com.pi4jl.system.SystemInfo;
import com.pi4jl.system.SystemInfoProvider;
import com.pi4jl.util.ExecUtil;

import java.io.IOException;
import java.text.ParseException;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class RaspiSystemInfoProvider extends DefaultSystemInfoProvider implements SystemInfoProvider {

    private long getClockFrequency(String target) throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_clock " + target.trim());
        if (result != null && result.length > 0) {
            for (String line : result) {
                String parts[] = line.split("=", 2);
                return Long.parseLong(parts[1].trim());
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    private boolean getCodecEnabled(String codec) throws IOException, InterruptedException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd codec_enabled " + codec);
        if (result != null && result.length > 0) {
            for (String line : result) {
                String parts[] = line.split("=", 2);
                return parts[1].trim().equalsIgnoreCase("enabled");
            }
        }
        throw new RuntimeException("Invalid command or response.");
    }

    private float getVoltage(String id) throws IOException, InterruptedException, NumberFormatException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_volts " + id);
        if (result != null && result.length > 0) {
            for (String line : result) {
                String parts[] = line.split("[=V]", 3);
                return Float.parseFloat(parts[1]);
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    @Override
    public String getModelName() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCpuInfo("model name");
    }

    @Override
    public String getOsFirmwareBuild() throws IOException, InterruptedException, UnsupportedOperationException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd version");
        if (result != null) {
            for (String line : result) {
                if (line.startsWith("version ")) {
                    return line.substring(8);
                }
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    @Override
    public String getOsFirmwareDate() throws IOException, InterruptedException, ParseException, UnsupportedOperationException {
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd version");
        if (result != null && result.length > 0) {
            for (String line : result) {
                return line; // return 1st line
            }
        }
        throw new UnsupportedOperationException("Invalid command or response.");
    }

    // Raspberry Pi Revision :: Model
    public static final short RPI_MODEL_A = 0;
    public static final short RPI_MODEL_B = 1;
    public static final short RPI_MODEL_A_PLUS = 2;
    public static final short RPI_MODEL_B_PLUS = 3;
    public static final short RPI_MODEL_2B = 4;
    public static final short RPI_MODEL_ALPHA = 5;
    public static final short RPI_MODEL_CM = 6;
    public static final short RPI_MODEL_UNKNOWN = 7;
    public static final short RPI_MODEL_3B = 8;
    public static final short RPI_MODEL_ZERO = 9;
    public static final short RPI_MODEL_CM3 = 10;
    public static final short RPI_MODEL_ZERO_W = 12;
    public static final short RPI_MODEL_3B_PLUS = 13;

    // Raspberry Pi Revision :: Memory
    public static final short RPI_RAM_256 = 0;
    public static final short RPI_RAM_512 = 1;
    public static final short RPI_RAM_1024 = 2;

    // Raspberry Pi Revision :: Manufacture
    public static final short RPI_MFG_SONY = 0;
    public static final short RPI_MFG_EGOMAN = 1;
    public static final short RPI_MFG_EMBEST = 2;
    public static final short RPI_MFG_UNKNOWN = 3;
    public static final short RPI_MFG_EMBEST2 = 4;

    // Raspberry Pi Revision :: Processor
    public static final short RPI_PROC_BCM2835 = 0;
    public static final short RPI_PROC_BCM2836 = 1;
    public static final short RPI_PROC_BCM2837 = 2;


    @Override
    public SystemInfo.BoardType getBoardType() throws IOException, InterruptedException, UnsupportedOperationException {
        String revision = getRevision();

        // determine the board info by deciphering the revision number
        long irevision = Long.parseLong(revision, 16);
        long scheme = (irevision >> 23) & 0x1;
        @SuppressWarnings("unused")
        long ram = (irevision >> 20) & 0x7;
        @SuppressWarnings("unused")
        long manufacturer = (irevision >> 16) & 0xF;
        @SuppressWarnings("unused")
        long processor = (irevision >> 12) & 0xF;
        long model = (irevision >> 4) & 0xFF;
        long pcbrev = irevision & 0xF;

        if (scheme > 0) {
            // a new revision scheme was provided with the release of Raspberry Pi 2
            // if the scheme bit is enabled, then use the new revision numbering scheme
            switch ((int) model) {
                case RPI_MODEL_A:
                    return SystemInfo.BoardType.RaspberryPi_A;
                case RPI_MODEL_A_PLUS:
                    return SystemInfo.BoardType.RaspberryPi_A_Plus;
                case RPI_MODEL_B_PLUS:
                    return SystemInfo.BoardType.RaspberryPi_B_Plus;
                case RPI_MODEL_2B:
                    return SystemInfo.BoardType.RaspberryPi_2B;
                case RPI_MODEL_ALPHA:
                    return SystemInfo.BoardType.RaspberryPi_Alpha;
                case RPI_MODEL_CM:
                    return SystemInfo.BoardType.RaspberryPi_ComputeModule;
                case RPI_MODEL_UNKNOWN:
                    return SystemInfo.BoardType.RaspberryPi_Unknown;
                case RPI_MODEL_3B:
                    return SystemInfo.BoardType.RaspberryPi_3B;
                case RPI_MODEL_ZERO:
                    return SystemInfo.BoardType.RaspberryPi_Zero;
                case RPI_MODEL_CM3:
                    return SystemInfo.BoardType.RaspberryPi_ComputeModule3;
                case RPI_MODEL_ZERO_W:
                    return SystemInfo.BoardType.RaspberryPi_ZeroW;
                case RPI_MODEL_3B_PLUS:
                    return SystemInfo.BoardType.RaspberryPi_3B_Plus;
                case RPI_MODEL_B: {
                    // for model B, also take into consideration the revision
                    if (pcbrev <= 1)
                        return SystemInfo.BoardType.RaspberryPi_B_Rev1;
                    else
                        return SystemInfo.BoardType.RaspberryPi_B_Rev2;
                }
            }
        }

        // prior to the Raspberry Pi 2, the original revision scheme
        // was simply a fixed identifier number
        else if (scheme == 0) {

            // The following info obtained from:
            // http://elinux.org/RPi_HardwareHistory
            // -and-
            // https://github.com/Pi4J/wiringPi/blob/master/wiringPi/wiringPi.c#L808

            // -------------------------------------------------------------------
            // Revision	Release Date	Model	PCB Revision	Memory	Notes
            // -------------------------------------------------------------------
            // Beta	  Q1 2012	B (Beta) ?.?	256 MB	Beta Board
            // 0002	  Q1 2012	B        1.0	256 MB
            // 0003	  Q3 2012	B     	 1.0	256 MB	(ECN0001) Fuses mod and D14 removed
            // 0004	  Q3 2012	B        2.0	256 MB	(Mfg by Sony)
            // 0005	  Q4 2012	B        2.0	256 MB	(Mfg by Qisda)
            // 0006	  Q4 2012	B        2.0	256 MB	(Mfg by Egoman)
            // 0007	  Q1 2013	A        2.0	256 MB	(Mfg by Egoman)
            // 0008	  Q1 2013	A        2.0	256 MB	(Mfg by Sony)
            // 0009	  Q1 2013	A        2.0	256 MB	(Mfg by Qisda)
            // 000d	  Q4 2012	B        2.0	512 MB	(Mfg by Egoman)
            // 000e	  Q4 2012	B        2.0	512 MB	(Mfg by Sony)
            // 000f	  Q4 2012	B        2.0	512 MB	(Mfg by Qisda)
            // 0010	  Q3 2014	B+       1.0	512 MB	(Mfg by Sony)
            // 0011	  Q2 2014	CM	     1.0	512 MB	(Mfg by Sony)
            // 0012	  Q4 2014	A+	     1.0	256 MB	(Mfg by Sony)
            // 0013	  Q1 2015	B+	     1.2	512 MB	 ?
            // 0014   ?? ????   CM       1.0    512 MB	(Mfg by Sony)
            // 0015   ?? ????   A+       1.1    256 MB 	(Mfg by Sony)|
            switch (revision.trim().toLowerCase()) {
                case "Beta":  // Model B Beta
                case "0002":  // Model B Revision 1
                case "0003":  // Model B Revision 1 (Egoman) + Fuses mod and D14 removed
                    return SystemInfo.BoardType.RaspberryPi_B_Rev1;

                case "0004":  // Model B Revision 2 256MB (Sony)
                case "0005":  // Model B Revision 2 256MB (Qisda)
                case "0006":  // Model B Revision 2 256MB (Egoman)
                    return SystemInfo.BoardType.RaspberryPi_B_Rev2;

                case "0007":  // Model A 256MB (Egoman)
                case "0008":  // Model A 256MB (Sony)
                case "0009":  // Model A 256MB (Qisda)
                    return SystemInfo.BoardType.RaspberryPi_A;

                case "000d":  // Model B Revision 2 512MB (Egoman)
                case "000e":  // Model B Revision 2 512MB (Sony)
                case "000f":  // Model B Revision 2 512MB (Egoman)
                    return SystemInfo.BoardType.RaspberryPi_B_Rev2;

                case "0010":  // Model B Plus 512MB (Sony)
                    return SystemInfo.BoardType.RaspberryPi_B_Plus;

                case "0011":  // Compute Module 512MB (Sony)
                    return SystemInfo.BoardType.RaspberryPi_ComputeModule;

                case "0012":  // Model A Plus 512MB (Sony)
                    return SystemInfo.BoardType.RaspberryPi_A_Plus;

                case "0013":  // Model B Plus 512MB (Egoman)
                    return SystemInfo.BoardType.RaspberryPi_B_Plus;

                /* UNDOCUMENTED */
                case "0014":  // Compute Module Rev 1.2, 512MB, (Sony)
                    return SystemInfo.BoardType.RaspberryPi_ComputeModule;

                /* UNDOCUMENTED */
                case "0015":  // Model A Plus 256MB (Sony)
                    return SystemInfo.BoardType.RaspberryPi_A_Plus;

                // unknown
                default:
                    return SystemInfo.BoardType.RaspberryPi_Unknown;
            }
        }

        // unknown board
        return SystemInfo.BoardType.UNKNOWN;
    }

    @Override
    public float getCpuTemperature() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        // CPU temperature is in the form
        // pi@mypi$ /opt/vc/bin/vcgencmd measure_temp
        // temp=42.3'C
        // Support for this was added around firmware version 3357xx per info
        // at http://www.raspberrypi.org/phpBB3/viewtopic.php?p=169909#p169909
        String result[] = ExecUtil.execute("/opt/vc/bin/vcgencmd measure_temp");
        if (result != null && result.length > 0) {
            for (String line : result) {
                String parts[] = line.split("[=']", 3);
                return Float.parseFloat(parts[1]);
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public float getCpuVoltage() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("core");
    }

    @Override
    public float getMemoryVoltageSDRam_C() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("sdram_c");
    }

    @Override
    public float getMemoryVoltageSDRam_I() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("sdram_i");
    }

    @Override
    public float getMemoryVoltageSDRam_P() throws IOException, InterruptedException, NumberFormatException, UnsupportedOperationException {
        return getVoltage("sdram_p");
    }

    @Override
    public boolean getCodecH264Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCodecEnabled("H264");
    }

    @Override
    public boolean getCodecMPG2Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCodecEnabled("MPG2");
    }

    @Override
    public boolean getCodecWVC1Enabled() throws IOException, InterruptedException, UnsupportedOperationException {
        return getCodecEnabled("WVC1");
    }

    @Override
    public long getClockFrequencyArm() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("arm");
    }

    @Override
    public long getClockFrequencyCore() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("core");
    }

    @Override
    public long getClockFrequencyH264() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("h264");
    }

    @Override
    public long getClockFrequencyISP() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("isp");
    }

    @Override
    public long getClockFrequencyV3D() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("v3d");
    }

    @Override
    public long getClockFrequencyUART() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("uart");
    }

    @Override
    public long getClockFrequencyPWM() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("pwm");
    }

    @Override
    public long getClockFrequencyEMMC() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("emmc");
    }

    @Override
    public long getClockFrequencyPixel() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("pixel");
    }

    @Override
    public long getClockFrequencyVEC() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("vec");
    }

    @Override
    public long getClockFrequencyHDMI() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("hdmi");
    }

    @Override
    public long getClockFrequencyDPI() throws IOException, InterruptedException, UnsupportedOperationException {
        return getClockFrequency("dpi");
    }
}
