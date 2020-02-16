package org.team217.ctre;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.*;

import org.team217.Converter;

/**
 * WPI Talon SRX Class (Extended). Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 * 
 * @author ThunderChickens 217, Cross the Road Electronics
 */
public class WPI_TalonSRX extends com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX {
	protected int zeroPos = 0;
	protected int invertEnc = 1;

	/**
	 * Constructor for creating a {@code TalonSRX} motor controller for FRC.
	 * 
	 * @param deviceNumber
	 *        The Device ID of the motor controller
	 */
	public WPI_TalonSRX(int deviceNumber) {
		super(deviceNumber);
	}
	
	/**
     * Inverts the direction of the encoders.
	 * 
	 * @param isInverted
	 *        {@code true} if the encoder value should be multiplied by -1
     * 
     * @deprecated use {@code setSensorPhase()}
	 */
    @Deprecated(forRemoval = true)
	public void invertEncoder(boolean isInverted) {
		invertEnc = isInverted ? -1 : 1;
	}
	
	/**
     * Returns the Quadrature Encoder position.
     * 
     * @deprecated use {@code configSelectedFeedbackSensor()} and {@code getSelectedSensorPosition()}
     */
    @Deprecated(forRemoval = true)
	public int getEncoder() {
		return invertEnc * getSensorCollection().getQuadraturePosition();
    }

	/** Returns the Analog Encoder position. */
    public int getAnalogEncoder() {
        return invertEnc * (getAnalogRaw() - zeroPos);
    }

	/** Returns the raw Analog Encoder position. */
    public int getAnalogRaw() {
        return getSensorCollection().getAnalogInRaw();
    }
	
	/** Returns the Analog Encoder position for swerve, where positions range from -512 to 512. */
	public int getSwerveAnalog() {
        int pos = getAnalogEncoder();
        
        // Get Analog Encoder position in range [-512, 512]
		pos = (int)Converter.partialAngle(pos, 1024);
		return pos;
    }
    
    /**
     * Sets the Analog Encoder to the given value.
	 * 
	 * @param pos
	 *        New encoder value
     */
    public void setAnalogEncoder(int pos) {
        setAnalogZero(getAnalogRaw() - pos);
    }

	/** Sets the current analog encoder position as zero. */
	public void setAnalogZero() {
		setAnalogZero(getAnalogRaw());
	}

	/**
     * Sets the given analog encoder position as zero.
	 * 
	 * @param pos
	 *        The zero position
     */
	public void setAnalogZero(int pos) {
		zeroPos = pos;
	}

	/**
	 * Sets the Quadrature Encoder to the given value.
	 * 
	 * @param pos
	 *        New encoder value
	 * @return
	 *        Error Code
     * 
     * @deprecated use {@code configSelectedFeedbackSensor()} and {@code setSelectedSensorPosition()}
	 */
    @Deprecated(forRemoval = true)
	public ErrorCode setEncoder(int pos) {
		return getSensorCollection().setQuadraturePosition(invertEnc * pos, 0);
	}
	
	/**
	 * Resets the Quadrature Encoder.
	 * 
	 * @return
     *        error code
     * 
     * @deprecated use {@code setSelectedSensorPosition(0)}
	 */
    @Deprecated(forRemoval = true)
	public ErrorCode resetEncoder() {
		return setEncoder(0);
    }
	
	/** Returns {@code true} iff forward limit switch is closed, {@code false} iff switch is open. This function works regardless if limit switch feature is enabled. */
	public boolean getLimitFwd() {
		return getSensorCollection().isFwdLimitSwitchClosed();
	}
	
	/** Returns {@code true} iff reverse limit switch is closed, {@code false} iff switch is open. This function works regardless if limit switch feature is enabled. */
	public boolean getLimitRev() {
		return getSensorCollection().isRevLimitSwitchClosed();
	}

	/** Sets up the motor controller to use a Quadrature Encoder in brake mode. */
	public void setup() {
        setup(NeutralMode.Brake);
    }
    
	/**
     * Sets up the motor controller to use a Quadrature Encoder.
     * 
     * @param neutralMode
     *        The neutral mode of the controller (coast or brake)
     */
    public void setup(NeutralMode neutralMode) {
        configFactoryDefault();
        configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
        setNeutralMode(neutralMode);
        setSelectedSensorPosition(0);
        set(0);
    }
}