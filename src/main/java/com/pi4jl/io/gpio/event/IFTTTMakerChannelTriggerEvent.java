package com.pi4jl.io.gpio.event;

import com.pi4jl.io.gpio.GpioPin;
import com.pi4jl.io.gpio.PinState;
import com.pi4jl.util.StringUtil;

import java.util.EventObject;

/**
 * Created by jiangfei on 2019/11/17.
 */
public class IFTTTMakerChannelTriggerEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    protected final GpioPin pin;
    protected final PinState state;
    private final String eventName;
    private String value1 = StringUtil.EMPTY;
    private String value2 = StringUtil.EMPTY;
    private String value3 = StringUtil.EMPTY;

    public IFTTTMakerChannelTriggerEvent(Object obj, GpioPin pin, PinState state, String eventName, String value1, String value2, String value3) {
        super(obj);
        this.pin = pin;
        this.state = state;
        this.eventName = eventName;
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
    }

    /**
     * Get the pin number that changed and raised this event.
     *
     * @return GPIO pin number (not header pin number; not wiringPi pin number)
     */
    public GpioPin getPin() {
        return this.pin;
    }

    /**
     * Get the pin state that activated this trigger.
     *
     * @return GPIO pin state
     */
    public PinState getState() { return this.state; }

    /**
     * Get the IFTTT event name configured for this trigger.
     *
     * @return IFTTT event name
     */
    public String getEventName() { return this.eventName; }

    /**
     * Get the IFTTT value1 data for this triggered event.
     * By default, this is the GPIO pin name.
     * The consumer can optionally override this value using the 'setValue1()' method.
     *
     * @return IFTTT value1 data
     */
    public String getValue1() { return this.value1; }

    /**
     * Set (override) the value1 data that will be sent to the IFTTT trigger event.
     *
     * @param data new value data/string
     */
    public void setValue1(String data) { this.value1 = data; }

    /**
     * Get the IFTTT value2 data for this triggered event.
     * By default, this is the GPIO state value (integer).
     * The consumer can optionally override this value using the 'setValue2()' method.
     *
     * @return IFTTT value2 data
     */
    public String getValue2() { return this.value2; }

    /**
     * Set (override) the value2 data that will be sent to the IFTTT trigger event.
     *
     * @param data new value data/string
     */
    public void setValue2(String data) { this.value2 = data; }

    /**
     * Get the IFTTT value2 data for this triggered event.
     * By default, this is a JSON string of data including all details about the GPIO pin and PinState.
     * The consumer can optionally override this value using the 'setValue3()' method.
     *
     * @return IFTTT value2 data
     */
    public String getValue3() { return this.value3; }

    /**
     * Set (override) the value3 data that will be sent to the IFTTT trigger event.
     *
     * @param data new value data/string
     */
    public void setValue3(String data) { this.value3 = data; }
}
