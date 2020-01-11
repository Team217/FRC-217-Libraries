package org.team217.pid;

import java.time.Clock;

import org.team217.*;

/**
 * A class that applies acceleration to a {@code PID} system.
 * 
 * @author ThunderChickens 217
 * @deprecated Use WPILib's Feedforward and PID
 */
@Deprecated
public class APID {
    private PID pid;
    private double accelTime;
    private boolean isAccel = true;
    private long startTime = 0;
    private double maxSpeed = 1;
	private static final Clock clock = Clock.systemUTC();

	/**
	 * Constructor to make a variable that applies acceleration to a {@code PID} system.
     * 
     * @param pid
     *        The {@code PID} variable to manage
	 * 
	 * @author ThunderChickens 217
	 */
    public APID(PID pid) {
        this.pid = pid;
        initialize();
    }

	/**
	 * Constructor to make a variable that applies acceleration to a {@code PID} system.
     * 
     * @param pid
     *        The {@code PID} variable to manage
     * @param accelTime
     *        The time it should take to accelerate from 0 to +/-1, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public APID(PID pid, double accelTime) {
        this.pid = pid;
        setAccelTime(accelTime);
        initialize();
    }

    /** Creates and returns a copy of this object. */
    @Override
    public APID clone() {
        return new APID(pid, accelTime).setMaxSpeed(maxSpeed);
    }

    /** Returns the {@code PID} variable being managed. */
    public PID getPID() {
        return pid;
    }

    /**
     * Sets the time it should take to accelerate from 0 to {@code maxSpeed}, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @return
     *        This {@code APID} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public APID setAccelTime(double accelTime) {
        if (accelTime < 0) {
            throw new IllegalArgumentException("Illegal accelTime value: " + accelTime + "\nValue cannot be negative");
        }
        this.accelTime = accelTime;

        return this;
    }

    /**
     * Sets the maximum motor speed used for acceleration.
     * 
     * @param maxSpeed
     *        The maximum motor speed
     * @return
     *        This {@code APID} object
     * 
     * @exception IllegalArgumentException if {@code maxSpeed} is not positive
     */
    public APID setMaxSpeed(double maxSpeed) {
        if (maxSpeed <= 0) {
            throw new IllegalArgumentException("Illegal maxSpeed value: " + maxSpeed + "\nValue must be greater than 0");
        }
        this.maxSpeed = maxSpeed;

        return this;
    }

    /**
     * Sets the {@code PID} variable.
     * 
     * @param pid
     *        The new {@code PID} variable
     * @return
     *        This {@code APID} object
     */
    public APID setPID(PID pid) {
        this.pid = pid;
        return this;
    }

    /** (Re)activates the acceleration period and resets the acceleration timer and PID errors. */
    public void initialize() {
        isAccel = true;
        startTime = clock.millis();
        pid.resetErrors();
    }

	/**
	 * Returns the motor output value.
	 * 
	 * @param pos
	 *        The current position
	 * @param tar
	 *        The desired target
	 */
    public double getOutput(double pos, double tar) {
        double output = Num.inRange(pid.getOutput(pos, tar), maxSpeed);
        int sign = Num.sign(output);
        
        // maxSpeed / (1000 * accelTime) = acceleration, acceleration * time = velocity
        double accelOutput = (accelTime == 0) ? output : sign * maxSpeed / (1000 * accelTime) * (clock.millis() - startTime);
        
        if (Math.abs(accelOutput) < Math.abs(output) && isAccel) {
            output = accelOutput;
        }
        else {
            isAccel = false;
        }

        return output;
    }
}