package com.pi4jl.temperature;

/**
 * Created by jiangfei on 2019/11/16.
 */
public class TemperatureConversion {

    public static final double ABSOLUTE_ZERO_CELSIUS = -273.15;
    public static final double ABSOLUTE_ZERO_FARENHEIT = -459.67;
    public static final double ABSOLUTE_ZERO_KELVIN = 0;
    public static final double ABSOLUTE_ZERO_RANKINE = 0;

    public static double convert(TemperatureScale from, TemperatureScale to, double temperature) {

        switch(from) {

            case FARENHEIT:
                return convertFromFarenheit(to, temperature);
            case CELSIUS:
                return convertFromCelsius(to, temperature);
            case KELVIN:
                return convertFromKelvin(to, temperature);
            case RANKINE:
                return convertFromRankine(to, temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertFromFarenheit (TemperatureScale to, double temperature) {

        switch(to) {

            case FARENHEIT:
                return temperature;
            case CELSIUS:
                return convertFarenheitToCelsius(temperature);
            case KELVIN:
                return convertFarenheitToKelvin(temperature);
            case RANKINE:
                return convertFarenheitToRankine(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertToFarenheit (TemperatureScale from, double temperature) {

        switch(from) {

            case FARENHEIT:
                return temperature;
            case CELSIUS:
                return convertCelsiusToFarenheit(temperature);
            case KELVIN:
                return convertKelvinToFarenheit(temperature);
            case RANKINE:
                return convertRankineToFarenheit(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertFromCelsius(TemperatureScale to, double temperature) {

        switch(to) {

            case FARENHEIT:
                return convertCelsiusToFarenheit(temperature);
            case CELSIUS:
                return temperature;
            case KELVIN:
                return convertCelsiusToKelvin(temperature);
            case RANKINE:
                return convertCelsiusToRankine(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertToCelsius (TemperatureScale from, double temperature) {

        switch(from) {

            case FARENHEIT:
                return convertFarenheitToCelsius(temperature);
            case CELSIUS:
                return temperature;
            case KELVIN:
                return convertKelvinToCelsius(temperature);
            case RANKINE:
                return convertRankineToCelsius(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertFromKelvin(TemperatureScale to, double temperature) {

        switch(to) {

            case FARENHEIT:
                return convertKelvinToFarenheit(temperature);
            case CELSIUS:
                return convertKelvinToCelsius(temperature);
            case KELVIN:
                return temperature;
            case RANKINE:
                return convertKelvinToRankine(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertToKelvin(TemperatureScale from, double temperature) {

        switch(from) {

            case FARENHEIT:
                return convertFarenheitToKelvin(temperature);
            case CELSIUS:
                return convertCelsiusToKelvin(temperature);
            case KELVIN:
                return temperature;
            case RANKINE:
                return convertRankineToKelvin(temperature);
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertFromRankine(TemperatureScale to, double temperature) {

        switch(to) {

            case FARENHEIT:
                return convertRankineToFarenheit(temperature);
            case CELSIUS:
                return convertRankineToCelsius(temperature);
            case KELVIN:
                return convertRankineToKelvin(temperature);
            case RANKINE:
                return temperature;
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertToRankine(TemperatureScale from, double temperature) {

        switch(from) {

            case FARENHEIT:
                return convertFarenheitToRankine(temperature);
            case CELSIUS:
                return convertCelsiusToRankine(temperature);
            case KELVIN:
                return convertKelvinToRankine(temperature);
            case RANKINE:
                return temperature;
            default:
                throw(new RuntimeException("Invalid termpature conversion"));
        }
    }

    public static double convertFarenheitToCelsius(double temperature) {
        return ((temperature - 32) * 5/9);
    }

    public static double convertFarenheitToKelvin(double temperature) {
        return (((temperature + 459.67) * 5) / 9);
    }

    public static double convertFarenheitToRankine(double temperature) {
        return temperature + 459.67;
    }

    public static double convertCelsiusToFarenheit(double temperature) {
        return (((temperature * 9) / 5) + 32);
    }

    public static double convertCelsiusToKelvin(double temperature) {
        return (temperature - ABSOLUTE_ZERO_CELSIUS);
    }

    public static double convertCelsiusToRankine(double temperature) {
        return (((temperature-ABSOLUTE_ZERO_CELSIUS) * 9) / 5);
    }

    public static double convertKelvinToCelsius(double temperature) {
        return (temperature + ABSOLUTE_ZERO_CELSIUS);
    }

    public static double convertKelvinToFarenheit(double temperature) {
        return (((temperature * 9) / 5) - 459.67);
    }

    public static double convertKelvinToRankine(double temperature) {
        return ((temperature * 9) / 5);
    }

    public static double convertRankineToFarenheit(double temperature) {
        return (temperature-(459.67));
    }

    public static double convertRankineToCelsius(double temperature) {
        return (((temperature-491.67)* 5) / 9);
    }

    public static double convertRankineToKelvin(double temperature) {
        return ((temperature * 5) / 9);
    }

}
