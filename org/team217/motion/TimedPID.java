package org.team217.motion;

import edu.wpi.first.wpilibj.*;

/**
 * A class that runs and controls PID systems on a timer.
 * 
 * @author ThunderChickens 217
 */
public class TimedPID extends PID {
    private Timer timer = new Timer();
    private double output = 0;
    
    /**
     * Creates a new timer-managed PID controller.
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
    public TimedPID(double kP, double kI, double kD, double period) {
        super(kP, kI, kD, period);
        timer.start();
    }
    
    /**
     * Returns the next output of the PID controller if the period has passed.
     * 
     * @param position
     *        The current position
     */
    @Override
    public double getOutput(double position) {
        if (timer.advanceIfElapsed(getPeriod())) {
            output = super.getOutput(position);

            if (timer.get() >= 2 * getPeriod()) {
                timer.reset();
            }
        }
        return output;
    }

    /**
     * Returns the next output of the PID controller if the period has passed.
     * 
     * @param position
     *        The current position
     * @param target
     *        The target position
     */
    @Override
    public double getOutput(double position, double target) {
        return super.getOutput(position, target);
    }
}