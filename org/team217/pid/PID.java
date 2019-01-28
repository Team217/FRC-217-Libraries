package org.team217.pid;

import java.time.Clock;

/**
 * A class that runs and controls PID systems.
 * 
 * @author ThunderChickens 217
 */
public class PID {
	
	// Made private because of getValue() functions
	private double kP = 0;
	private double kI = 0;
	private double kD = 0;
	private int timeout = 0;
	private double max = 1;
	private double min = -1;
	
	// Always private
	private double currentError = 0;
	private double lastError = 0;
	private double aError = 0;
	private double pOut = 0, iOut = 0, dOut = 0;
	
	private static final Clock clock = Clock.systemUTC();
    private long currentTime = clock.millis();
    
	/**
	 * Constructor to make a blank variable that holds information about and calculates information for a PID system.
	 * 
	 * @author ThunderChickens 217
	 */
	public PID() { }
	
	/**
	 * Constructor to make a blank variable that holds information about and calculates information for a PID system.
	 * 
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
	 * 
	 * @exception IllegalArgumentException if {@code timeout} is negative
	 * 
	 * @author ThunderChickens 217
	 */
	public PID(int timeout) {
        setTimeout(timeout);
	}
	
	/**
	 * Constructor to make a variable that holds information about and calculates information for a PID system.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param newD
	 *        The new kD value
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 * 
	 * @author ThunderChickens 217
	 */
	public PID(double newP, double newI, double newD) {
		setP(newP);
		setI(newI);
		setD(newD);
	}
	
	/**
	 * Constructor to make a variable that holds information about and calculates information for a PID system.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param newD
	 *        The new kD value
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, {@code newD}, or {@code timeout} is negative
	 * 
	 * @author ThunderChickens 217
	 */
	public PID(double newP, double newI, double newD, int timeout) {
		setP(newP);
		setI(newI);
        setD(newD);
        setTimeout(timeout);
	}
	
	/**
	 * Sets the given values as the values used for the PID.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param newD
	 *        The new kD value
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 */
	public void setPID(double newP, double newI, double newD) {
		setP(newP);
		setI(newI);
		setD(newD);
	}
	
	/**
	 * Sets the given values as the values used for the PID.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param newD
	 *        The new kD value
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, {@code newD}, or {@code timeout} is negative
	 */
	public void setPID(double newP, double newI, double newD, int timeout) {
		setP(newP);
		setI(newI);
        setD(newD);
        setTimeout(timeout);
	}
	
	/**
	 * Sets the kP value.
	 * 
	 * @param newP
	 *        The new kP value
	 * 
	 * @exception IllegalArgumentException if {@code newP} is negative
	 */
	public void setP(double newP) {
		if (newP < 0) {
			throw new IllegalArgumentException("Illegal kP Value: " + newP + "\nValue cannot be negative");
		}
		kP = newP;
	}
	
	/**
	 * Sets the kI value.
	 * 
	 * @param newI
	 *        The new kI value
	 * 
	 * @exception IllegalArgumentException if {@code newI} is negative
	 */
	public void setI(double newI) {
		if (newI < 0) {
			throw new IllegalArgumentException("Illegal kI Value: " + newI + "\nValue cannot be negative");
		}
		kI = newI;
	}
	
	/**
	 * Sets the kD value.
	 * 
	 * @param newD
	 *        The new kD value
	 * 
	 * @exception IllegalArgumentException if {@code newD} is negative
	 */
	public void setD(double newD) {
		if (newD < 0) {
			throw new IllegalArgumentException("Illegal kD Value: " + newD + "\nValue cannot be negative");
		}
		kD = newD;
	}
	
	/**
	 * Sets the given values as the minimum/maximum output values for which I will accumulate. Default values are -1 and 1, respectively.
	 * 
	 * @param minimum
	 *            The minimum output value
	 * @param maximum
	 *            The maximum output value
	 * 
	 * @exception IllegalArgumentException if {@code minimum} &gt;= {@code maximum}
	 */
	public void setMinMax(double minimum, double maximum) {
		if (minimum >= maximum) {
			throw new IllegalArgumentException(
					"Illegal minimum/maximum value: " + minimum + "/" + maximum + "\nMaximum must be greater than the minimum");
		}
		min = minimum;
		max = maximum;
	}
	
	/**
	 * Sets the timeout.
	 * 
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
	 */
	public void setTimeout(int timeout) {
		if (timeout < 0) {
			throw new IllegalArgumentException("Illegal timeout Value: " + timeout + "\nValue cannot be negative");
		}
		this.timeout = timeout;
	}
	
	/** Returns the current kP value. */
	public double getP() {
		return kP;
	}
	
	/** Returns the current kI value. */
	public double getI() {
		return kI;
	}
	
	/** Returns the current kD value. */
	public double getD() {
		return kD;
	}

	/** Returns the timeout before updating I or D */
	public int getTimeout() {
		return timeout;
	}

	/** Returns the minimum output value for which I and D will update */
	public double getMin() {
		return min;
	}

	/** Returns the maximum output value for which I and D will update */
	public double getMax() {
		return max;
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
        currentError = tar - pos;
        updatePOut();
        updateIOut();
        updateDOut();
		return pOut + iOut + dOut;
	}
	
	/** Resets kP, kI, and kD to 0 and resets the accumulated error and last error. */
	public void resetPID() {
		setPID(0, 0, 0);
		resetErrors();
	}
	
	/** Resets the accumulated error and last error. */
	public void resetErrors() {
		aError = 0;
		lastError = 0;
    }
    
    /** Resets all values to 0. */
    public void reset() {
        resetPID();
        setTimeout(0);
    }
	
	/** Calculates the Proportional output. */
	private void updatePOut() {
        pOut = kP * currentError;
	}
	
	/** Calculates the Integral output. */
	private void updateIOut() {
		updateAccumulatedI();
        iOut = kI * aError;
	}
	
	/** Calculates the Derivative output. */
	private void updateDOut() {
		if (clock.millis() >= currentTime + timeout) { // Wait for [timeout] milliseconds before updating the D output
		    dOut = kD * (currentError - lastError);
            lastError = currentError;
        }
	}
	
	/** Calculates the Accumulated Integral output for use by the I Output calculation. */
	private void updateAccumulatedI() {
		if (clock.millis() >= currentTime + timeout) { // Wait for [timeout] milliseconds because we don't get new encoder values until then
			if (currentError < 0 && aError > 0 || currentError > 0 && aError < 0) {
				aError = 0;
			}
			
			if (pOut < max && pOut > min) {
				aError += currentError;
				currentTime = clock.millis();
			}
			else {
				aError = 0;
			}
        }
	}
}