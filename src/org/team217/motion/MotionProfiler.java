package org.team217.motion;

/**
 * A class to create objects that apply PID and acceleration and jerk control to control motion.
 * 
 * @author ThunderChickens 217
 */
public class MotionProfiler {
    private PID pid;
    private MotionController controller;

    /**
     * Creates a new motion profiler with the given PID controller and motion controller.
     * 
     * @param pid
     *        The PID controller for velocity control
     * @param controller
     *        The motion controller for acceleration and jerk control
     * 
     * @author ThunderChickens 217
     */
    public MotionProfiler(PID pid, MotionController controller) {
        this.pid = pid;
        this.controller = controller;
    }

    /**
     * Returns the PID controller.
     */
    public PID getPID() {
        return pid;
    }

    /**
     * Returns the motion controller.
     */
    public MotionController getController() {
        return controller;
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
        return controller.getOutput(pid.getOutput(position));
    }

    /**
     * Resets the Motion Profiler.
     */
    public void reset() {
        pid.reset();
        controller.reset();
    }
}