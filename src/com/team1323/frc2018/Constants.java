package com.team1323.frc2018;

public class Constants {
	public static double kLooperDt = 0.01;
	
	//Swerve Calculations Constants
    public static final double WHEELBASE_LENGTH = 22.181;
    public static final double WHEELBASE_WIDTH  = 15.681;
    public static final double SWERVE_R = Math.hypot(WHEELBASE_LENGTH, WHEELBASE_WIDTH);
    
 // Swerve Module Wheel Offsets
 	public static final double FRONT_RIGHT_TURN_OFFSET = 0.0;
     public static final double FRONT_LEFT_TURN_OFFSET  = 0.0;
     public static final double REAR_LEFT_TURN_OFFSET   = 0.0;
     public static final double REAR_RIGHT_TURN_OFFSET  = 0.0;
}
