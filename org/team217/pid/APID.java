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
        double accelOutput = sign * maxSpeed * (clock.millis() - startTime) / (1000 * accelTime);
        
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

    /** Sets the time it should take to accelerate from 0.0 to maxSpeed, in seconds. */
    public void setAccelTime(double accelTime) {
        if (accelTime < 0.0) {
            throw new IllegalArgumentException("Illegal accelTime Value: " + accelTime + "\nValue cannot be negative");
        }
        this.accelTime = accelTime;
    }

    /** Sets the maximum motor speed used for acceleration. */
    public void setMaxSpeed(double maxSpeed) {
        if (maxSpeed <= 0.0) {
            throw new IllegalArgumentException("Illegal maxSpeed Value: " + maxSpeed + "\nValue must be greater than 0");
        }
        this.maxSpeed = maxSpeed;
    }
}