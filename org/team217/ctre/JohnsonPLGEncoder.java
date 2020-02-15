package org.team217.ctre;

import edu.wpi.first.wpilibj.DigitalInput;

public class JohnsonPLGEncoder {
    private final DigitalInput hallSensor1, hallSensor2;
    private int encoder = 0;

    private boolean isLast1 = false, isLast2 = false;

    public JohnsonPLGEncoder(int hallChannel1, int hallChannel2) {
        hallSensor1 = new DigitalInput(hallChannel1);
        hallSensor2 = new DigitalInput(hallChannel2);
    }

    public boolean getSensor1Raw() {
        return hallSensor1.get();
    }

    public boolean getSensor2Raw() {
        return hallSensor2.get();
    }

    public int getEncoder() {
        return encoder;
    }

    public void update() {
        boolean isSensor2False = !isLast2 && !hallSensor2.get();
        if (isSensor2False && !isLast1 && hallSensor1.get()) {
            encoder++;
        }
        else if (isSensor2False && isLast1 && !hallSensor1.get()) {
            encoder--;
        }

        isLast1 = hallSensor1.get();
        isLast2 = hallSensor2.get();
    }
}