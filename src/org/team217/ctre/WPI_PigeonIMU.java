package org.team217.ctre;

/**
 * Pigeon IMU Class (Extended). Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 * 
 * @author ThunderChickens 217, Cross the Road Electronics
 */
public class WPI_PigeonIMU extends com.ctre.phoenix.sensors.WPI_PigeonIMU {
    protected double pitchOffset = 0;
    protected double rollOffset = 0;

    private double[] ypr = new double[3];
    
    /**
     * Constructor for creating a {@code PigeonIMU} object.
     * 
     * @param deviceNumber
     *        The Device ID of the {@code PigeonIMU}
     */
    public WPI_PigeonIMU(int deviceNumber) {
        super(deviceNumber);
    }

    /** Returns the pitch (front and back tip) angle of the {@code PigeonIMU}. */
    public double getPitch() {
        getYawPitchRoll(ypr);
        return -(ypr[1] - pitchOffset);
    }
    
    /** Returns the roll (left and right tip) angle of the {@code PigeonIMU}. */
    public double getRoll() {
        getYawPitchRoll(ypr);
        return ypr[2] - rollOffset;
    }

    /**
     * Sets the pitch (front and back tip) angle of the {@code PigeonIMU}.
     * 
     * @param angleDeg
     *        The new angle in degrees
     */
    public void setPitch(double angleDeg) {
        getYawPitchRoll(ypr);
        pitchOffset = ypr[1] + angleDeg; // normally returns -ypr[1], so + angleDeg instead of -
    }
    
    /**
     * Sets the roll (left and right tip) angle of the {@code PigeonIMU}.
     * 
     * @param angleDeg
     *        The new angle in degrees
     */
    public void setRoll(double angleDeg) {
        getYawPitchRoll(ypr);
        rollOffset = ypr[2] - angleDeg;
    }

    /** Resets the pitch (front and back tip) angle of the {@code PigeonIMU} to 0. */
    public void resetPitch() {
        setPitch(0);
    }

    /** Resets the roll (left and right tip) angle of the {@code PigeonIMU} to 0. */
    public void resetRoll() {
        setRoll(0);
    }
}