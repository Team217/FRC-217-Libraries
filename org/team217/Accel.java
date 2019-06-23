package org.team217;

import java.time.Clock;

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
    private double lastSpeed = 0;
    private double maxSpeed = 1;
    private boolean modifyLastOut = true;
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
	 * Constructor to make a variable that manages acceleration and deceleration.
     * 
     * @param accelTime
     *        The time it should take to accelerate from 0 to {@code maxSpeed}, in seconds
     * @param decelTime
     *        The time it should take to decelerate from {@code maxSpeed} to 0, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public Accel(double accelTime, double decelTime) {
        this();
        setAccelTime(accelTime);
        setDecelTime(decelTime);
    }

	/**
	 * Constructor to make a variable that manages acceleration and deceleration.
     * 
     * @param accelTime
     *        The time it should take to accelerate from 0 to {@code maxSpeed} and decelerate from {@code maxSpeed} to 0, in seconds
	 * 
	 * @author ThunderChickens 217
	 */
    public Accel(double accelTime) {
        this(accelTime, accelTime);
    }

    /** Creates and returns a copy of this object. */
    @Override
    public Accel clone() {
        return new Accel(accelTime, decelTime).setMaxSpeed(maxSpeed);
    }

    /**
     * Sets the time it should take to accelerate from 0 to {@code maxSpeed}, in seconds.
     * 
     * @param accelTime
     *        The acceleration time
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public Accel setAccelTime(double accelTime) {
        if (accelTime < 0) {
            throw new IllegalArgumentException("Illegal accelTime value: " + accelTime + "\nValue cannot be negative");
        }
        this.accelTime = accelTime;

        return this;
    }

    /**
     * Sets the time it should take to decelerate from {@code maxSpeed} to 0, in seconds.
     * 
     * @param decelTime
     *        The deceleration time
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code decelTime} is negative
     */
    public Accel setDecelTime(double decelTime) {
        if (decelTime < 0) {
            throw new IllegalArgumentException("Illegal accelTime value: " + decelTime + "\nValue cannot be negative");
        }
        this.decelTime = decelTime;

        return this;
    }

    /**
     * Sets the time it should take to accelerate from 0 to {@code maxSpeed}, in seconds,
     * and the time it should take to decelerate from {@code maxSpeed} to 0, in seconds.
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
     * Sets the time it should take to accelerate from 0 to {@code maxSpeed}, in seconds,
     * and the time it should take to decelerate from {@code maxSpeed} to 0, in seconds.
     * 
     * @param accelTimes
     *        The acceleration/deceleration time
     * @return
     *        This {@code Accel} object
     * 
     * @exception IllegalArgumentException if {@code accelTime} is negative
     */
    public Accel setAccelTimes(double accelTimes) {
        return setAccelTimes(accelTime, accelTime);
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
        if (maxSpeed <= 0) {
            throw new IllegalArgumentException("Illegal maxSpeed value: " + maxSpeed + "\nValue must be greater than 0");
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
        Accel accel = this.clone();
        return accel.setMaxSpeed(maxSpeed);
    }

    /** (Re)sets the acceleration period. */
    public void initialize() {
        isAccel = false;
        isDecel = false;

        lastAccel = false;
        lastDecel = false;

        modifyLastOut = true;

        startTime = clock.millis();

        lastOutput = 0;
        lastSpeed = 0;
    }

	/**
	 * Returns the motor output value.
	 * 
	 * @param speed
     *        The target motor speed
	 */
    public double getOutput(double speed) {
        speed = Num.inRange(speed, maxSpeed);
        int sign = Num.sign(speed);
        int lastOutSign = Num.sign(lastOutput);
        int lastSign = Num.sign(lastSpeed);

        if (speed == lastSpeed) { // Same speed as last run, no need to change
            isAccel = false;
            isDecel = false;
            lastOutput = speed;
        }
        else if (sign == lastSign) { // Same direction
            isAccel = Math.abs(speed) > Math.abs(lastSpeed); // if speed is farther from 0 than lastSpeed, need to accelerate
            isDecel = !isAccel;
        }
        else {
            isAccel = lastSign == 0; // If different sign, will only accelerate when lastSpeed was 0; otherwise, first decelerates to 0
            isDecel = !isAccel;
        }

        if (isAccel != lastAccel || isDecel != lastDecel) { // Change in operation
            startTime = clock.millis();
            lastAccel = isAccel;
            lastDecel = isDecel;

            if (modifyLastOut) { // true when didn't reach target speed
                lastOutput = lastSpeed; // lastOutput is set to speed when it does reach target speed
            }
            modifyLastOut = true;

            return lastOutput;
        }
        
        // maxSpeed / (1000 * accelTime) = acceleration, acceleration * time = velocity
        double accelOutput = accelTime == 0 ? speed : lastOutput + sign * maxSpeed / (1000 * accelTime) * (clock.millis() - startTime);
        double decelOutput = decelTime == 0 ? speed : lastOutput - lastOutSign * maxSpeed / (1000 * decelTime) * (clock.millis() - startTime);

        boolean shouldAccel = sign == 1 ? accelOutput < speed : sign == -1 ? accelOutput > speed : false; // Accelerate if less than target
        if (shouldAccel && isAccel) {
            speed = accelOutput;
        }
        else if (isAccel) { // Reached target speed or beyond
            lastOutput = speed;
            modifyLastOut = false; // Don't need to modify
        }
        
        boolean shouldDecel = lastOutSign == 1 ? decelOutput > speed && decelOutput > 0 : lastOutSign == -1 ? decelOutput < speed && decelOutput < 0 : false; // Decelerate if greater than target or not past 0
        if (shouldDecel && isDecel) {
            speed = decelOutput;
        }
        else if (isDecel) { // Reached target speed or 0
            speed = (lastOutSign == 1 && decelOutput <= 0) || (lastOutSign == -1 && decelOutput >= 0) ? 0 : speed; // If went through/to 0, lastOutput = 0, needs to accelerate from here to target speed
            lastOutput = speed;
            modifyLastOut = false; // Don't need to modify
        }

        lastSpeed = speed; // speed has been modified above unless no accel/decel

        return speed;
    }
}