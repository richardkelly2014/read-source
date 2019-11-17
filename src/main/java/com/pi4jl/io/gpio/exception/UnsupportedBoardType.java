package com.pi4jl.io.gpio.exception;



/**
 * Unsupported hardware boart model/type.
 *
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
@SuppressWarnings("unused")
public class UnsupportedBoardType extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public UnsupportedBoardType() {
        super("Unsupported/unknown board type; unable to detect hardware.");
    }
}
