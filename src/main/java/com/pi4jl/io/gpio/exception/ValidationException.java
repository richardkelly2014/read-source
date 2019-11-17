package com.pi4jl.io.gpio.exception;

/**
 * @author Christian Wehrli
 */
@SuppressWarnings("unused")
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 4526136277211409422L;

    public ValidationException(String message) {
        super(message);
    }

    public ValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}
