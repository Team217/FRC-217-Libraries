package org.team217;

/**
 * Contains deadband functions for controllers and motors
 * 
 * @author ThunderChickens 217
 */
public class Range {
    
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
	public static boolean isWithinRange(double value, double lower, double upper) {
		if (lower > upper) {
			throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
		}
		if (value >= lower && value <= upper) {
			return true;
		}
		return false;
	}

	/**
	 * Keeps the value within a given range.
	 * 
	 * @param value
	 *        The tested value
	 * @param lower
	 *        The lower range
	 * @param upper
	 *        The upper range
	 * @return
	 *        The value, modified to stay within the range
	 */
	public static double inRange(double value, double lower, double upper) {
		if (lower > upper) {
			throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
		}
		if (value > upper) {
			value = upper;
		}
		else if (value < lower) {
			value = lower;
		}

		return value;
	}
}