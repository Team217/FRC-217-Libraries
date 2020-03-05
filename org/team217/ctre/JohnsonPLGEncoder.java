package org.team217.ctre;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Creates a class to manage pulses of the Johnson Electric PLG Hall Effect Sensors as a relative encoder.
 * 
 * @author ThunderChickens 217
 */
public class JohnsonPLGEncoder {
    private final DigitalInput hallSensor1, hallSensor2;
    
    private boolean isLast1, isLast2;
    private volatile int encoder = 0;

    /**
     * Manages pulses of the Johnson Electric PLG Hall Effect Sensors as a relative encoder.
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

        Thread update = new Thread() {
            public void run() {
                while (true) {
                    Thread.onSpinWait();
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

        if (isSame1 != isSame2) {
            if (isSame1) {
                if (getSensor1Raw() == getSensor2Raw()) {
                    encoder++;
                }
                else {
                    encoder--;
                }
            }
            else if (isSame2) {
                if (getSensor1Raw() == getSensor2Raw()) {
                    encoder--;
                }
                else {
                    encoder++;
                }
            }
        }

        isLast1 = getSensor1Raw();
        isLast2 = getSensor2Raw();
    }
}