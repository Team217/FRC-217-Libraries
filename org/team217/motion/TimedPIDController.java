package org.team217.motion;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.controller.*;

/**
 * Implements a PID control loop on a timer.
 * 
 * @author ThunderChickens 217, WPILib
 */
public class TimedPIDController extends PIDController {
    private Timer timer = new Timer();
    private double output = 0;

    /**
     * Allocates a timer-managed PIDController with the given constants for Kp, Ki, and Kd.
     *
     * @param Kp
     *        The proportional coefficient
     * @param Ki
     *        The integral coefficient
     * @param Kd
     *        The derivative coefficient
     * @param period
     *        The period between controller updates in seconds
     * 
     * @author ThunderChickens 217, WPILib
     */
    public TimedPIDController(double Kp, double Ki, double Kd, double period) {
        super(Kp, Ki, Kd, period);
        timer.start();
    }

    /**
     * Returns the next output of the PID controller if the period has passed.
     *
     * @param measurement
     *        The current measurement of the process variable
     */
    @Override
    public double calculate(double measurement) {
        if (timer.hasPeriodPassed(getPeriod())) {
            output = super.calculate(measurement);
        }
        return output;
    }
}