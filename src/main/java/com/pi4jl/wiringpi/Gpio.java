package com.pi4jl.wiringpi;

import com.pi4jl.util.NativeLibraryLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class Gpio {

    private Gpio() {
        // forbid object construction
    }

    public static final int NUM_PINS = 46;
    public static final int INPUT = 0;
    public static final int OUTPUT = 1;
    public static final int PWM_OUTPUT = 2;
    public static final int GPIO_CLOCK = 3;

    public static final int LOW = 0;
    public static final int HIGH = 1;

    public static final int PUD_OFF = 0;
    public static final int PUD_DOWN = 1;
    public static final int PUD_UP = 2;

    public static final int PWM_MODE_BAL = 1;
    public static final int PWM_MODE_MS = 0;

    public static final int ALT0 = 4;
    public static final int ALT1 = 5;
    public static final int ALT2 = 6;
    public static final int ALT3 = 7;
    public static final int ALT4 = 3;
    public static final int ALT5 = 2;

    public static final int INT_EDGE_SETUP = 0;
    public static final int INT_EDGE_FALLING = 1;
    public static final int INT_EDGE_RISING = 2;
    public static final int INT_EDGE_BOTH = 3;

    static {
        // Load the platform library
        NativeLibraryLoader.load("libpi4j.so");
    }

    /**
     * 程序使用的是wiringPi 引脚编号表
     *
     * @return
     */
    public static native int wiringPiSetup();

    /**
     * 物理 引脚编号表
     *
     * @return
     */
    public static native int wiringPiSetupPhys();

    public static native int wiringPiSetupSys();

    /**
     * 程序中使用的是BCM GPIO 引脚编号表
     *
     * @return
     */
    public static native int wiringPiSetupGpio();

    /**
     * 针脚 model
     *
     * @param pin
     * @param mode INPUT、OUTPUT、PWM_OUTPUT，GPIO_CLOCK
     */
    public static native void pinMode(int pin, int mode);

    /**
     * <li>INPUT</li>
     * <li>OUTPUT</li>
     * <li>ALT0</li>
     * <li>ALT1</li>
     * <li>ALT2</li>
     * <li>ALT3</li>
     * <li>ALT4</li>
     * <li>ALT5</li>
     *
     * @param pin
     * @param mode
     */
    public static native void pinModeAlt(int pin, int mode);

    /**
     * <li>PUD_OFF</li>
     * <li>PUD_DOWN</li>
     * <li>PUD_UP</li>
     *
     * @param pin
     * @param pud
     */
    public static native void pullUpDnControl(int pin, int pud);

    /**
     * 置为输出模式的 引脚  输出指定的电平信号
     *
     * @param pin
     * @param value HIGH，LOW分别代表高低电平
     */
    public static native void digitalWrite(int pin, int value);

    public static void digitalWrite(int pin, boolean value) {
        digitalWrite(pin, (value) ? 1 : 0);
    }

    /**
     * 输出一个值到PWM寄存器，控制PWM输出
     * pin只能是wiringPi 引脚编号下的1脚（BCM下的18脚）
     *
     * @param pin
     * @param value
     */
    public static native void pwmWrite(int pin, int value);

    /**
     * 读取一个引脚的电平值  LOW  HIGH
     *
     * @param pin
     * @return
     */
    public static native int digitalRead(int pin);

    public static native int analogRead(int pin);

    public static native void analogWrite(int pin, int value);

    public static native void delay(long howLong);

    public static native long millis();

    public static native long micros();

    public static native void delayMicroseconds(long howLong);

    public static native int piHiPri(int priority);

    public static native int waitForInterrupt(int pin, int timeout);

    //注册 回调
    public static int wiringPiISR(int pin, int edgeType, GpioInterruptCallback callback) {
        // if there is not collection in the array at this pin location, then initialize an array list
        if (isrCallbacks[pin] == null) {
            isrCallbacks[pin] = new ArrayList<>();
        }

        // add provided callback interface to ISR callbacks collection
        isrCallbacks[pin].add(callback);
        return _wiringPiISR(pin, edgeType);
    }

    private static List<GpioInterruptCallback> isrCallbacks[] = new List[NUM_PINS];

    private static native int _wiringPiISR(int pin, int edgeType);

    public static void wiringPiClearISR(int pin) {
        // ensure there is a callbacks at this pin location
        if (isrCallbacks[pin] != null) {
            // remove all ISR callbacks
            isrCallbacks[pin].clear();
        }
    }

    private static void isrCallback(int pin) {
        // dispatch callback to the subscribers for this pin
        List<GpioInterruptCallback> callbacks = isrCallbacks[pin];

        // ensure callbacks collection exists for this pins number
        if (callbacks != null && !callbacks.isEmpty()) {
            // iterate over each callback delegate and invoke the callback if the pin and edge type match
            for (GpioInterruptCallback callback : callbacks) {
                callback.callback(pin);
            }
        }
    }

    //板子编号
    public static native int piBoardRev();

    //wri to gpio 针脚
    public static native int wpiPinToGpio(int wpiPin);

    public static native int physPinToGpio(int physPin);

    public static native void digitalWriteByte(int value);

    /**
     * 设置PWM的运行模式
     *
     * @param mode
     */
    public static native void pwmSetMode(int mode);

    public static native void pwmSetRange(int range);

    public static native void pwmSetClock(int divisor);

    public static native void setPadDrive(int group, int value);

    public static native int getAlt(int pin);

    public static native void gpioClockSet(int pin, int frequency);

}
