package org.team217;

/**
 * A class to manage a boolean one-shot, which flips a
 * boolean flag when a trigger switches from low to high.
 * 
 * @author ThunderChickens 217
 */
public class BooleanOneShot {
    private boolean isSet, canSet = true;

    /**
     * Creates a new boolean one-shot with a default flag value of {@code false}.
     */
    public BooleanOneShot() {
        this(false);
    }

    /**
     * Creates a new boolean one-shot.
     * 
     * @param isSet
     *        The default value of the boolean flag
     */
    public BooleanOneShot(boolean isSet) {
        set(isSet);
    }

    /**
     * Returns the value of the boolean one-shot.
     * 
     * @param trigger
     *        The boolean to use as the flag's trigger; the flag changes state
     *        when the trigger changes from {@code false} to {@code true}
     */
    public boolean get(boolean trigger) {
        if (trigger && canSet) {
            // trigger activated, flip the boolean
            isSet = !isSet;
        }
        // make sure we don't flip the boolean again until the trigger is released
        canSet = !trigger;

        return isSet;
    }

    /**
     * Sets the value of the boolean flag.
     * 
     * @param isSet
     *        The new value of the boolean flag
     */
    public void set(boolean isSet) {
        this.isSet = isSet;
    }
}