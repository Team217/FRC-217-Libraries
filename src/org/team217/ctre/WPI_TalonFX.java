package org.team217.ctre;

import com.ctre.phoenix.motorcontrol.*;

/**
 * WPI Talon FX Class (Extended). Class supports communicating over CANbus and over ribbon-cable (CAN Talon FX).
 * 
 * @author ThunderChickens 217, Cross the Road Electronics
 */
public class WPI_TalonFX extends com.ctre.phoenix.motorcontrol.can.WPI_TalonFX {
    /**
     * Constructor for creating a {@code TalonFX} motor controller for FRC.
     * 
     * @param deviceNumber
     *        The Device ID of the motor controller
     */
    public WPI_TalonFX(int deviceNumber) {
        super(deviceNumber);
    }
    
    /** Returns {@code true} iff forward limit switch is closed, {@code false} iff switch is open. This function works regardless if limit switch feature is enabled. */
    public boolean getLimitFwd() {
        return getSensorCollection().isFwdLimitSwitchClosed() != 0;
    }
    
    /** Returns {@code true} iff reverse limit switch is closed, {@code false} iff switch is open. This function works regardless if limit switch feature is enabled. */
    public boolean getLimitRev() {
        return getSensorCollection().isRevLimitSwitchClosed() != 0;
    }

    /** Sets up the motor controller to use the integrated sensor. */
    public void setup() {
        configFactoryDefault();
        configSelectedFeedbackSensor(FeedbackDevice.IntegratedSensor, 0, 0);
        setSelectedSensorPosition(0);
        set(0);
    }

    /**
     * Sets up the motor controller to use the integrated sensor.
     * 
     * @param neutralMode
     *        The neutral mode of the controller (coast or brake)
     */
    public void setup(NeutralMode neutralMode) {
        setup();
        setNeutralMode(neutralMode);
    }
}