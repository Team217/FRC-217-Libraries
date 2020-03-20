package org.team217.motion;

import org.team217.*;

/**
 * A class to create geometric motion profiles.
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
        public final double maxVel, maxAccel, maxJerk;

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
        public Constraints(double maxVel, double maxAccel, double maxJerk) {
            if (maxVel == 0 || maxAccel == 0 || maxJerk == 0) {
                throw new IllegalArgumentException("Error: Profile constraints cannot be zero");
            }
            this.maxVel = Math.abs(maxVel);
            this.maxAccel = Math.abs(maxAccel);
            this.maxJerk = Math.abs(maxJerk);
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

    private double endJerk1;
    private double endAccel;
    private double endJerk2;
    private double endFullVel;
    private double endJerk3;
    private double endDecel;
    private double endJerk4;

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
        
        double cutoffBegin = initial.velocity / constraints.maxAccel;
        double cutoffDistBegin = cutoffBegin * cutoffBegin * constraints.maxAccel / 2.0;

        double cutoffEnd = goal.velocity / constraints.maxAccel;
        double cutoffDistEnd = cutoffEnd * cutoffEnd * constraints.maxAccel / 2.0;

        double fullTrapezoidDist = cutoffDistBegin + (goal.position - initial.position) + cutoffDistEnd;

        double jerkTime = constraints.maxJerk == 0 ? 0 : constraints.maxAccel / constraints.maxJerk;
        double jerkVel = constraints.maxJerk / 2 * jerkTime * jerkTime;
        double accelTime = (constraints.maxVel - 2 * jerkVel) / constraints.maxAccel;

        double fullSpeedDist = fullTrapezoidDist - 2 * (
            constraints.maxJerk / 6 * jerkTime * jerkTime * jerkTime +
            (jerkVel + constraints.maxAccel / 2 * accelTime) * accelTime +
            (constraints.maxVel - jerkVel + constraints.maxAccel / 2 * jerkTime - constraints.maxJerk / 6 * jerkTime * jerkTime) * jerkTime
        );

        if (fullSpeedDist < 0) {
            accelTime = constraints.maxVel / constraints.maxAccel;
            jerkTime = 0; // for now, just make a triangle
            fullSpeedDist = fullTrapezoidDist - constraints.maxAccel * accelTime * accelTime;

            if (fullSpeedDist < 0) {
                accelTime = Math.sqrt(fullTrapezoidDist / constraints.maxAccel);
                fullSpeedDist = 0;
            }
        }

        endJerk1 = jerkTime;
        endAccel = endJerk1 + accelTime - cutoffBegin;
        endJerk2 = endAccel + jerkTime;
        endFullVel = endJerk2 + fullSpeedDist / constraints.maxVel;
        endJerk3 = endFullVel + jerkTime;
        endDecel = endJerk3 + accelTime - cutoffEnd;
        endJerk4 = endDecel + jerkTime;
    }

    /**
     * Calculates and returns the target state of the profile at a given time.
     * 
     * @param t
     *        The time, in seconds
     */
    public State getOutput(double t) {
        State result = new State(initial.velocity, initial.position);

        if (t < endJerk1) {
            result.velocity += constraints.maxJerk / 2 * t * t;
            result.position += (initial.velocity + constraints.maxJerk / 6 * t * t) * t;
        }
        else if (t < endAccel) {
            result.velocity += constraints.maxJerk / 2 * endJerk1 * endJerk1 +
                constraints.maxAccel * (t - endJerk1);
            result.position += (initial.velocity + constraints.maxJerk / 6 * endJerk1 * endJerk1) * endJerk1 +
                (initial.velocity + constraints.maxJerk / 2 * endJerk1 * endJerk1 + constraints.maxAccel / 2 * (t - endJerk1)) * (t - endJerk1);
        }
        else if (t < endJerk2) {
            result.velocity = constraints.maxVel - constraints.maxJerk / 2 * (endJerk2 - endAccel) * (endJerk2 - endAccel) + (constraints.maxAccel - constraints.maxJerk / 2 * (t - endAccel)) * (t - endAccel);
            result.position += (initial.velocity + constraints.maxJerk / 6 * endJerk1 * endJerk1) * endJerk1 +
                (initial.velocity + constraints.maxJerk / 2 * endJerk1 * endJerk1 + constraints.maxAccel / 2 * (endAccel - endJerk1)) * (endAccel - endJerk1) +
                (constraints.maxVel - constraints.maxJerk / 2 * (endJerk2 - endAccel) * (endJerk2 - endAccel) + (constraints.maxAccel / 2 - constraints.maxJerk / 6 * (t - endAccel)) * (t - endAccel)) * (t - endAccel);
        }
        else if (t < endFullVel) {
            result.velocity = constraints.maxVel;
            result.position += (initial.velocity + constraints.maxJerk / 6 * endJerk1 * endJerk1) * endJerk1 +
                (initial.velocity + constraints.maxJerk / 2 * endJerk1 * endJerk1 + constraints.maxAccel / 2 * (endAccel - endJerk1)) * (endAccel - endJerk1) +
                (constraints.maxVel - constraints.maxJerk / 2 * (endJerk2 - endAccel) * (endJerk2 - endAccel) + (constraints.maxAccel / 2 - constraints.maxJerk / 6 * (endJerk2 - endAccel)) * (endJerk2 - endAccel)) * (endJerk2 - endAccel) +
                constraints.maxVel * (t - endJerk2);
        }
        else if (t < endJerk3) {
            result.velocity = constraints.maxVel - constraints.maxJerk / 2 * (t - endFullVel) * (t - endFullVel);
            result.position = goal.position - (
                (goal.velocity + constraints.maxJerk / 6 * (endJerk4 - endDecel) * (endJerk4 - endDecel)) * (endJerk4 - endDecel) +
                (goal.velocity + constraints.maxJerk / 2 * (endJerk4 - endDecel) * (endJerk4 - endDecel) + constraints.maxAccel / 2 * (endDecel - endJerk3)) * (endDecel - endJerk3) +
                (constraints.maxVel - constraints.maxJerk / 2 * (endJerk3 - endFullVel) * (endJerk3 - endFullVel) + (constraints.maxAccel / 2 - constraints.maxJerk / 6 * (endJerk3 - t)) * (endJerk3 - t)) * (endJerk3 - t)
            );
        }
        else if (t < endDecel) {
            result.velocity = constraints.maxVel - (
                constraints.maxJerk / 2 * (endJerk3 - endFullVel) * (endJerk3 - endFullVel) +
                constraints.maxAccel * (t - endJerk3)
            );
            result.position = goal.position - (
                (goal.velocity + constraints.maxJerk / 6 * (endJerk4 - endDecel) * (endJerk4 - endDecel)) * (endJerk4 - endDecel) +
                (goal.velocity + constraints.maxJerk / 2 * (endJerk4 - endDecel) * (endJerk4 - endDecel) + constraints.maxAccel / 2 * (endDecel - t)) * (endDecel - t)
            );
        }
        else if (t < endJerk4) {
            result.velocity = goal.velocity + constraints.maxJerk / 2 * (endJerk4 - endDecel) * (endJerk4 - endDecel) - (constraints.maxAccel - constraints.maxJerk / 2 * (t - endDecel)) * (t - endDecel);
            result.position = goal.position - (
                (goal.velocity + constraints.maxJerk / 6 * (endJerk4 - t) * (endJerk4 - t)) * (endJerk4 - t)
            );
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