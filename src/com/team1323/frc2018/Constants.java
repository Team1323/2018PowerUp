package com.team1323.frc2018;

import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

public class Constants {
	public static double kLooperDt = 0.01;
	
	//Swerve Calculations Constants
    public static final double WHEELBASE_LENGTH = 18.5;
    public static final double WHEELBASE_WIDTH  = 23.5;
    public static final double SWERVE_DIAGONAL = Math.hypot(WHEELBASE_LENGTH, WHEELBASE_WIDTH);
    
    //Swerve Module Wheel Offsets
 	public static final double FRONT_RIGHT_TURN_OFFSET = 0.0;
	public static final double FRONT_LEFT_TURN_OFFSET  = 0.0;
	public static final double REAR_LEFT_TURN_OFFSET   = 0.0;
	public static final double REAR_RIGHT_TURN_OFFSET  = 0.0;
	
	//Swerve Module Positions
	public static final RigidTransform2d kVehicleToModuleOne = new RigidTransform2d(
			new Translation2d(WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2), new Rotation2d());
	public static final RigidTransform2d kVehicleToModuleTwo = new RigidTransform2d(
			new Translation2d(WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2), new Rotation2d());
	public static final RigidTransform2d kVehicleToModuleThree = new RigidTransform2d(
			new Translation2d(-WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2), new Rotation2d());
	public static final RigidTransform2d kVehicleToModuleFour = new RigidTransform2d(
			new Translation2d(-WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2), new Rotation2d());
	
	//Swerve Odometry Constants
	public static final double SWERVE_WHEEL_DIAMETER = 4.0;
	public static final double DRIVE_ENCODER_RESOLUTION = 4096.0;
	public static final double SWERVE_ENCODER_TO_WHEEL_RATIO = 10.0/9.0;
	public static final double SWERVE_ENC_UNITS_PER_WHEEL_REV = DRIVE_ENCODER_RESOLUTION * SWERVE_ENCODER_TO_WHEEL_RATIO;
	public static final double SWERVE_ENC_UNITS_PER_INCH = SWERVE_ENC_UNITS_PER_WHEEL_REV / (Math.PI * SWERVE_WHEEL_DIAMETER);
}
