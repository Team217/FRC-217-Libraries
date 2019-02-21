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
        setPID(newP, newI, newD);
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
        setPID(newP, newI, newD, timeout);
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
		setPID(newP, newI, newD);
        setTimeout(timeout);
    }
    
	/**
	 * Returns a PID object with the new PID values.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param newD
	 *        The new kD value
     * @param modifyOrig
     *        {@code true} if the original PID object should be modified as well
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 */
    public PID setPID(double newP, double newI, double newD, boolean modifyOrig) {
        if (modifyOrig) {
            setPID(newP, newI, newD);
            return this;
        }
        else {
            PID pid = this;
            pid.setPID(newP, newI, newD);
            return pid;
        }
    }
    
	/**
	 * Returns a PID object with the new PID values.
	 * 
	 * @param newP
	 *        The new kP value
	 * @param newI
	 *        The new kI value
	 * @param newD
	 *        The new kD value
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
     * @param modifyOrig
     *        {@code true} if the original PID object should be modified as well
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, {@code newD}, or {@code timeout} is negative
	 */
    public PID setPID(double newP, double newI, double newD, int timeout, boolean modifyOrig) {
        if (modifyOrig) {
            setPID(newP, newI, newD, timeout);
            return this;
        }
        else {
            PID pid = this;
            pid.setPID(newP, newI, newD, timeout);
            return pid;
        }
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
     * Returns a PID object with the new kP value.
     * 
     * @param newP
     *        The new kP value
     * @param modifyOrig
     *        {@code true} if the original PID object should be modified as well
	 * 
	 * @exception IllegalArgumentException if {@code newP} is negative
     */
    public PID setP(double newP, boolean modifyOrig) {
        if (modifyOrig) {
            setD(newP);
            return this;
        }
        else {
            PID pid = this;
            pid.setD(newP);
            return pid;
        }
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
     * Returns a PID object with the new kI value.
     * 
     * @param newI
     *        The new kI value
     * @param modifyOrig
     *        {@code true} if the original PID object should be modified as well
	 * 
	 * @exception IllegalArgumentException if {@code newI} is negative
     */
    public PID setI(double newI, boolean modifyOrig) {
        if (modifyOrig) {
            setD(newI);
            return this;
        }
        else {
            PID pid = this;
            pid.setD(newI);
            return pid;
        }
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
     * Returns a PID object with the new kD value.
     * 
     * @param newD
     *        The new kD value
     * @param modifyOrig
     *        {@code true} if the original PID object should be modified as well
	 * 
	 * @exception IllegalArgumentException if {@code newD} is negative
     */
    public PID setD(double newD, boolean modifyOrig) {
        if (modifyOrig) {
            setD(newD);
            return this;
        }
        else {
            PID pid = this;
            pid.setD(newD);
            return pid;
        }
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
     * Returns a PID object with the minimum/maximum output values for which I will accumulate. Default values are -1 and 1, respectively.
     * 
     * @param minimum
	 *                The minimum output value
	 * @param maximum
	 *                The maximum output value
     * @param modifyOrig
     *                {@code true} if the original PID object should be modified as well
	 * 
	 * @exception IllegalArgumentException if {@code minimum} &gt;= {@code maximum}
     */
    public PID setMinMax(double minimum, double maximum, boolean modifyOrig) {
        if (modifyOrig) {
            setMinMax(minimum, maximum);
            return this;
        }
        else {
            PID pid = this;
            pid.setMinMax(minimum, maximum);
            return pid;
        }
    }
	
	/**
	 * Sets the timeout.
	 * 
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
     * 
     * @exception IllegalArgumentException if {@code timeout} is negative
	 */
	public void setTimeout(int timeout) {
		if (timeout < 0) {
			throw new IllegalArgumentException("Illegal timeout Value: " + timeout + "\nValue cannot be negative");
		}
		this.timeout = timeout;
    }
    
    /**
     * Returns a PID object with the timeout.
     * 
	 * @param timeout
	 *                The time to wait before updating I or D, in milliseconds
     * @param modifyOrig
     *                {@code true} if the original PID object should be modified as well
     * 
     * @exception IllegalArgumentException if {@code timeout} is negative
     */
    public PID setTimeout(int timeout, boolean modifyOrig) {
        if (modifyOrig) {
            setTimeout(timeout);
            return this;
        }
        else {
            PID pid = this;
            pid.setTimeout(timeout);
            return pid;
        }
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
    
    /**
     * Returns a PID object with kP, kI, kD, and the accumulated error and last error reset to 0.
     * 
     * @param modifyOrig
     *                {@code true} if the original PID object should have these values reset as well
     */
    public PID resetPID(boolean modifyOrig) {
        if (modifyOrig) {
            resetPID();
            return this;
        }
        else {
            PID pid = this;
            pid.resetPID();
            return pid;
        }
    }
	
	/** Resets the accumulated error and last error. */
	public void resetErrors() {
		aError = 0;
		lastError = 0;
    }

    /**
     * Returns a PID object with the accumulated error and last error reset.
     * 
     * @param modifyOrig
     *                {@code true} if the original PID object should have these values reset as well
     */
    public PID resetErrors(boolean modifyOrig) {
        if (modifyOrig) {
            resetErrors();
            return this;
        }
        else {
            PID pid = this;
            pid.resetErrors();
            return pid;
        }
    }
    
    /** Resets all values to 0. */
    public void reset() {
        resetPID();
        setTimeout(0);
    }

    /**
     * Returns a PID object with all values reset to 0.
     * 
     * @param modifyOrig
     *                {@code true} if the original PID object should be reset as well
     */
    public PID reset(boolean modifyOrig) {
        if (modifyOrig) {
            reset();
            return this;
        }
        else {
            PID pid = this;
            pid.reset();
            return pid;
        }
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
			if (currentError < 0 && aError > 0 || currentError > 0 && aError < 0) { // Reset accumulated error if error changes sign
				aError = 0;
			}
			
			if (pOut < max && pOut > min) { // Only accumulate error if within range (min, max)
				aError += currentError;
				currentTime = clock.millis();
			}
			else {
				aError = 0;
			}
        }
	}
}