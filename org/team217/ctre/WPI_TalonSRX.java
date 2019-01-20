package org.team217.ctre;

import com.ctre.phoenix.ErrorCode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;

/**
 * WPI Talon SRX Class (Extended). Class supports communicating over CANbus and over ribbon-cable (CAN Talon SRX).
 * 
 * @author ThunderChickens 217, Cross the Road Electronics
 */
public class WPI_TalonSRX extends com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX
{
	long zeroPos = 0;
	int invertEnc = 1;

	/**
	 * Constructor for creating a {@code TalonSRX} motor controller for FRC.
	 * 
	 * @param deviceNumber
	 *                  The Device ID of the motor controller
	 */
	public WPI_TalonSRX(int deviceNumber)
	{
		super(deviceNumber);
	}
	
	/**
	 * Resets the Quadrature Encoder.
	 * 
	 * @return error code
	 */
	public ErrorCode resetEncoder()
	{
		return setEncoder(0);
	}
	
	/** Returns the Quadrature Encoder position. */
	public int getEncoder()
	{
		return invertEnc * getSensorCollection().getQuadraturePosition();
	}
	
	/** Returns the Analog Encoder position. Positions range from -512 to 512. */
	public long getAnalogEnc() {
		long pos = invertEnc * (getSensorCollection().getAnalogInRaw() - zeroPos - 512);

		while (pos < -512) {
			pos += 1024;
		}
		while (pos > 511) {
			pos -= 1024;
		}

		return pos;
	}
	
	/** Returns '1' iff forward limit switch is closed, 0 iff switch is open. This function works regardless if limit switch feature is enabled. */
	public boolean getLimitFwd()
	{
		return getSensorCollection().isFwdLimitSwitchClosed();
	}
	
	/** Returns '1' iff reverse limit switch is closed, 0 iff switch is open. This function works regardless if limit switch feature is enabled. */
	public boolean getLimitRev()
	{
		return getSensorCollection().isRevLimitSwitchClosed();
	}

	/**
	 * Sets the Quadrature Encoder to the given value
	 * 
	 * @param pos
	 *         New encoder value
	 * @return
	 *         Error Code
	 */
	public ErrorCode setEncoder(int pos)
	{
		return getSensorCollection().setQuadraturePosition(pos, 0);
	}

	/** Sets the current analog encoder position as zero. Encoder values will then range from -512 to 512. */
	public void setAnalogZero() {
		zeroPos = getSensorCollection().getAnalogInRaw() + 512;
	}
	
	/** Inverts the direction of the encoders.
	 * 
	 * @param invert
	 *            {@code true} if the encoder value should be multiplied by -1
	 */
	public void invertEncoder(boolean invert) {
		invertEnc = (invert) ? -1 : 1;
	}

	/** Sets up the motor controller to have a current limit of 40 and use a Quadrature Encoder. */
	public void setup()
	{
		set(0);
		configSelectedFeedbackSensor(FeedbackDevice.QuadEncoder, 0, 0);
		resetEncoder();
	}
}