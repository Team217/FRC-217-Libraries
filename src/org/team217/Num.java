package org.team217;

/**
 * Contains operations for managing and checking numerical data.
 * 
 * @author ThunderChickens 217
 */
public class Num {
    /**
     * Checks if a value is within a given inclusive deadband and, if it is, sets the value to 0.
     * 
     * @param value
     *        The value to be tested
     * @param deadband
     *        The deadband size
     * @return
     *        The value if not in the deadband, 0 if in the deadband
     * 
     * @exception IllegalArgumentException if {@code deadband} is negative
     */
    public static double deadband(double value, double deadband) {
        return deadband(value, deadband, true);
    }
    
    /**
     * Checks if a value is within a given deadband and, if it is, sets the value to 0.
     * 
     * @param value
     *        The value to be tested
     * @param deadband
     *        The deadband size
     * @param isInclusive
     *        {@code true} if the deadband is inclusive
     * @return
     *        The value if not in the deadband, 0 if in the deadband
     * 
     * @exception IllegalArgumentException if {@code deadband} is negative
     */
    public static double deadband(double value, double deadband, boolean isInclusive) {
        if (deadband < 0) {
            throw new IllegalArgumentException("Illegal deadband value: " + deadband + "\nValue cannot be negative");
        }
        return isWithinRange(value, deadband, isInclusive) ? 0 : value;
    }
    
    /**
     * Checks if a value is within a two-sided inclusive range.
     * 
     * @param value
     *        The value to be tested
     * @param range
     *        The two-sided range [-range, range]
     * @return
     *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code range} is negative
     */
    public static boolean isWithinRange(double value, double range) {
        return isWithinRange(value, range, true);
    }
    
    /**
     * Checks if a value is within a two-sided range.
     * 
     * @param value
     *        The value to be tested
     * @param range
     *        The two-sided range [-range, range]
     * @param isInclusive
     *        {@code true} if the range is inclusive
     * @return
     *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code range} is negative
     */
    public static boolean isWithinRange(double value, double range, boolean isInclusive) {
        if (range < 0) {
            throw new IllegalArgumentException("Illegal range value: " + range + "\nValue cannot be negative");
        }
        return isWithinRange(value, -range, range, isInclusive);
    }

    /**
     * Checks if a value is within an inclusive range.
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
     * Checks if a value is within a range.
     * 
     * @param value
     *        The value to be tested
     * @param lower
     *        The lower range
     * @param upper
     *        The upper range
     * @param isInclusive
     *        {@code true} if the range is inclusive
     * @return
     *        {@code true} if the value is within the range
     * 
     * @exception IllegalArgumentException if {@code lower} &gt; {@code upper}
     */
    public static boolean isWithinRange(double value, double lower, double upper, boolean isInclusive) {
        if (lower > upper) {
            throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
        }
        return isInclusive ? value >= lower && value <= upper
               : value > lower && value < upper;
    }

    /**
     * Returns a value kept within a two-sided range.
     * 
     * @param value
     *        The value to manage
     * @param range
     *        The two-sided range [-range, range]
     * @return
     *        The value, modified to stay within the range
     * 
     * @exception IllegalArgumentException if {@code range} is negative
     */
    public static double getValueInRange(double value, double range) {
        if (range < 0) {
            throw new IllegalArgumentException("Illegal range value: " + range + "\nValue cannot be negative");
        }
        return getValueInRange(value, -range, range);
    }

    /**
     * Returns a value kept within a range.
     * 
     * @param value
     *        The value to manage
     * @param lower
     *        The lower range
     * @param upper
     *        The upper range
     * @return
     *        The value, modified to stay within the range
     * 
     * @exception IllegalArgumentException if {@code lower} &gt; {@code upper}
     */
    public static double getValueInRange(double value, double lower, double upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
        }
        return value > upper ? upper
               : value < lower ? lower : value;
    }

    /**
     * Checks if a value is within an inclusive range of a target value.
     * 
     * @param value
     *        The value to be tested
     * @param target
     *        The target value
     * @param range
     *        The target range
     * @return
     *        {@code true} if the value is within the range of the target
     * 
     * @exception IllegalArgumentException if {@code range} is negative
     */
    public static boolean isWithinTarget(double value, double target, double range) {
        return isWithinTarget(value, target, range, true);
    }

    /**
     * Checks if a value is within a range of a target value.
     * 
     * @param value
     *        The value to be tested
     * @param target
     *        The target value
     * @param range
     *        The target range
     * @param isInclusive
     *        {@code true} if the range is inclusive
     * @return
     *        {@code true} if the value is within the range of the target
     * 
     * @exception IllegalArgumentException if {@code range} is negative
     */
    public static boolean isWithinTarget(double value, double target, double range, boolean isInclusive) {
        if (range < 0) {
            throw new IllegalArgumentException("Illegal range value: " + range + "\nValue cannot be negative");
        }
        return isWithinRange(value, target - range, target + range, isInclusive);
    }

    /**
     * Returns a value kept within the range of a target value.
     * 
     * @param value
     *        The value to manage
     * @param target
     *        The target value
     * @param range
     *        The target range
     * @return
     *        The value, modified to stay within the range of the target
     */
    public static double getValueInTarget(double value, double target, double range) {
        if (range < 0) {
            throw new IllegalArgumentException("Illegal range value: " + range + "\nValue cannot be negative");
        }
        return getValueInRange(value, target - range, target + range);
    }

    /**
     * Returns a value kept within a circular range [lower, upper).
     * <p>
     * In a circular range, when value goes from upper - 1 to upper, it wraps around
     * back to lower, and vice versa.
     * 
     * @param value
     *        The value to manage
     * @param lower
     *        The lower range
     * @param upper
     *        The upper range
     * @return
     *        The value, modified to stay within the circular range
     */
    public static double getValueInCircularRange(double value, double lower, double upper) {
        if (lower > upper) {
            throw new IllegalArgumentException("Illegal lower/upper value: " + lower + "/" + upper + "\nUpper must be greater than lower");
        }
        value -= lower; // convert circular range to [0, upper - lower) for computation
        value %= (upper - lower); // keeps value within the circular range [0, upper - lower)
        if (value < 0) { // edge case where value was negative prior to the modulus
            value += (upper - lower);
        }
        value += lower; // convert circular range back to [lower, upper)
        return value;
    }

    /**
     * Returns the distance between two points given the change in distance along component axes.
     * 
     * @param axis
     *        The change in distance along an axis; multiple parameters for multiple axes
     */
    public static double distance(double... axis) {
        // d^2 = x_1^2 + x_2^2 + ... + x_n^2
        double axes = 0;
        for (double d : axis) {
            // sum up the squares
            axes += d * d;
        }
        return Math.sqrt(axes);
    }

    /**
     * Returns the missing component axis given the distance between two points and the other component axes.
     * 
     * @param dist
     *        The distance between the two points
     * @param axis
     *        The change in distance along an axis; multiple parameters for multiple axes
     */
    public static double componentAxis(double dist, double... axis) {
        // d^2 = x_1^2 + x_2^2 + ... + x_n^2
        // x_1^2 = d^2 - (x_2^2 + x_3^2 + ... + x_n^2)
        double axes = 0;
        for (double d : axis) {
            // sum up the squares
            axes += d * d;
        }
        return Math.sqrt(dist * dist - axes);
    }
}
