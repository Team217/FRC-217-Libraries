package org.team217.rev;

public class CANSparkMax extends com.revrobotics.CANSparkMax {

    protected double resetPosition = 0.0;
    protected double invertEnc = 1.0;

    public CANSparkMax(int deviceNumber, MotorType type) {
        super(deviceNumber, type);
    }

    /** Creates a brushless CANSparkMax */
    public CANSparkMax(int deviceNumber) {
        super(deviceNumber, MotorType.kBrushless);
    }
	
	/** Inverts the direction of the encoders.
	 * 
	 * @param isInverted
	 *            {@code true} if the encoder value should be multiplied by -1
	 */
	public void invertEncoder(boolean isInverted) {
		invertEnc = (isInverted) ? -1.0 : 1.0;
    }
    
    /** Gets the encoder velocity, modified so {@code invertEncoder()}, {@code setEncoder()}, and {@code resetEncoder()} affect the return value. */
    public double getVelocity() {
        return invertEnc * getEncoder().getVelocity();
    }

    /** Gets the encoder position, modified so {@code invertEncoder()}, {@code setEncoder()}, and {@code resetEncoder()} affect the return value. */
    public double getPosition() {
        return invertEnc * (getEncoder().getPosition() - resetPosition);
    }

    /** Sets the encoder value to the given position. This does not save to the motor controller and must be called each time code is deployed. */
    public void setEncoder(double position) {
        resetPosition = getEncoder().getPosition() - invertEnc * position;
    }

    /** Resets the encoder value to 0. This does not save to the motor controller and must be called each time code is deployed. */
    public void resetEncoder() {
        resetPosition = getEncoder().getPosition();
    }
}