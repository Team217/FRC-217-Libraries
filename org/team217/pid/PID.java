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
	private int delay = 0;
	private double max = 1;
	private double min = -1;
	
	// Always private
	private double target = 0;
	private double position = 0;
	private double currentError = 0;
	private double lastError = 0;
	private double aI = 0;
	private double P_Output, I_Output, D_Output;
	
	private static final Clock clock = Clock.systemUTC();
	private long currentTime = clock.millis();
	
	/**
	 * Constructor to make a blank variable that holds information about and calculates information for a PID system.
	 * 
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
	 * 
	 * @author ThunderChickens 217
	 */
	public PID(int timeout) {
		setP(0);
		setI(0);
		setD(0);
		delay = timeout;
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
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 * 
	 * @author ThunderChickens 217
	 */
	public PID(double newP, double newI, double newD, int timeout) {
		setP(newP);
		setI(newI);
		setD(newD);
		delay = timeout;
	}
	
	/**
	 * Constructor to make a variable that holds information about and calculates information for a PI system.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param timeout
	 *        The time to wait before updating I, in milliseconds
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 * 
	 * @author ThunderChickens 217
	 */
	public PID(double newP, double newI, int timeout) {
		setP(newP);
		setI(newI);
		setD(0);
		delay = timeout;
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
		resetError();
	}
	
	/**
	 * Sets the given values as the values used for the PI.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * 
	 * @exception IllegalArgumentException if {@code newP} or {@code newI} is negative
	 */
	public void setPID(double newP, double newI) {
		setP(newP);
		setI(newI);
		setD(0);
		resetError();
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
		delay = timeout;
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
		return delay;
	}

	/** Returns the minimum output value for which I will accumulate */
	public double getMin() {
		return min;
	}

	/** Returns the maximum output value for which I will accumulate */
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
		target = tar;
		position = pos;
		getCurrentError();
		double output = getP_Output() + getI_Output() + getD_Output();
		return output;
	}
	
	/** Resets kP, kI, and kD to 0. */
	public void resetPID() {
		setPID(0, 0, 0);
		resetError();
	}
	
	/** Resets the accumulated I value and the last error value. */
	public void resetError() {
		aI = 0;
		lastError = 0;
	}
	
	/**
	 * Sets the kP value.
	 * 
	 * @param newP
	 *        The new kP value
	 * 
	 * @exception IllegalArgumentException if {@code newP} is negative
	 */
	private void setP(double newP) {
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
	private void setI(double newI) {
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
	private void setD(double newD) {
		if (newD < 0) {
			throw new IllegalArgumentException("Illegal kD Value: " + newD + "\nValue cannot be negative");
		}
		kD = newD;
	}
	
	/** Calculates and returns the Proportional output. */
	private double getP_Output() {
        P_Output = kP * currentError;
        return P_Output;
	}
	
	/** Calculates and returns the Integral output. */
	private double getI_Output() {
		getAccumulatedI();
        I_Output = kI * aI;
        return I_Output;
	}
	
	/** Calculates and returns the Derivative output. */
	private double getD_Output() {
		D_Output = kD * (currentError - lastError);
        updateError();
        return D_Output;
	}
	
	/** Calculates and returns the current error. */
	private double getCurrentError() {
        currentError = target - position;
        return currentError;
	}
	
	/** Calculates the Accumulated Integral output for use by the I Output calculation. */
	private double getAccumulatedI() {
		if (clock.millis() >= currentTime + delay) { // Wait for [delay] milliseconds because we don't get new encoder values until then
			if (P_Output < max && P_Output > min) {
				getCurrentError();
				aI += currentError;
				currentTime = clock.millis();
			}
			else {
				aI = 0;
			}
        }
        return aI;
	}
	
	/** Updates the error and last error values. */
	private void updateError() {
		if (clock.millis() >= currentTime + delay) { // Wait for [delay] milliseconds before updating the last error
			getCurrentError();
			lastError = currentError;
		}
	}
}