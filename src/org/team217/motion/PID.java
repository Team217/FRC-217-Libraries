package org.team217.motion;

import org.team217.*;

/**
 * A class that runs and controls PID systems.
 * 
 * @author ThunderChickens 217
 */
public class PID {
    private double kP, kI, kD;
    private double period;

    private double integratorRange = 0;
    private double maxIntegrator = 0;
    private double target = 0;
    
    private double lastError = 0;
    private double totalError = 0;
    private double velocityError = 0;
    
    /**
     * Creates a new PID controller.
     * 
     * @param kP
     *        The proportional coefficient
     * @param kI
     *        The integral coefficient
     * @param kD
     *        The derivative coefficient
     * 
     * @author ThunderChickens 217
     */
    public PID(double kP, double kI, double kD) {
        this(kP, kI, kD, 0.02);
    }
    
    /**
     * Creates a new PID controller.
     * 
     * @param kP
     *        The proportional coefficient
     * @param kI
     *        The integral coefficient
     * @param kD
     *        The derivative coefficient
     * @param period
     *        The period between controller updates, in seconds
     * 
     * @author ThunderChickens 217
     */
    public PID(double kP, double kI, double kD, double period) {
        set(kP, kI, kD, period);
    }

    /**
     * Sets the coefficient values of the PID controller.
     * 
     * @param kP
     *        The proportional coefficient
     * @param kI
     *        The integral coefficient
     * @param kD
     *        The derivative coefficient
     * @return
     *        {@code false} if kP, kI, or kD is negative
     */
    public boolean set(double kP, double kI, double kD) {
        return setP(kP) & setI(kI) & setD(kD);
    }

    /**
     * Sets the coefficient values and update period of the PID controller.
     * 
     * @param kP
     *        The proportional coefficient
     * @param kI
     *        The integral coefficient
     * @param kD
     *        The derivative coefficient
     * @param period
     *        The period between controller updates, in seconds
     * @return
     *        {@code false} if kP, kI, or kD is negative or if period is not positive
     */
    public boolean set(double kP, double kI, double kD, double period) {
        return set(kP, kI, kD) & setPeriod(period);
    }

    /**
     * Sets the value of the proportional coefficient.
     * 
     * @param kP
     *        The proportional coefficient
     * @return
     *        {@code false} if kP is negative
     */
    public boolean setP(double kP) {
        if (kP < 0) {
            return false;
        }
        this.kP = kP;
        return true;
    }

    /**
     * Sets the value of the integral coefficient.
     * 
     * @param kI
     *        The integral coefficient
     * @return
     *        {@code false} if kI is negative
     */
    public boolean setI(double kI) {
        if (kI < 0) {
            return false;
        }
        this.kI = kI;
        return true;
    }

    /**
     * Sets the value of the derivative coefficient.
     * 
     * @param kD
     *        The derivative coefficient
     * @return
     *        {@code false} if kD is negative
     */
    public boolean setD(double kD) {
        if (kD < 0) {
            return false;
        }
        this.kD = kD;
        return true;
    }

    /**
     * Sets the value of the controller update period.
     * 
     * @param period
     *        The period between controller updates, in seconds
     * @return
     *        {@code false} if period is not positive
     */
    public boolean setPeriod(double period) {
        if (period <= 0) {
            return false;
        }
        this.period = period;
        return true;
    }

    /**
     * Sets the range of the position error for which the integrator will run.
     * 
     * @param integratorRange
     *        The range of the position error; 0 disables the range
     * @return
     *        {@code false} if integratorRange is negative
     */
    public boolean setIntegratorRange(double integratorRange) {
        if (integratorRange < 0) {
            return false;
        }
        this.integratorRange = integratorRange;
        return true;
    }

    /**
     * Sets the maximum allowed value of the integrator output.
     * 
     * @param maxIntegrator
     *        The maximum output of the integrator; 0 disables the maximum integrator output
     * @return
     *        {@code false} if maxIntegrator is negative
     */
    public boolean setMaxIntegrator(double maxIntegrator) {
        if (maxIntegrator < 0) {
            return false;
        }
        this.maxIntegrator = maxIntegrator;
        return true;
    }

    /**
     * Sets the target position of the PID controller.
     * 
     * @param target
     *        The target position
     * @return
     *        {@code false} if the target could not be set
     */
    public boolean setTarget(double target) {
        this.target = target;
        return true;
    }

    /**
     * Returns the value of the proportional coefficient.
     */
    public double getP() {
        return kP;
    }

    /**
     * Returns the value of the integral coefficient.
     */
    public double getI() {
        return kI;
    }

    /**
     * Returns the value of the derivative coefficient.
     */
    public double getD() {
        return kD;
    }

    /**
     * Returns the value of the controller update period.
     */
    public double getPeriod() {
        return period;
    }

    /**
     * Returns the range of the position error for which the integrator will run.</p>
     * A value of 0 means there is no range.
     */
    public double getIntegratorRange() {
        return integratorRange;
    }

    /**
     * Returns the maximum allowed value of the integrator output.</p>
     * A value of 0 means there is no maximum integrator output.
     */
    public double getMaxIntegrator() {
        return maxIntegrator;
    }

    /**
     * Returns the target position of the PID controller.
     */
    public double getTarget() {
        return target;
    }

    /**
     * Returns the next output of the PID controller.
     * 
     * @param position
     *        The current position
     */
    public double getOutput(double position) {
        double error = target - position;
        
        // check if our error is within the range at which to start integrating (0 means range is disabled)
        if (integratorRange == 0 || Num.isWithinRange(error, integratorRange)) {
            totalError += error * period;
        }
        else {
            totalError = 0;
        }
        // check if we have a max integrator and, if so, cap off the integrator value
        if (kI != 0 && maxIntegrator != 0) {
            totalError = Num.getValueInRange(totalError, maxIntegrator / kI);
        }
        
        // velocity is the derivative of position; the velocity error is the derivative portion of PID
        velocityError = (error - lastError) / period;
        lastError = error;

        return kP * error + kI * totalError + kD * velocityError;
    }

    /**
     * Returns the next output of the PID controller.
     * 
     * @param position
     *        The current position
     * @param target
     *        The target position
     */
    public double getOutput(double position, double target) {
        setTarget(target);
        return getOutput(position);
    }

    /**
     * Returns the last position error of the PID controller.
     */
    public double getLastError() {
        return lastError;
    }

    /**
     * Returns the velocity error of the PID controller.
     */
    public double getVelocityError() {
        return velocityError;
    }

    /**
     * Resets the PID controller errors.
     */
    public void reset() {
        totalError = 0;
        lastError = 0;
    }
}