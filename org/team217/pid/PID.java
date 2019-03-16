package org.team217.pid;

import java.time.Clock;
import org.team217.*;

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
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 */
	public PID setPID(double newP, double newI, double newD) {
		setP(newP);
		setI(newI);
        setD(newD);

        return this;
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
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, {@code newD}, or {@code timeout} is negative
	 */
	public PID setPID(double newP, double newI, double newD, int timeout) {
		setPID(newP, newI, newD);
        setTimeout(timeout);

        return this;
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
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, or {@code newD} is negative
	 */
    public PID setPID(double newP, double newI, double newD, boolean modifyOrig) {
        if (modifyOrig) {
            return setPID(newP, newI, newD);
        }
        PID pid = this;
        return pid.setPID(newP, newI, newD);
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
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newP}, {@code newI}, {@code newD}, or {@code timeout} is negative
	 */
    public PID setPID(double newP, double newI, double newD, int timeout, boolean modifyOrig) {
        if (modifyOrig) {
            return setPID(newP, newI, newD, timeout);
        }
        PID pid = this;
        return pid.setPID(newP, newI, newD, timeout);
    }
	
	/**
	 * Sets the kP value.
	 * 
	 * @param newP
	 *        The new kP value
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newP} is negative
	 */
	public PID setP(double newP) {
		if (newP < 0) {
			throw new IllegalArgumentException("Illegal kP value: " + newP + "\nValue cannot be negative");
		}
        kP = newP;

        return this;
	}
	
    /**
     * Sets the kP value.
     * 
     * @param newP
     *        The new kP value
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newP} is negative
     */
    public PID setP(double newP, boolean modifyOrig) {
        if (modifyOrig) {
            setP(newP);
            return this;
        }
        PID pid = this;
        return pid.setD(newP);
    }
	
	/**
	 * Sets the kI value.
	 * 
	 * @param newI
	 *        The new kI value
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newI} is negative
	 */
	public PID setI(double newI) {
		if (newI < 0) {
			throw new IllegalArgumentException("Illegal kI value: " + newI + "\nValue cannot be negative");
		}
        kI = newI;
        
        return this;
	}
	
    /**
     * Sets the kI value.
     * 
     * @param newI
     *        The new kI value
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newI} is negative
     */
    public PID setI(double newI, boolean modifyOrig) {
        if (modifyOrig) {
            return setI(newI);
        }
        PID pid = this;
        return pid.setD(newI);
    }

	/**
	 * Sets the kD value.
	 * 
	 * @param newD
	 *        The new kD value
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newD} is negative
	 */
	public PID setD(double newD) {
		if (newD < 0) {
			throw new IllegalArgumentException("Illegal kD value: " + newD + "\nValue cannot be negative");
		}
        kD = newD;
        
        return this;
    }
    
    /**
     * Sets the kD value.
     * 
     * @param newD
     *        The new kD value
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code newD} is negative
     */
    public PID setD(double newD, boolean modifyOrig) {
        if (modifyOrig) {
            return setD(newD);
        }
        PID pid = this;
        return pid.setD(newD);
    }
	
	/**
	 * Sets the given values as the minimum/maximum output values for which I will accumulate. Default values are -1 and 1, respectively.
	 * 
	 * @param minimum
	 *        The minimum output value
	 * @param maximum
	 *        The maximum output value
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code minimum} &gt;= {@code maximum}
	 */
	public PID setMinMax(double minimum, double maximum) {
		if (minimum >= maximum) {
			throw new IllegalArgumentException(
					"Illegal minimum/maximum value: " + minimum + "/" + maximum + "\nMaximum must be greater than the minimum");
		}
		min = minimum;
        max = maximum;
        
        return this;
    }
    
    /**
     * Sets the given values as the minimum/maximum output values for which I will accumulate. Default values are -1 and 1, respectively.
     * 
     * @param minimum
	 *        The minimum output value
	 * @param maximum
	 *        The maximum output value
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code minimum} &gt;= {@code maximum}
     */
    public PID setMinMax(double minimum, double maximum, boolean modifyOrig) {
        if (modifyOrig) {
            return setMinMax(minimum, maximum);
        }
        PID pid = this;
        return pid.setMinMax(minimum, maximum);
    }
	
	/**
	 * Sets the given value as the minimum/maximum output values for which I will accumulate. Default value is 1 (-1/1, respectively).
	 * 
	 * @param minMax
	 *        The minimum ({@code -minMax}) and maximum ({@code minMax}) output values
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code minMax} is not positive
	 */
    public PID setMinMax(double minMax) {
		if (minMax <= 0) {
			throw new IllegalArgumentException("Illegal minimum/maximum value: " + minMax + "\nValue must be positive");
        }
        return setMinMax(-minMax, minMax);
    }
	
	/**
	 * Sets the given value as the minimum/maximum output values for which I will accumulate. Default value is 1 (-1/1, respectively).
	 * 
	 * @param minMax
	 *        The minimum ({@code -minMax}) and maximum ({@code minMax}) output values
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        This {@code PID} object
	 * 
	 * @exception IllegalArgumentException if {@code minMax} is not positive
	 */
    public PID setMinMax(double minMax, boolean modifyOrig) {
        if (modifyOrig) {
            return setMinMax(minMax);
        }
        PID pid = this;
        return pid.setMinMax(minMax);
    }
	
	/**
	 * Sets the timeout.
	 * 
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
     * @return
     *        This {@code PID} object
     * 
     * @exception IllegalArgumentException if {@code timeout} is negative
	 */
	public PID setTimeout(int timeout) {
		if (timeout < 0) {
			throw new IllegalArgumentException("Illegal timeout value: " + timeout + "\nValue cannot be negative");
		}
        this.timeout = timeout;
        
        return this;
    }
    
    /**
     * Sets the timeout.
     * 
	 * @param timeout
	 *        The time to wait before updating I or D, in milliseconds
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
     * 
     * @exception IllegalArgumentException if {@code timeout} is negative
     */
    public PID setTimeout(int timeout, boolean modifyOrig) {
        if (modifyOrig) {
            return setTimeout(timeout);
        }
        PID pid = this;
        return pid.setTimeout(timeout);
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
        updateCurrentTime();
		return pOut + iOut + dOut;
	}
	
	/**
     * Resets kP, kI, and kD to 0 and resets the accumulated error and last error.
     * 
     * @return
     *        This {@code PID} object
     */
	public PID resetPID() {
		setPID(0, 0, 0);
        resetErrors();
        
        return this;
    }
    
    /**
     * Resets kP, kI, and kD to 0 and resets the accumulated error and last error.
     * 
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
     */
    public PID resetPID(boolean modifyOrig) {
        if (modifyOrig) {
            return resetPID();
        }
        PID pid = this;
        return pid.resetPID();
    }
	
	/**
     * Resets the accumulated error and last error.
     * 
     * @return
     *        This {@code PID} object
     */
	public PID resetErrors() {
		aError = 0;
        lastError = 0;
        
        return this;
    }

    /**
     * Resets the accumulated error and last error.
     * 
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
     */
    public PID resetErrors(boolean modifyOrig) {
        if (modifyOrig) {
            return resetErrors();
        }
        PID pid = this;
        return pid.resetErrors();
    }
    
    /**
     * Resets all values to 0.
     * 
     * @return
     *        This {@code PID} object
     */
    public PID reset() {
        resetPID();
        setTimeout(0);

        return this;
    }

    /**
     * Resets all values to 0.
     * 
     * @param modifyOrig
     *        {@code true} [default] if the original {@code PID} object should be modified as well
     * @return
     *        The resulting {@code PID} object
     */
    public PID reset(boolean modifyOrig) {
        if (modifyOrig) {
            return reset();
        }
        PID pid = this;
        return pid.reset();
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
			if (Num.sign(currentError) != Num.sign(aError)) { // Reset accumulated error if error changes sign
				aError = 0;
			}
			
			if (Num.isWithinRange(pOut, min, max, false)) { // Only accumulate error if within range (min, max)
				aError += currentError;
			}
			else {
				aError = 0;
			}
        }
    }
    
    /** Updates {@code currentTime} if {@code timeout} milliseconds have passed since the last update. */
    private void updateCurrentTime() {
        if (clock.millis() >= currentTime + timeout) {
            currentTime = clock.millis();
        }
    }
}