package org.team217;

/**
 * Converts between encoder ticks, angles, and distances.
 * 
 * @author ThunderChickens
 */
public class Converter {

    /**
     * Converts radians to encoder ticks
     * 
     * @param rad
     *        Angle in radians
     * @param encPerRot
     *        Number of encoder ticks per rotation
     * @return
     *        Number of encoder ticks to complete the rotation
     */
    public static double radToEnc(double rad, double encPerRot) {
        return rad / (2 * Math.PI) * encPerRot; // Take angle in radians, convert to revolutions, convert to encoder ticks
    }

    /**
     * Converts encoder ticks to radians
     * 
     * @param enc
     *        Encoder ticks
     * @param encPerRot
     *        Number of encoder ticks per rotation
     * @return
     *        Angle in radians
     */
    public static double encToRad(double enc, double encPerRot) {
        return enc / encPerRot * (2 * Math.PI); // Take encoder ticks, convert to revolutions, convert to radians
    }

    /**
     * Converts degrees to encoder ticks
     * 
     * @param deg
     *        Angle in degrees
     * @param encPerRot
     *        Number of encoder ticks per rotation
     * @return
     *        Number of encoder ticks to complete the rotation
     */
    public static double degToEnc(double deg, double encPerRot) {
        return deg / 360 * encPerRot; // Take angle in degrees, convert to revolutions, convert to encoder ticks
    }

    /**
     * Converts encoder ticks to degrees
     * 
     * @param enc
     *        Encoder ticks
     * @param encPerRot
     *        Number of encoder ticks per rotation
     * @return
     *        Angle in degrees
     */
    public static double encToDeg(double enc, double encPerRot) {
        return enc / encPerRot * 360; // Take encoder ticks, convert to revolutions, convert to degrees
    }
    
    /**
     * Converts inches to encoder ticks.
     * 
     * @param inches
     *        Inches to convert
     * @param diameter
     *        Size of diameter in inches
     * @param ticksPerRev
     *        Number of encoder ticks per one revolution
     * @return
     *        Number of encoder ticks to move the distance
     */
    public static double inchToEnc(double inches, double diameter, double ticksPerRev) {
        return inches / (Math.PI * diameter) * ticksPerRev; // Take inches, convert to revolutions, convert to encoder ticks
    }
    
    /**
     * Converts encoder ticks to inches.
     * 
     * @param encTicks
     *        Encoder Ticks to convert
     * @param diameter
     *        Size of diameter in inches
     * @param ticksPerRev
     *        Number of encoder ticks per one revolution
     * @return
     *        Distance in inches
     */
    public static double encToInch(double encTicks, double diameter, double ticksPerRev) {
        return encTicks / ticksPerRev * Math.PI * diameter; // Take encoder ticks, convert to revolutions, convert to inches
    }
}