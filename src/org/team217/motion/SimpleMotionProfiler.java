package org.team217.motion;

/**
 * A class to create objects that apply PID and acceleration control to control motion.
 * 
 * @author ThunderChickens 217
 */
public class SimpleMotionProfiler {
    private PID pid;
    private AccelController accelController;

    /**
     * Creates a new simple motion profiler with the given PID controller and acceleration controller.
     * 
     * @param pid
     *        The PID controller for velocity control
     * @param accelController
     *        The acceleration controller for acceleration control
     * 
     * @author ThunderChickens 217
     */
    public SimpleMotionProfiler(PID pid, AccelController accelController) {
        this.pid = pid;
        this.accelController = accelController;
    }

    /**
     * Returns the PID controller.
     */
    public PID getPID() {
        return pid;
    }

    /**
     * Returns the acceleration controller.
     */
    public AccelController getAccelController() {
        return accelController;
    }

    /**
     * Calculates and returns a velocity after applying the motion profile to the
     * PID output on the current position.
     * 
     * @param position
     *        The current position
     * @param target
     *        The target position
     */
    public double getOutput(double position, double target) {
        pid.setTarget(target);
        return getOutput(position);
    }

    /**
     * Calculates and returns a velocity after applying the motion profile to the
     * PID output on the current position.
     * 
     * @param position
     *        The current position
     */
    public double getOutput(double position) {
        return accelController.getOutput(pid.getOutput(position));
    }

    /**
     * Resets the motion profiler.
     */
    public void reset() {
        pid.reset();
        accelController.reset();
    }
}