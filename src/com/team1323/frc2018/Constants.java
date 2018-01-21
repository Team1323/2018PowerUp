package com.team1323.frc2018;

import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

public class Constants {
	public static double kLooperDt = 0.01;
	
	public static final double kEpsilon = 0.0001;
	
	//Swerve Calculations Constants
    public static final double WHEELBASE_LENGTH = 22.181;//18.5;
    public static final double WHEELBASE_WIDTH  = 15.681;//23.5;
    public static final double SWERVE_DIAGONAL = Math.hypot(WHEELBASE_LENGTH, WHEELBASE_WIDTH);
    
    //Swerve Module Wheel Offsets
 	public static final Rotation2d FRONT_RIGHT_TURN_OFFSET = Rotation2d.fromDegrees(55.19);
	public static final Rotation2d FRONT_LEFT_TURN_OFFSET  = Rotation2d.fromDegrees(93.0);
	public static final Rotation2d REAR_LEFT_TURN_OFFSET   = Rotation2d.fromDegrees(278.2);
	public static final Rotation2d REAR_RIGHT_TURN_OFFSET  = Rotation2d.fromDegrees(358.94);
	
	//Swerve Module Positions
	public static final Translation2d kVehicleToModuleOne = new Translation2d(WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleTwo = new Translation2d(WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleThree = new Translation2d(-WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleFour = new Translation2d(-WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2);
	
	//Swerve Odometry Constants
	public static final double SWERVE_WHEEL_DIAMETER = 2.8;//4.0;
	public static final double DRIVE_ENCODER_RESOLUTION = 1440.0;//4096.0;
	public static final double SWERVE_ENCODER_TO_WHEEL_RATIO = 4.307692308;//10.0/9.0;
	public static final double SWERVE_ENC_UNITS_PER_WHEEL_REV = DRIVE_ENCODER_RESOLUTION * SWERVE_ENCODER_TO_WHEEL_RATIO;
	public static final double SWERVE_ENC_UNITS_PER_INCH = SWERVE_ENC_UNITS_PER_WHEEL_REV / (Math.PI * SWERVE_WHEEL_DIAMETER);
}
