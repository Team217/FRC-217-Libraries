package org.team217.ctre;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Creates a class to manage pulses of the Johnson Electric PLG Hall Effect Sensors as a relative quadrature encoder.
 * 
 * @author ThunderChickens 217
 */
public class JohnsonPLGEncoder {
    private final DigitalInput hallSensor1, hallSensor2;
    
    private boolean isLast1, isLast2;
    private volatile int encoder = 0;

    /**
     * Manages pulses of the Johnson Electric PLG Hall Effect Sensors as a relative quadrature encoder.
     * 
     * @param hallChannel1
     *        The DIO channel for Hall Sensor 1 (yellow)
     * @param hallChannel2
     *        The DIO channel for Hall Sensor 2 (green)
     */
    public JohnsonPLGEncoder(int hallChannel1, int hallChannel2) {
        hallSensor1 = new DigitalInput(hallChannel1);
        hallSensor2 = new DigitalInput(hallChannel2);

        isLast1 = getSensor1Raw();
        isLast2 = getSensor2Raw();
        
        // We need to update this faster than the robot code will run, either
        // use a loop in a separate thread (below) or interrupts
        Thread update = new Thread() {
            public void run() {
                while (true) {
                    Thread.onSpinWait(); // makes this thread lower priority
                    update();
                }
            }
        };
        update.setDaemon(true);
        update.start();
    }

    /** Returns the raw value of Hall Effect Sensor 1. */
    public boolean getSensor1Raw() {
        return hallSensor1.get();
    }

    /** Returns the raw value of Hall Effect Sensor 2. */
    public boolean getSensor2Raw() {
        return hallSensor2.get();
    }

    /** Returns the calculated value of the encoder. */
    public int get() {
        return encoder;
    }

    /**
     * Sets the value of the encoder.
     * 
     * @param position
     *        The new encoder value
     */
    public void set(int position) {
        encoder = position;
    }

    /**
     * Updates the value of the encoder based on the pulses.
     * This should be called continuously.
     */
    private void update() {
        boolean isSame1 = isLast1 == getSensor1Raw();
        boolean isSame2 = isLast2 == getSensor2Raw();
        
        // check if one sensor has changed (can't do anything if both change)
        if (isSame1 != isSame2) {
            // First sensor (fwd):   |‾|_|‾|_
            // Second sensor (fwd):  _|‾|_|‾|
            if (isSame1) {
                // first sensor went unchanged, check where the second sensor went
                if (getSensor1Raw() == getSensor2Raw()) {
                    // second sensor moved towards the first (such as from low
                    // to high while first is high), going forward, increment
                    encoder++;
                }
                else {
                    // second sensor moved away from the first (such as from high
                    // to low while first is high), going backward, decrement
                    encoder--;
                }
            }
            else if (isSame2) {
                // second sensor went unchanged, check where the first sensor went
                if (getSensor1Raw() == getSensor2Raw()) {
                    // first sensor moved towards the second (such as from high
                    // to low while second is low), going backward, decrement
                    encoder--;
                }
                else {
                    // first sensor moved away from the second (such as from low
                    // to high while second is low), going forward, increment
                    encoder++;
                }
            }
        }

        isLast1 = getSensor1Raw();
        isLast2 = getSensor2Raw();
    }
}