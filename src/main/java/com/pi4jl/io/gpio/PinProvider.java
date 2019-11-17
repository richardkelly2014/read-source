package com.pi4jl.io.gpio;

import com.pi4jl.io.gpio.impl.PinImpl;

import java.util.*;

/**
 * Created by jiangfei on 2019/11/17.
 */
public abstract class PinProvider {

    protected static final Map<String, Pin> pins = new HashMap<>();

    protected static Pin createDigitalPin(String providerName, int address, String name) {
        return createDigitalPin(providerName, address, name, EnumSet.allOf(PinEdge.class));
    }

    protected static Pin createDigitalPin(String providerName, int address, String name, EnumSet<PinPullResistance> resistance, EnumSet<PinEdge> edges) {
        return createPin(providerName, address, name,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.SOFT_PWM_OUTPUT),
                resistance,
                edges);
    }

    protected static Pin createDigitalPin(String providerName, int address, String name, EnumSet<PinEdge> edges) {
        return createPin(providerName, address, name,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.SOFT_PWM_OUTPUT),
                PinPullResistance.all(),
                edges);
    }

    protected static Pin createDigitalAndPwmPin(String providerName, int address, String name, EnumSet<PinEdge> edges) {
        return createPin(providerName, address, name,
                EnumSet.of(PinMode.DIGITAL_INPUT, PinMode.DIGITAL_OUTPUT, PinMode.PWM_OUTPUT),
                PinPullResistance.all(),
                edges);
    }

    protected static Pin createDigitalAndPwmPin(String providerName, int address, String name) {
        return createDigitalAndPwmPin(providerName, address, name, EnumSet.allOf(PinEdge.class));
    }

    protected static Pin createAnalogInputPin(String providerName, int address, String name) {
        return createPin(providerName, address, name, EnumSet.of(PinMode.ANALOG_INPUT));
    }

    protected static Pin createPin(String providerName, int address, String name, EnumSet<PinMode> modes) {
        Pin pin = new PinImpl(providerName, address, name, modes);
        pins.put(name, pin);
        return pin;
    }

    protected static Pin createPin(String providerName, int address, String name, EnumSet<PinMode> modes,
                                   EnumSet<PinPullResistance> resistance, EnumSet<PinEdge> edges) {
        Pin pin = new PinImpl(providerName, address, name, modes, resistance, edges);
        pins.put(name, pin);
        return pin;
    }

    public static Pin getPinByName(String name) {
        return pins.get(name);
    }

    public static Pin getPinByAddress(int address) {
        for (Pin pin : pins.values()) {
            if (pin.getAddress() == address) {
                return pin;
            }
        }
        return null;
    }

    /**
     * Get all pin instances from this provider.
     *
     * @return all pin instances support by this provider
     */
    public static Pin[] allPins() {
        return pins.values().toArray(new Pin[0]);
    }

    /**
     * Get all pin instances from this provider that support one of the provided pin modes.
     *
     * @param mode one or more pin modes that you wish to include in the result set
     * @return pin instances that support the provided pin modes
     */
    public static Pin[] allPins(PinMode... mode) {
        List<Pin> results = new ArrayList<>();
        for (Pin p : pins.values()) {
            EnumSet<PinMode> supported_modes = p.getSupportedPinModes();
            for (PinMode m : mode) {
                if (supported_modes.contains(m)) {
                    results.add(p);
                    continue;
                }
            }
        }
        return results.toArray(new Pin[0]);
    }
}
