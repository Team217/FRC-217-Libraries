package org.beta.team217;

import java.time.Clock;

import org.team217.*;

/**
 * A class that manages acceleration.
 * 
 * @author ThunderChickens 217
 */
public class Accel {
    private double accelTime = 0, decelTime = 0;
    private boolean isAccel = false, isDecel = false;
    private boolean lastAccel = false, lastDecel = false;
    private long startTime = 0;
    private double lastOutput = 0;
    private double maxSpeed = 1.0;
    private static final Clock clock = Clock.systemUTC();
    
    /**
	 * Constructor to make a blank variable that manages acceleration.
	 * 
	 * @author ThunderChickens 217
	 */
    public Accel() {
        initialize();
    }

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
        speed = Range.inRange(speed, -maxSpeed, maxSpeed);
        int sign = Range.sign(speed);
        int lastSign = Range.sign(lastOutput);

        if (speed == lastOutput) {
            isAccel = false;
            isDecel = false;
        }
        else if (sign == lastSign) {
            isAccel = Math.abs(speed) > Math.abs(lastOutput);
            isDecel = !isAccel;
        }
        else {
            isAccel = lastSign == 0; // If different sign, will only accelerate when lastOutput was 0; otherwise, first decelerates
            isDecel = !isAccel;
        }

        if (isAccel != lastAccel || isDecel != lastDecel) {
            startTime = clock.millis();
            lastAccel = isAccel;
            lastDecel = isDecel;
        }
        
        // maxSpeed / (1000 * accelTime) = acceleration, acceleration * time = velocity
        double accelOutput = accelTime == 0 ? speed : lastOutput + sign * maxSpeed / (1000 * accelTime) * (clock.millis() - startTime);
        double decelOutput = decelTime == 0 ? speed : lastOutput - lastSign * maxSpeed / (1000 * decelTime) * (clock.millis() - startTime);

        boolean shouldAccel = sign == 1 ? accelOutput < speed : sign == -1 ? accelOutput > speed : false;
        if (shouldAccel && isAccel) {
            speed = accelOutput;
        }
        else if (isAccel) {
            lastOutput = speed;
        }
        
        boolean shouldDecel = lastSign == 1 ? decelOutput > speed && decelOutput > 0 : lastSign == -1 ? decelOutput < speed && decelOutput < 0 : false; // Past 0, flips to acceleration
        if (shouldDecel && isDecel) {
            speed = decelOutput;
        }
        else if (isDecel) {
            speed = (lastSign == 1 && decelOutput <= 0) || (lastSign == -1 && decelOutput >= 0) ? 0 : speed; // If went through/to 0, lastOutput = 0, can flip to acceleration next loop
            lastOutput = speed;
        }

        return speed;
    }

    /** (Re)activates the acceleration period and resets the acceleration timer. */
    public void initialize() {
        isAccel = false;
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