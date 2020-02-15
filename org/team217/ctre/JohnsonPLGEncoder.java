package org.team217.ctre;

import edu.wpi.first.wpilibj.DigitalInput;

/**
 * Creates a class to manage pulses of the Johnson Electric PLG Hall Sensors as a relative encoder.
 * 
 * @author ThunderChickens 217
 */
public class JohnsonPLGEncoder {
    private final DigitalInput hallSensor1, hallSensor2;
    
    private boolean isLast1 = false, isLast2 = false;
    private volatile int encoder = 0;

    /**
     * Manages pulses of the Johnson Electric PLG Hall Sensors as a relative encoder.
     * 
     * @param hallChannel1
     *        The DIO channel for Hall Sensor 1 (yellow)
     * @param hallChannel2
     *        The DIO channel for Hall Sensor 2 (green)
     */
    public JohnsonPLGEncoder(int hallChannel1, int hallChannel2) {
        hallSensor1 = new DigitalInput(hallChannel1);
        hallSensor2 = new DigitalInput(hallChannel2);

        Thread update = new Thread() {
            public void run() {
                while (true) {
                    update();
                }
            }
        };
        update.setDaemon(true);
        update.start();
    }

    /**
     * Returns the raw value of Hall Sensor 1.
     */
    public boolean getSensor1Raw() {
        return hallSensor1.get();
    }

    /**
     * Returns the raw value of Hall Sensor 2.
     */
    public boolean getSensor2Raw() {
        return hallSensor2.get();
    }

    /**
     * Returns the calculated value of the encoder.
     */
    public int get() {
        return encoder;
    }

    /**
     * Sets the value of the encoder.
     * 
     * @param position
     *        The new encoder value
     */
    public void setEncoder(int position) {
        encoder = position;
    }

    /**
     * Updates the value of the encoder based on the pulses.
     * This should be called continuously.
     */
    private void update() {
        if (!isLast2 && !getSensor2Raw()) {
            if (!isLast1 && getSensor1Raw()) {
                encoder++;
            }
            else if (isLast1 && !getSensor1Raw()) {
                encoder--;
            }
        }

        isLast1 = getSensor1Raw();
        isLast2 = getSensor2Raw();
    }
}