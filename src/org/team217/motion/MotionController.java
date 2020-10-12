package org.team217.motion;

/**
 * A class to create objects that apply acceleration and jerk control to velocity.
 * 
 * @author ThunderChickens 217
 */
public class MotionController {
    private AccelController accel, jerk;
    private double lastVel = 0;

    /**
     * Creates a new Motion Controller with the given target acceleration and target jerk.
     * 
     * @param targetAccel
     *        The target acceleration, in units/second^2
     * @param targetJerk
     *        The target jerk, in units/second^3
     * 
     * @author ThunderChickens 217
     */
    public MotionController(double targetAccel, double targetJerk) {
        accel = new AccelController(0);
        jerk = new AccelController(targetJerk, targetAccel);
    }

    /**
     * Creates a new Motion Controller with the given target acceleration, target jerk, and maximum velocity.
     * 
     * @param targetAccel
     *        The target acceleration, in units/second^2
     * @param targetJerk
     *        The target jerk, in units/second^3
     * @param maxVel
     *        The maximum velocity, in units/second
     * 
     * @author ThunderChickens 217
     */
    public MotionController(double targetAccel, double targetJerk, double maxVel) {
        accel = new AccelController(0, maxVel);
        jerk = new AccelController(targetJerk, targetAccel);
    }

    /**
     * Sets the target acceleration of the controller.
     * 
     * @param targetAccel
     *        The target acceleration, in units/second^2
     * @return
     *        {@code false} if the target acceleration is not positive
     */
    public boolean setTargetAccel(double targetAccel) {
        return jerk.setMaxVel(targetAccel);
    }

    /**
     * Sets the target jerk of the controller.
     * 
     * @param targetJerk
     *        The target jerk, in units/second^3
     * @return
     *        {@code false} if the target jerk is not positive
     */
    public boolean setTargetJerk(double targetJerk) {
        return jerk.setTargetAccel(targetJerk);
    }

    /**
     * Sets the maximum velocity of the controller.
     * 
     * @param maxVel
     *        The maximum velocity, in units/second
     * @return
     *        {@code false} if the maximum velocity is not positive
     */
    public boolean setMaxVel(double maxVel) {
        return accel.setMaxVel(maxVel);
    }

    /**
     * Sets the update period of velocity for the controller.
     * 
     * @param period
     *        The update period of velocity, in seconds
     * @return
     *        {@code false} if the period is not positive
     */
    public boolean setPeriod(double period) {
        return accel.setPeriod(period) && jerk.setPeriod(period);
    }

    /**
     * Returns the target acceleration, in units/second^2.
     */
    public double getTargetAccel() {
        return jerk.getMaxVel();
    }

    /**
     * Returns the target jerk, in units/second^3.
     */
    public double getTargetJerk() {
        return jerk.getTargetAccel();
    }

    /**
     * Returns the maximum velocity, in units/second.
     */
    public double getMaxVel() {
        return accel.getMaxVel();
    }

    /**
     * Returns the update period of velocity, in units/second.
     */
    public double getPeriod() {
        return accel.getPeriod();
    }

    /**
     * Returns the acceleration controller.
     */
    public AccelController getAccel() {
        return accel;
    }

    /**
     * Returns the jerk controller.
     */
    public AccelController getJerk() {
        return jerk;
    }

    /**
     * Calculates and returns a velocity after applying motion control.
     * 
     * @param velocity
     *        The velocity to control, in units/second
     */
    public double getOutput(double velocity) {
        double acc = (velocity - lastVel) / jerk.getPeriod();
        accel.setTargetAccel(Math.abs(jerk.getOutput(acc)));
        velocity = accel.getOutput(velocity);

        lastVel = velocity;
        return velocity;
    }

    /**
     * Resets the controller calculations to 0.
     */
    public void reset() {
        lastVel = 0;
        accel.reset();
        jerk.reset();
    }
}