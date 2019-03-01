package org.team217.pid;

import java.time.Clock;

import org.team217.Range;

/**
 * A class that applies acceleration to a {@code PID} system.
 * 
 * @author ThunderChickens 217
 */
public class APID {
    private PID pid;
    private double accelTime;
    private boolean isAccel = true;
    private long startTime = 0;
    private double maxSpeed = 1.0;
	private static final Clock clock = Clock.systemUTC();

	/**
	 * Constructor to make a variable that contains the {@code PID} variable and the acceleration rate.
     * 
     * @param pid
     *        The {@code PID} variable to manage
     * @param accelTime
     *        The time it should take to accelerate from 0.0 to +/-1.0, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public APID(PID pid, double accelTime) {
        this.pid = pid;
        setAccelTime(accelTime);
        initialize();
    }

	/**
	 * Constructor to make a variable that contains the {@code PID} variable and the acceleration rate.
     * 
     * @param pid
     *        The {@code PID} variable to manage
     * @param accelTime
     *        The time it should take to accelerate from 0 to {@code maxSpeed}, in seconds
     * @param maxSpeed
     *        The maximum motor speed
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
        int sign = Range.sign(output);
        
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

    /** Returns the {@code PID} variable being managed. */
    public PID getPID() {
        return pid;
    }

    /** (Re)activates the acceleration period and resets the acceleration timer. */
    public void initialize() {
        isAccel = true;
        startTime = clock.millis();
    }

    /**
     * Sets the time it should take to accelerate from 0.0 to {@code maxSpeed}, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @return
     *        This {@code APID} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public APID setAccelTime(double accelTime) {
        if (accelTime < 0.0) {
            throw new IllegalArgumentException("Illegal accelTime Value: " + accelTime + "\nValue cannot be negative");
        }
        this.accelTime = accelTime;

        return this;
    }

    /**
     * Sets the time it should take to accelerate from 0.0 to {@code maxSpeed}, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @param modifyOrig
     *        {@code true} [default] if the original {@code APID} object should be modified as well
     * @return
     *        The resulting {@code APID} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public APID setAccelTime(double accelTime, boolean modifyOrig) {
        if (modifyOrig) {
            return setAccelTime(accelTime);
        }
        APID apid = this;
        return apid.setAccelTime(accelTime);
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
        if (maxSpeed <= 0.0) {
            throw new IllegalArgumentException("Illegal maxSpeed Value: " + maxSpeed + "\nValue must be greater than 0");
        }
        this.maxSpeed = maxSpeed;

        return this;
    }

    /**
     * Sets the maximum motor speed used for acceleration.
     * 
     * @param maxSpeed
     *        The maximum motor speed
     * @param modifyOrig
     *        {@code true} [default] if the original {@code APID} object should be modified as well
     * @return
     *        The resulting {@code APID} object
     * 
     * @exception IllegalArgumentException if {@code maxSpeed} is not positive
     */
    public APID setMaxSpeed(double maxSpeed, boolean modifyOrig) {
        if (modifyOrig) {
            return setMaxSpeed(maxSpeed);
        }
        APID apid = this;
        return apid.setMaxSpeed(maxSpeed);
    }
}