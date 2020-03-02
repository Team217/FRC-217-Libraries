package org.team217.motion;

/**
 * A class to create objects that apply PID and Motion Profiling to control motion.
 * 
 * @author ThunderChickens 217
 */
public class MotionController {
    private PID pid;
    private MotionProfiler profiler;

    /**
     * Creates a new Motion Controller with the given PID Controller and Motion Profiler.
     * 
     * @param pid
     *        The PID controller for velocity control
     * @param profiler
     *        The Motion Profiler for acceleration and jerk control
     * 
     * @author ThunderChickens 217
     */
    public MotionController(PID pid, MotionProfiler profiler) {
        this.pid = pid;
        this.profiler = profiler;
    }

    /**
     * Returns the PID Controller.
     */
    public PID getPID() {
        return pid;
    }

    /**
     * Returns the Motion Profiler.
     */
    public MotionProfiler getProfiler() {
        return profiler;
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
        return profiler.getOutput(pid.getOutput(position));
    }

    /**
     * Resets the Motion Controller.
     */
    public void reset() {
        pid.reset();
        profiler.reset();
    }
}