package org.team217.wpi;

import edu.wpi.first.wpilibj.AnalogInput;

public class AnalogGyro extends edu.wpi.first.wpilibj.AnalogGyro {
    protected int offset = 0;

    public AnalogGyro(int channel) {
        super(channel);
    }

    public AnalogGyro(AnalogInput channel) {
        super(channel);
    }

    @Override
    public double getAngle() {
        return getAngle() + offset;
    }

    public void set(int offset) {
        reset();
        this.offset = offset;
    }
}