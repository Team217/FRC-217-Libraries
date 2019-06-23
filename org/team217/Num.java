package org.team217;

import java.math.*;

/**
 * Contains operations for managing and checking numerical data.
 * 
 * @author ThunderChickens 217
 */
public class Num {
    /**
	 * Checks if the value is within a given deadband and, if it is, sets the value to 0.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param deadband
	 *        The deadband size
     * @param isInclusive
     *        {@code true} [default] if the deadband is inclusive
	 * @return
	 *        The value if not in the deadband, 0 if in the deadband
	 * 
	 * @author ThunderChickens 217
	 */
	public static double deadband(double value, double deadband, boolean isInclusive) {
        if (deadband < 0) {
			throw new IllegalArgumentException("Illegal deadband value: " + deadband + "\nValue cannot be negative");
        }
		return isInclusive ? (Math.abs(value) <= deadband ? 0 : value) : (Math.abs(value) < deadband ? 0 : value);
	}
    
    /**
	 * Checks if the value is within a given inclusive deadband and, if it is, sets the value to 0.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param deadband
	 *        The deadband size
	 * @return
	 *        The value if not in the deadband, 0 if in the deadband
	 * 
	 * @author ThunderChickens 217
	 */
	public static double deadband(double value, double deadband) {
		return deadband(value, deadband, true);
	}

	/**
	 * Checks if the value is within a given range.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param lower
	 *        The lower range
	 * @param upper
	 *        The upper range
     * @param isInclusive
     *        {@code true} [default] if the range is inclusive
	 * @return
	 *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code lower} &gt; {@code upper}
	 */
	public static boolean isWithinRange(double value, double lower, double upper, boolean isInclusive) {
		if (lower > upper) {
			throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
        }
        return isInclusive ? value >= lower && value <= upper : value > lower && value < upper;
    }
    

	/**
	 * Checks if the value is within a given inclusive range.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param lower
	 *        The lower range
	 * @param upper
	 *        The upper range
	 * @return
	 *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code lower} &gt; {@code upper}
	 */
    public static boolean isWithinRange(double value, double lower, double upper) {
        return isWithinRange(value, lower, upper, true);
    }
    
	/**
	 * Checks if the value is within a given two-sided range.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param range
     *        The two-sided range [-range, range]
     * @param isInclusive
     *        {@code true} [default] if the range is inclusive
	 * @return
	 *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code range} is not positive
	 */
    public static boolean isWithinRange(double value, double range, boolean isInclusive) {
		if (range <= 0) {
			throw new IllegalArgumentException("Illegal range value: " + range + "\nValue must be positive");
		}
        return isWithinRange(value, -range, range, isInclusive);
    }
    
	/**
	 * Checks if the value is within a given two-sided, inclusive range.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param range
     *        The two-sided range [-range, range]
	 * @return
	 *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code range} is not positive
	 */
    public static boolean isWithinRange(double value, double range) {
        return isWithinRange(value, range, true);
    }

	/**
	 * Keeps the value within a given range.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param lower
	 *        The lower range
	 * @param upper
	 *        The upper range
	 * @return
	 *        The value, modified to stay within the range
     * 
     * @exception IllegalArgumentException if {@code lower} &gt; {@code upper}
	 */
	public static double inRange(double value, double lower, double upper) {
		if (lower > upper) {
			throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
		}
		return value > upper ? upper : value < lower ? lower : value;
    }

	/**
	 * Keeps the value within a given two-sided range.
	 * 
	 * @param value
	 *        The value to be tested
	 * @param range
	 *        The two-sided range [-range, range]
	 * @return
	 *        The value, modified to stay within the range
     * 
     * @exception IllegalArgumentException if {@code range} is not positive
	 */
	public static double inRange(double value, double range) {
		if (range <= 0) {
			throw new IllegalArgumentException("Illegal range value: " + range + "\nValue must be positive");
		}
		return inRange(value, -range, range);
    }

    /**
     * Returns the rounded value.
     * <p>
     * Note: half values (0.5) round to the nearest even.
     * </p>
     * 
     * @param value
     *        The value to be rounded
     * @param places
     *        The number of decimal places
     */
    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException("Illegal decimal places value: " + places + "\nValue cannot be negative");
        }
        return new BigDecimal(String.valueOf(value)).setScale(places, RoundingMode.HALF_EVEN).doubleValue();
    }
    
    /** Returns the sign (positivity) of the value. Signs are 1, 0, and -1. */
    public static int sign(double value) {
        return value > 0 ? 1 : value < 0 ? -1 : 0;
    }

    /**
     * Returns the distance between two points given the change in distance along component axes.
     * 
     * @param dist
     *        The change in distance along an axis; multiple parameters for multiple axes
     */
    public static double distance(double... dist) {
        double distance = 0;
        for (double d : dist) {
            distance += Math.pow(d, 2);
        }
        return Math.sqrt(distance);
    }
}