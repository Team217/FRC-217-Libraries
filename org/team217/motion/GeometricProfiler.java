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
        // If the final position is less than the current one, going backwards
        this.direction = initial.position > goal.position ? -1 : 1;

        this.constraints = constraints;
        // direct() flips the direction so the calculations are done with positives
        // (so we don't have to Math.abs() stuff), which will be flipped back to
        // negative, if necessary, at the end of getOutput()
        this.initial = restrict(direct(initial));
        this.goal = restrict(direct(goal));
        
        double distance = this.goal.position - this.initial.position;
        
        // Dealing with a cosine wave such that its derivative is maxAccel, which is a form of cos(maxAccel * t) (derivative: maxAccel * sin(maxAccel * t))
        // Since the cos wave is restricted from 0 to 1, it is flipped, shifted up one, and halved, so the inner part must be doubled so the derivative remains the same
        // after Chain Rule, which gives us 1/2 (-cos(maxAccel * 2t) + 1). We need to amplify this by the change in velocity, and we must multiply the inside by
        // the reciprocal of that amplification so the derivative doesn't change, giving us 1/2 (maxVel - initVel) (-cos(maxAccel * 2t / (maxVel - initVel)) + 1)
        
        // The period of this wave is 2pi(maxVel - initVel)/(2 * maxAccel), and from min to max is half that period.
        double accelTime = Math.PI * (this.constraints.maxVel - this.initial.velocity) / (2 * this.constraints.maxAccel);
        double decelTime = Math.PI * (this.constraints.maxVel - this.goal.velocity) / (2 * this.constraints.maxAccel);
        
        // After integrating the above function from 0 to pi(maxVel - initVel)/(2 * maxAccel), we get the distance traveled after reaching maxVel:
        // pi (maxVel - initVel^2) / (4 * maxAccel)
        double accelDistance = Math.PI * (this.constraints.maxVel - this.initial.velocity * this.initial.velocity) / (4 * this.constraints.maxAccel);
        double decelDistance = Math.PI * (this.constraints.maxVel - this.goal.velocity * this.goal.velocity) / (4 * this.constraints.maxAccel);
        // Full speed distance is the full distance excluding the two acceleration distances
        double fullSpeedDistance = distance - (accelDistance + decelDistance);
        
        // Calculate the times at which each of the three stages of motion ends
        this.endAccel = accelTime;
        this.endFullVel = endAccel + fullSpeedDistance / this.constraints.maxVel; // fullSpeedDistance / maxVel gives us time
        this.endDecel = endFullVel + decelTime;
        
        // Calculate the distance traveled by the end of two of the stages (after the third is goal.position)
        this.endAccelDist = accelDistance;
        this.endFullVelDist = distance - decelDistance;
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
            double c = 2 * constraints.maxAccel / (constraints.maxVel - initial.velocity); // the constant that affects the period of the cos wave
            result.velocity += (constraints.maxVel - initial.velocity) / 2 * (1 - Math.cos(c * t)); // the cosine function for velocity discussed in the constructor
            // Integrating the cosine wave from 0 to t gives us:
            // initVel * t + (maxVel - initVel) / 2 * (t - (maxVel - initVel) / (2 * maxAccel) * sin(maxAccel * 2t / (maxVel - initVel))),
            // which simplifies down to the below after substituting in c
            result.position += initial.velocity * t + (constraints.maxVel - initial.velocity) / 2 * (t - Math.sin(c * t) / c);
        }
        else if (t < endFullVel) {
            result.velocity = constraints.maxVel;
            result.position += endAccelDist + constraints.maxVel * (t - endAccel); // (t - endAccel) is the time we've been at full speed
        }
        else if (t < endDecel) {
            double c = 2 * constraints.maxAccel / (constraints.maxVel - goal.velocity);
            // Since this cosine wave goes from 1 to 0, instead of -cos(...), it needs to be +cos(...) (so it's flipped)
            result.velocity = goal.velocity + (constraints.maxVel - goal.velocity) / 2 * (1 + Math.cos(c * (t - endFullVel))); // (t - endFullVel) is the time we've been decelerating
            result.position = endFullVelDist + goal.velocity * (t - endFullVel) + (constraints.maxVel - goal.velocity) / 2 * ((t - endFullVel) + Math.sin(c * (t - endFullVel)) / c);
        }
        else {
            result.velocity = goal.velocity;
            result.position = goal.position;
        }

        return direct(result); // flip the position and velocity if necessary, since we've been working in positives
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