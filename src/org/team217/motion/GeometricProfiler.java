package org.team217.motion;

import java.util.function.*;
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
         *        The maximum velocity, in units/second
         * @param maxAccel
         *        The maximum acceleration, in units/second^2
         * @param maxJerk
         *        The maximum jerk, in units/second^2
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
        
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof State) {
                State state = (State)obj;
                return this.position == state.position && this.velocity == state.velocity;
            }
            else {
                return false;
            }
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
        // pi (maxVel^2 - initVel^2) / (4 * maxAccel)
        double accelDistance = Math.PI * (this.constraints.maxVel * this.constraints.maxVel - this.initial.velocity * this.initial.velocity) / (4 * this.constraints.maxAccel);
        double decelDistance = Math.PI * (this.constraints.maxVel * this.constraints.maxVel - this.goal.velocity * this.goal.velocity) / (4 * this.constraints.maxAccel);
        // Full speed distance is the full distance excluding the two acceleration distances
        double fullSpeedDistance = distance - (accelDistance + decelDistance);
        
        if (fullSpeedDistance < 0) {
            // path does not reach max velocity, override with a max velocity such that the path is a smooth cosine curve
            // accelDistance + decelDistance = distance, solve for maxVel, result is the below
            double maxVel = Math.sqrt(2 * this.constraints.maxAccel * distance / Math.PI + (this.initial.velocity * this.initial.velocity + this.goal.velocity * this.goal.velocity) / 2);
            // create a new set of constraints with this new maxVel
            this.constraints = new Constraints(maxVel, this.constraints.maxAccel);
            
            // recalculate the variables from earlier using the new constraints
            accelTime = Math.PI * (this.constraints.maxVel - this.initial.velocity) / (2 * this.constraints.maxAccel);
            decelTime = Math.PI * (this.constraints.maxVel - this.goal.velocity) / (2 * this.constraints.maxAccel);
            
            accelDistance = Math.PI * (this.constraints.maxVel * this.constraints.maxVel - this.initial.velocity * this.initial.velocity) / (4 * this.constraints.maxAccel);
            decelDistance = Math.PI * (this.constraints.maxVel * this.constraints.maxVel - this.goal.velocity * this.goal.velocity) / (4 * this.constraints.maxAccel);
            fullSpeedDistance = 0;
        }
        
        // Calculate the times at which each of the three stages of motion ends
        this.endAccel = accelTime;
        this.endFullVel = endAccel + fullSpeedDistance / this.constraints.maxVel; // fullSpeedDistance / maxVel gives us time
        this.endDecel = endFullVel + decelTime;
        
        // Calculate the distance traveled by the end of two of the stages (after the third is goal.position - initial.position)
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
            result.velocity = initial.velocity + (constraints.maxVel - initial.velocity) / 2 * (1 - Math.cos(c * t)); // the cosine function for velocity discussed in the constructor
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
            result.position += endFullVelDist + goal.velocity * (t - endFullVel) + (constraints.maxVel - goal.velocity) / 2 * ((t - endFullVel) + Math.sin(c * (t - endFullVel)) / c);
        }
        else {
            result.velocity = goal.velocity;
            result.position = goal.position;
        }

        return direct(result); // flip the position and velocity if necessary, since we've been working in positives
    }
    
    /**
     * Returns the time left until the profile reaches a target position.
     * 
     * @param target
     *        The target position
     */
    public double timeLeftUntil(double target) {
        // Flip the target to be the same direction as the initial and goal states
        target *= direction;
        double distance = target - initial.position;
        
        if (distance <= 0) {
            // At or past the target position, no time left
            return 0;
        }
        else if (distance < endAccelDist) {
            // In the acceleration phase, apply Newton's method to approximate the zero
            // Newton's method states that to approximate the zero of a function, you can use the recursive equation:
            // x_(n+1) = x_n - f(x_n)/f'(x_n)
            // If the initial approximate is close enough, the equation will converge and get more accurate each iteration.
            // Since it approximates zeroes, and we want s(x) = distance, we apply the recursion to s(x) - distance = 0
            double c = 2 * constraints.maxAccel / (constraints.maxVel - initial.velocity); // the constant that affects the period of the cos wave
            final double d = distance; // Function<> can only use constants when using local variables
            // s(t) - distance (= 0)
            Function<Double, Double> position = t -> initial.velocity * t + (constraints.maxVel - initial.velocity) / 2 * (t - Math.sin(c * t) / c) - d;
            // s'(t)
            Function<Double, Double> dPosition = t -> (constraints.maxVel - initial.velocity) / 2 * (1 - Math.cos(c * t)) + initial.velocity;
            
            // Start the approximation halfway through the acceleration period
            double result = endAccel / 2; // endAccel is the acceleration time
            double lastResult = 0;
            while (Math.abs((result - lastResult) / lastResult) > 0.001) { // loop until error is no more than 0.1%
                lastResult = result;
                // x_(n+1) = x_n - f(x_n)/f'(x_n)
                result = lastResult - position.apply(lastResult) / dPosition.apply(lastResult);
            }
            return result;
        }
        else if (distance < endFullVelDist) {
            // In the full velocity phase, distance / maxVel gives the time taken to travel distance
            distance -= endAccelDist; // subtract off the acceleration period difference
            return endAccel + distance / constraints.maxVel; // add the acceleration period time to the full velocity time
        }
        else if (distance < goal.position - initial.position) {
            // In the deceleration phase, apply Newton's method to approximate the zero
            distance -= endFullVelDist;
            
            double c = 2 * constraints.maxAccel / (constraints.maxVel - goal.velocity);
            final double d = distance;
            Function<Double, Double> position = t -> goal.velocity * t + (constraints.maxVel - goal.velocity) / 2 * (t + Math.sin(c * t) / c) - d;
            Function<Double, Double> dPosition = t -> (constraints.maxVel - goal.velocity) / 2 * (1 + Math.cos(c * t)) + goal.velocity;
            
            double result = (endDecel - endFullVel) / 2; // endDecel - endFullVel gives us the deceleration time
            double lastResult = 0;
            while (Math.abs((result - lastResult) / lastResult) > 0.001) {
                lastResult = result;
                result = lastResult - position.apply(lastResult) / dPosition.apply(lastResult);
            }
            return endFullVel + result;
        }
        else {
            // Target is at or beyond the goal, will take the full path time to reach it
            return totalTime();
        }
    }
    
    /**
     * Returns the total time the profile takes to reach the goal.
     */
    public double totalTime() {
        return endDecel;
    }
    
    /**
     * Returns {@code true} if the profile has reached the goal at the given time.
     * 
     * @param t
     *        The time since the beginning of the profile
     */
    public boolean isFinished(double t) {
        return t >= totalTime();
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
        return new State(Num.getValueInRange(state.velocity, constraints.maxVel), state.position);
    }
}