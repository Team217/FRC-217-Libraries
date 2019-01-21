package org.team217;

/**
 * Contains deadband functions for controllers and motors
 * 
 * @author ThunderChickens 217
 */
public class Miscellaneous {
    
    /**
	 * Checks if the joystick is in a given deadband and, if it is, sets the joystick value to 0.
	 * 
	 * @param value
	 *        The joystick value
	 * @param deadband
	 *        The deadband size
	 * @return
	 *        The joystick value if not in the deadband, 0.0 if in the deadband
	 * 
	 * @author ThunderChickens 217
	 */
	public static double deadband(double value, double deadband) {
		if (Math.abs(value) <= Math.abs(deadband)) {
			value = 0.0;
		}
		
		return value;
	}
}