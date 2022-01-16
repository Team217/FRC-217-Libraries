package org.team217.wpi;

import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.CounterBase.EncodingType;

/**
 * Creates a class to manage pulses of the Johnson Electric PLG Hall Effect Sensors as a relative quadrature encoder.
 * 
 * @author ThunderChickens 217
 */
public class JohnsonPLGEncoder {
    private final Encoder encoder;
    private int offset = 0;

    /**
     * Manages pulses of the Johnson Electric PLG Hall Effect Sensors as a relative quadrature encoder.
     * 
     * @param hallChannel1
     *        The DIO channel for Hall Sensor 1 (yellow)
     * @param hallChannel2
     *        The DIO channel for Hall Sensor 2 (green)
     * @param isInverted
     *        {@code true} if the value of the encoder should be inverted
     */
    public JohnsonPLGEncoder(int hallChannel1, int hallChannel2, boolean isInverted) {
        encoder = new Encoder(hallChannel1, hallChannel2, isInverted, EncodingType.k4X);
    }

    /**
     * Manages pulses of the Johnson Electric PLG Hall Effect Sensors as a relative quadrature encoder.
     * 
     * @param hallChannel1
     *        The DIO channel for Hall Sensor 1 (yellow)
     * @param hallChannel2
     *        The DIO channel for Hall Sensor 2 (green)
     */
    public JohnsonPLGEncoder(int hallChannel1, int hallChannel2) {
        this(hallChannel1, hallChannel2, false);
    }

    /** Returns the current value of the encoder. */
    public int get() {
        return encoder.get() + offset;
    }

    /**
     * Sets the value of the encoder.
     * 
     * @param position
     *        The new encoder value
     */
    public void set(int position) {
        encoder.reset();
        offset = position;
    }

    /**
     * Resets the value of the encoder to 0.
     */
    public void reset() {
        set(0);
    }

    /**
     * Returns the underlying WPILib quadrature encoder object.
     */
    public Encoder getEncoder() {
        return encoder;
    }

    /**
     * Returns the number of encoder counts per revolution.
     */
    public static double getCPR() {
        return 44.4 * 4;
    }
}