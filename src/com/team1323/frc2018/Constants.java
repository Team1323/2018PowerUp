package com.team1323.frc2018;

import com.team254.lib.util.math.Translation2d;

public class Constants {
	public static final double kLooperDt = 0.005;
	
	public static final double kEpsilon = 0.0001;
	
	//Swerve Calculations Constants
    public static final double WHEELBASE_LENGTH = 18.5 / 12.0; //feet
    public static final double WHEELBASE_WIDTH  = 23.5 / 12.0; //feet
    public static final double SWERVE_DIAGONAL = Math.hypot(WHEELBASE_LENGTH, WHEELBASE_WIDTH);
    
    //Swerve Speed Constants
    public static final double SWERVE_ROTATION_MAX_SPEED = 1250.0;
    
    //Swerve Module Wheel Offsets
	public static final int FRONT_RIGHT_ENCODER_STARTING_POS = 132;//done
	public static final int FRONT_LEFT_ENCODER_STARTING_POS = 250;//done
	public static final int REAR_LEFT_ENCODER_STARTING_POS = 2983;//done
	public static final int REAR_RIGHT_ENCODER_STARTING_POS = 3480;//done
	
	//Swerve Module Positions
	public static final Translation2d kVehicleToModuleOne = new Translation2d(WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleTwo = new Translation2d(WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleThree = new Translation2d(-WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleFour = new Translation2d(-WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2);
	
	//Swerve Odometry Constants
	public static final double SWERVE_WHEEL_DIAMETER = 4.0; //inches
	public static final double DRIVE_ENCODER_RESOLUTION = 4096.0;
	/**
	 * The number of rotations the swerve drive encoder undergoes for every rotation of the wheel.
	 */
	public static final double SWERVE_ENCODER_TO_WHEEL_RATIO = 10.0/9.0;
	public static final double SWERVE_ENC_UNITS_PER_WHEEL_REV = DRIVE_ENCODER_RESOLUTION * SWERVE_ENCODER_TO_WHEEL_RATIO;
	public static final double SWERVE_ENC_UNITS_PER_INCH = SWERVE_ENC_UNITS_PER_WHEEL_REV / (Math.PI * SWERVE_WHEEL_DIAMETER);
	
	//Elevator Constants
	public static final double ELEVATOR_MAX_SPEED_HIGH_GEAR = 363.388 * 4096.0 / 600.0; //encoder units per 100 ms
	public static final double ELEVATOR_MAX_SPEED_LOW_GEAR = 148.0 * 4096.0 / 600.0; //encoder units per 100 ms
	/**
	 * Pulse width position of the elevator encoder when it has fully descended.
	 */
	public static final int ELEVATOR_ENCODER_STARTING_POSITION = 0;
	public static final double ELEVATOR_TICKS_PER_FOOT = 11983.0 / 2.5989583; //determined empirically
	public static final double ELEVATOR_HEIGHT_TOLERANCE = 0.1; //feet
	public static final double ELEVATOR_INTAKING_HEIGHT = 0.125; //feet
	public static final double ELEVATOR_SWITCH_HEIGHT = 2.0; //feet
	public static final double ELEVATOR_SCALE_HEIGHT = 5.0; //feet
	public static final double ELEVATOR_MAX_CURRENT = 40.0;//amps
	
	//Wrist Constants
	public static final double WRIST_MAX_SPEED = 41.58 * 4096.0 / 600.0; //encoder units per 100 ms
	public static final double WRIST_STARTING_ANGLE = 90.0;
	/**
	 * Pulse width position of the wrist encoder when the wrist is upright (at 90 degrees).
	 */
	public static final int WRIST_STARTING_ENCODER_POSITION = 2553;
	/**
	 * The number of rotations the wrist encoder undergoes for every rotation of the wrist.
	 */
	public static final double WRIST_ENCODER_TO_OUTPUT_RATIO = 41.58 / 19.19;
	public static final double WRIST_ANGLE_TOLERANCE = 1.0; //degrees
	public static final double WRIST_MAX_CURRENT = 20.0;//amps
}
