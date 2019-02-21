package org.team217.pid;

import java.time.Clock;

/**
 * A class that applies acceleration to a PID system.
 * 
 * @author ThunderChickens 217
 */
public class APID {
    PID pid;
    double accelTime;
    boolean isAccel = true;
    long startTime = 0;
    double maxSpeed = 1.0;
	private static final Clock clock = Clock.systemUTC();

	/**
	 * Constructor to make a variable that contains the PID variable and the acceleration rate.
     * 
     * @param pid
     *          The PID variable to manage
     * @param accelTime
     *          The time it should take to accelerate from 0.0 to +/-1.0, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public APID(PID pid, double accelTime) {
        this.pid = pid;
        setAccelTime(accelTime);
        startTime = clock.millis();
    }

	/**
	 * Constructor to make a variable that contains the PID variable and the acceleration rate.
     * 
     * @param pid
     *          The PID variable to manage
     * @param accelTime
     *          The time it should take to accelerate from 0 to maxSpeed, in seconds
     * @param maxSpeed
     *          The maximum motor speed
	 * 
	 * @author ThunderChickens 217
	 */
    public APID(PID pid, double accelTime, double maxSpeed) {
        this(pid, accelTime);
        setMaxSpeed(maxSpeed);
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
        double output = pid.getOutput(pos, tar);
        int sign = (output < 0.0) ? -1 : 1;
        
        // maxSpeed / (1000 * accelTime) = acceleration, acceleration * time = velocity
        double accelOutput = sign * maxSpeed / (1000 * accelTime) * (clock.millis() - startTime);
        
        if (accelOutput < output && isAccel) {
            output = accelOutput;
        }
        else {
            isAccel = false;
        }

        return output;
    }

    /** Returns the PID variable being managed. */
    public PID getPID() {
        return pid;
    }

    /** (Re)activates the acceleration period and resets the acceleration timer. */
    public void initialize() {
        isAccel = true;
        startTime = clock.millis();
    }

    /**
     * Sets the time it should take to accelerate from 0.0 to maxSpeed, in seconds.
     * 
     * @param accelTime
     *               The acceleration time
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public void setAccelTime(double accelTime) {
        if (accelTime < 0.0) {
            throw new IllegalArgumentException("Illegal accelTime Value: " + accelTime + "\nValue cannot be negative");
        }
        this.accelTime = accelTime;
    }

    /**
     * Returns an APID object with the time it should take to accelerate from 0.0 to maxSpeed, in seconds.
     * 
     * @param accelTime
     *               The acceleration time
     * @param modifyOrig
     *                {@code true} if the original APID object should be modified as well
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public APID setAccelTime(double accelTime, boolean modifyOrig) {
        if (modifyOrig) {
            setAccelTime(accelTime);
            return this;
        }
        else {
            APID apid = this;
            apid.setAccelTime(accelTime);
            return apid;
        }
    }

    /**
     * Sets the maximum motor speed used for acceleration.
     * 
     * @param maxSpeed
     *              The maximum motor speed
     * 
     * @exception IllegalArgumentException if {@code maxSpeed} is not positive
     */
    public void setMaxSpeed(double maxSpeed) {
        if (maxSpeed <= 0.0) {
            throw new IllegalArgumentException("Illegal maxSpeed Value: " + maxSpeed + "\nValue must be greater than 0");
        }
        this.maxSpeed = maxSpeed;
    }

    /**
     * Returns an APID object with the maximum motor speed used for acceleration.
     * 
     * @param maxSpeed
     *              The maximum motor speed
     * @param modifyOrig
     *              {@code true} if the original APID object should be modified as well
     * 
     * @exception IllegalArgumentException if {@code maxSpeed} is not positive
     */
    public APID setMaxSpeed(double maxSpeed, boolean modifyOrig) {
        if (modifyOrig) {
            setMaxSpeed(maxSpeed);
            return this;
        }
        else {
            APID apid = this;
            apid.setMaxSpeed(maxSpeed);
            return apid;
        }
    }
}