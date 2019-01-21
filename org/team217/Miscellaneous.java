package org.team217;

/**
 * Contains deadband functions for controllers and motors
 * 
 * @author ThunderChickens 217
 */
public class Miscellaneous {
    
    /**
	 * Checks if the value is in a given deadband and, if it is, sets the value to 0.
	 * 
	 * @param value
	 *        The tested value
	 * @param deadband
	 *        The deadband size
	 * @return
	 *        The value if not in the deadband, 0.0 if in the deadband
	 * 
	 * @author ThunderChickens 217
	 */
	public static double deadband(double value, double deadband) {
		if (Math.abs(value) <= Math.abs(deadband)) {
			value = 0.0;
		}
		
		return value;
	}

	/**
	 * Checks if the value is within a given range
	 * 
	 * @param value
	 *           The tested value
	 * @param lower
	 *           The lower range
	 * @param upper
	 *           The upper range
	 * @return
	 *           {@code true} if the value is within the range
	 */
	public static boolean withinRange(double value, double lower, double upper) {
		if (value >= lower && value <= upper) {
			return true;
		}
		return false;
	}
}