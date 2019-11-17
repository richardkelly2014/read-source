package com.pi4jl.platform;

/**
 * <p>
 * This exception is thrown if a platform assignment is attempted when a
 * platform instance has already been assigned.
 * </p>
 *
 * @see <a href="https://www.pi4j.com/">https://www.pi4j.com/</a>
 * @author Robert Savage (<a
 *         href="http://www.savagehomeautomation.com">http://www.savagehomeautomation.com</a>)
 */
public class PlatformAlreadyAssignedException extends Exception {

	private static final long serialVersionUID = -2812520138732144154L;

	/**
     * Default Constructor
     *
     * @param platform
     */
    public PlatformAlreadyAssignedException(Platform platform){
        super("The Pi4J platform has already been assigned as '" + platform.label() +
              "'; cannot change platforms once a platform assignment has been made.");
    }
}
