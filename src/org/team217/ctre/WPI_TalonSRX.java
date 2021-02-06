package org.team217.ctre;

import com.ctre.phoenix.motorcontrol.*;

/**
 * WPI Talon SRX Class (Extended). Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 * 
 * @author ThunderChickens 217, Cross the Road Electronics
 */
public class WPI_TalonSRX extends com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX {
    /**
     * Constructor for creating a {@code TalonSRX} motor controller for FRC.
     * 
     * @param deviceNumber
     *        The Device ID of the motor controller
     */
    public WPI_TalonSRX(int deviceNumber) {
        super(deviceNumber);
    }

    /** Returns {@code true} iff forward limit switch is closed, {@code false} iff switch is open. This function works regardless if limit switch feature is enabled. */
    public boolean getLimitFwd() {
        return getSensorCollection().isFwdLimitSwitchClosed();
    }

    /** Returns {@code true} iff reverse limit switch is closed, {@code false} iff switch is open. This function works regardless if limit switch feature is enabled. */
    public boolean getLimitRev() {
        return getSensorCollection().isRevLimitSwitchClosed();
    }

    /** Sets up the motor controller to use a quadrature encoder. */
    public void setup() {
        configFactoryDefault();
        configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        setSelectedSensorPosition(0);
        set(0);
    }
    
    /**
     * Sets up the motor controller to use a quadrature encoder.
     * 
     * @param neutralMode
     *        The neutral mode of the controller (coast or brake)
     */
    public void setup(NeutralMode neutralMode) {
        setup();
        setNeutralMode(neutralMode);
    }
}