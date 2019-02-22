package org.team217.wpi;

import edu.wpi.first.wpilibj.AnalogInput;

/**
 * AnalogGyro Class (Extended). This class is for gyro sensors that connect to an analog input.
 * 
 * @author ThunderChickens 217, WPILib
 */
public class AnalogGyro extends edu.wpi.first.wpilibj.AnalogGyro {
    protected int offset = 0;

    /**
     * Gyro constructor using the channel number.
     * 
     * @param channel
     *        The analog channel the gyro is connected to.
     *        Gyros can only be used on board channels 0-1
     * 
     * @author ThunderChickens 217, WPILib
     */
    public AnalogGyro(int channel) {
        super(channel);
    }

    /**
     * Gyro constructor with a precreated analog channel object.
     * Use this constructor when the analog channel needs to be shared.
     * 
     * @param channel
     *        The AnalogInput object that the gyro is connected to.
     *        Gyros can only be used on board channels 0-1
     * 
     * @author ThunderChickens 217, WPILib
     */
    public AnalogGyro(AnalogInput channel) {
        super(channel);
    }

    /** Return the actual angle in degrees that the robot is currently facing. */
    @Override
    public double getAngle() {
        return getAngle() + offset;
    }

    /**
     * Sets the gyro to the given angle.
     * 
     * @param offset
     *        The offset angle to set as the current gyro angle
     */
    public void set(int offset) {
        reset();
        this.offset = offset;
    }
}