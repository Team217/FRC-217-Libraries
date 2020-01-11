package org.team217.pid;

/**
 * A class that runs and controls PID systems using
 * integral and derivative times (Ti and Td) instead of kI and kD.
 * 
 * @author ThunderChickens 217
 * @deprecated Use WPILib's Feedforward and PID
 */
@Deprecated
public class TPID extends PID {
    public TPID(double Kc, double Ti, double Td) {
        super(Kc, (Ti == 0 ? 0 : Kc / Ti), Kc * Td);
    }

    public TPID(double Kc, double Ti, double Td, int timeout) {
        super(Kc, (Ti == 0 ? 0 : Kc / Ti), Kc * Td, timeout);
    }
}