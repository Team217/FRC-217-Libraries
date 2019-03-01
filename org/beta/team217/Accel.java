package org.beta.team217;

import java.time.Clock;

import org.team217.*;

public class Accel {
    double accelTime, decelTime;
    boolean isAccel = true, isDecel = false;
    long startTime = 0;
    double lastOutput = 0;
    double maxSpeed = 1.0;
	private static final Clock clock = Clock.systemUTC();

	/**
	 * Constructor to make a variable that manages acceleration.
     * 
     * @param accelTime
     *        The time it should take to accelerate from 0.0 to +/-1.0, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public Accel(double accelTime) {
        setAccelTime(accelTime);
        initialize();
    }

	/**
	 * Constructor to make a variable that manages acceleration.
     * 
     * @param accelTime
     *        The time it should take to accelerate from 0 to {@code maxSpeed}, in seconds
     * @param decelTime
     *        The time it should take to decelerate from {@code maxSpeed} to 0, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public Accel(double accelTime, double decelTime) {
        this(accelTime);
        setDecelTime(decelTime);
    }

	/**
	 * Returns the motor output value.
	 * 
	 * @param speed
     *        The target motor speed
	 */
    public double getOutput(double speed) {
        int sign = Range.sign(speed);
        int lastSign = Range.sign(lastOutput);
        
        // maxSpeed / (1000 * accelTime) = acceleration, acceleration * time = velocity
        double accelOutput = (accelTime == 0) ? speed : sign * maxSpeed / (1000 * accelTime) * (clock.millis() - startTime);

        // 1 - (acceleration * time) = inverse of above
        double decelOutput = (decelTime == 0) ? speed : maxSpeed * (1 - lastSign / (1000 * decelTime) * (clock.millis() - startTime));
        
        if (Math.abs(accelOutput) < Math.abs(speed) && isAccel) {
            speed = accelOutput;
        }
        else {
            isAccel = false;
        }

        if ((Math.abs(speed) < Math.abs(lastOutput) || sign != lastSign) && !isDecel) {
            // set decelOutput = Math.abs(lastOutput) and solve for startTime; this allows us to decelerate from lastOutput instead of maxSpeed
            startTime = (long) (clock.millis() - 1000 * decelTime / lastSign * (1 - Math.abs(lastOutput) / maxSpeed));
            isDecel = true;
        }
        else if (isDecel) {
            speed = decelOutput;
        }
        else {
            lastOutput = speed;
        }

        return speed;
    }

    /** (Re)activates the acceleration period and resets the acceleration timer. */
    public void initialize() {
        isAccel = true;
        isDecel = false;
        startTime = clock.millis();
        lastOutput = 0;
    }

    /**
     * Sets the time it should take to accelerate from 0.0 to {@code maxSpeed}, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public Accel setAccelTime(double accelTime) {
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
     *        {@code true} [default] if the original {@code Accel} object should be modified as well
     * @return
     *        The resulting {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public Accel setAccelTime(double accelTime, boolean modifyOrig) {
        if (modifyOrig) {
            return setAccelTime(accelTime);
        }
        Accel accel = this;
        return accel.setAccelTime(accelTime);
    }

    /**
     * Sets the time it should take to decelerate from {@code maxSpeed} to 0.0, in seconds.
     * 
     * @param decelTime
     *        The deceleration time
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code decelTime} is negative
     */
    public Accel setDecelTime(double decelTime) {
        if (decelTime < 0.0) {
            throw new IllegalArgumentException("Illegal accelTime Value: " + decelTime + "\nValue cannot be negative");
        }
        this.decelTime = decelTime;

        return this;
    }

    /**
     * Sets the time it should take to decelerate from {@code maxSpeed} to 0.0, in seconds.
     * 
     * @param decelTime
     *        The deceleration time
     * @param modifyOrig
     *        {@code true} [default] if the original {@code Accel} object should be modified as well
     * @return
     *        The resulting {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code decelTime} is negative
     */
    public Accel setDecelTime(double decelTime, boolean modifyOrig) {
        if (modifyOrig) {
            return setDecelTime(decelTime);
        }
        Accel accel = this;
        return accel.setDecelTime(decelTime);
    }

    /**
     * Sets the time it should take to accelerate from 0.0 to {@code maxSpeed}, in seconds,
     * and the time it should take to decelerate from {@code maxSpeed} to 0.0, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @param decelTime
     *        The deceleration time
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} or {@code decelTime} is negative
     */
    public Accel setAccelTimes(double accelTime, double decelTime) {
        setAccelTime(accelTime);
        setDecelTime(decelTime);
        return this;
    }

    /**
     * Sets the time it should take to accelerate from 0.0 to {@code maxSpeed}, in seconds,
     * and the time it should take to decelerate from {@code maxSpeed} to 0.0, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @param decelTime
     *        The deceleration time
     * @param modifyOrig
     *        {@code true} [default] if the original {@code Accel} object should be modified as well
     * @return
     *        The resulting {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} or {@code decelTime} is negative
     */
    public Accel setAccelTimes(double accelTime, double decelTime, boolean modifyOrig) {
        if (modifyOrig) {
            return setAccelTimes(accelTime, decelTime);
        }
        Accel accel = this;
        return accel.setAccelTimes(accelTime, decelTime);
    }

    /**
     * Sets the maximum motor speed used for acceleration.
     * 
     * @param maxSpeed
     *        The maximum motor speed
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code maxSpeed} is not positive
     */
    public Accel setMaxSpeed(double maxSpeed) {
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
     *        {@code true} [default] if the original {@code Accel} object should be modified as well
     * @return
     *        The resulting {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code maxSpeed} is not positive
     */
    public Accel setMaxSpeed(double maxSpeed, boolean modifyOrig) {
        if (modifyOrig) {
            return setMaxSpeed(maxSpeed);
        }
        Accel accel = this;
        return accel.setMaxSpeed(maxSpeed);
    }
}