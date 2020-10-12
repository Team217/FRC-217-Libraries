package org.team217.motion;

import org.team217.*;

/**
 * A class to create motion profiles using sinusoidal waves.
 * 
 * @author ThunderChickens 217
 */
public class GeometricProfiler {
    /**
     * A class to contain information about profile constraints.
     * 
     * @author ThunderChickens 217
     */
    public static class Constraints {
        public final double maxVel, maxAccel;

        /**
         * Creates a new set of profile constraints.
         * 
         * @param maxVel
         *        The maximum velocity
         * @param maxAccel
         *        The maximum acceleration, in dv/second
         * @param maxJerk
         *        The maximum jerk, in dv/second^2
         * 
         * @author ThunderChickens 217
         */
        public Constraints(double maxVel, double maxAccel) {
            if (maxVel == 0 || maxAccel == 0) {
                throw new IllegalArgumentException("Error: Profile constraints cannot be zero");
            }
            this.maxVel = Math.abs(maxVel);
            this.maxAccel = Math.abs(maxAccel);
        }
    }

    /**
     * A class to contain information about a state of the profile.
     */
    public static class State {
        public double velocity, position;

        /**
         * Creates a new profile state.
         * 
         * @param velocity
         *        The velocity at the profile state
         * @param position
         *        The position at the profile state
         */
        public State(double velocity, double position) {
            this.velocity = velocity;
            this.position = position;
        }
    }

    private Constraints constraints;
    private State initial, goal;
    private double direction;
    
    private double endAccel;
    private double endFullVel;
    private double endDecel;
    
    private double endAccelDist;
    private double endFullVelDist;

    /**
     * Creates a new geometric profiler.
     * 
     * @param constraints
     *        The profile constraints
     * @param goal
     *        The final state of the profile
     */
    public GeometricProfiler(Constraints constraints, State goal) {
        this(constraints, new State(0, 0), goal);
    }

    /**
     * Creates a new geometric profiler.
     * 
     * @param constraints
     *        The profile constraints
     * @param initial
     *        The initial state of the profile
     * @param goal
     *        The final state of the profile
     */
    public GeometricProfiler(Constraints constraints, State initial, State goal) {
        direction = initial.position > goal.position ? -1 : 1;

        this.constraints = constraints;
        this.initial = restrict(direct(initial));
        this.goal = restrict(direct(goal));
        
        double distance = Math.abs(goal.position - initial.position);
        
        double accelTime = Math.PI * (constraints.maxVel - initial.velocity) / (2 * constraints.maxAccel);
        double decelTime = Math.PI * (constraints.maxVel - goal.velocity) / (2 * constraints.maxAccel);
        
        double accelDistance = Math.PI * (constraints.maxVel - initial.velocity * initial.velocity) / (4 * constraints.maxAccel);
        double decelDistance = Math.PI * (constraints.maxVel - goal.velocity * goal.velocity) / (4 * constraints.maxAccel);
        double fullSpeedDistance = distance - (accelDistance + decelDistance);

        endAccel = accelTime;
        endFullVel = endAccel + fullSpeedDistance / constraints.maxVel;
        endDecel = endFullVel + decelTime;
        
        endAccelDist = accelDistance;
        endFullVelDist = distance - decelDistance;
    }

    /**
     * Calculates and returns the target state of the profile at a given time.
     * 
     * @param t
     *        The time, in seconds
     */
    public State getOutput(double t) {
        State result = new State(initial.velocity, initial.position);
        
        if (t < endAccel) {
            double c = 2 * constraints.maxAccel / (constraints.maxVel - initial.velocity);
            result.velocity += (constraints.maxVel - initial.velocity) / 2 * (1 - Math.cos(c * t));
            result.position += initial.velocity * t + (constraints.maxVel - initial.velocity) / 2 * (t - Math.sin(c * t) / c);
        }
        else if (t < endFullVel) {
            result.velocity = constraints.maxVel;
            result.position += endAccelDist + constraints.maxVel * (t - endAccel);
        }
        else if (t < endDecel) {
            double c = 2 * constraints.maxAccel / (constraints.maxVel - goal.velocity);
            result.velocity = goal.velocity + (constraints.maxVel - goal.velocity) / 2 * (1 + Math.cos(c * (t - endFullVel)));
            result.position = endFullVelDist + goal.velocity * (t - endFullVel) + (constraints.maxVel - goal.velocity) / 2 * ((t - endFullVel) + Math.sin(c * (t - endFullVel)) / c);
        }
        else {
            result.velocity = goal.velocity;
            result.position = goal.position;
        }

        return direct(result);
    }

    /**
     * Returns the state of the profile in the correct direction.
     * 
     * @param state
     *        The state to direct
     */
    private State direct(State state) {
        return new State(direction * state.velocity, direction * state.position);
    }

    /**
     * Returns the state of the profile restricted to the given constraints.
     * 
     * @param state
     *        The state to restrict
     */
    private State restrict(State state) {
        return new State(Num.inRange(state.velocity, constraints.maxVel), state.position);
    }
}