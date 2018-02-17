package com.team1323.frc2018;

import com.team1323.lib.util.InterpolatingDouble;
import com.team1323.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.math.Translation2d;

public class Constants {
	public static final double kLooperDt = 0.02;
	
	public static final double kEpsilon = 0.0001;
	
	//Physical Robot Dimensions
	public static final double ROBOT_WIDTH = 39.0 / 12.0;
	public static final double ROBOT_LENGTH = 34.0 / 12.0;
	public static final double ROBOT_HALF_WIDTH = ROBOT_WIDTH / 2.0;
	public static final double ROBOT_HALF_LENGTH = ROBOT_LENGTH / 2.0;
	public static final double ROBOT_INTAKE_EXTRUSION = 11.0/12.0;
	
	public static final double kCubeWidth = 13.0/12.0;
	
	//Field Landmarks
	public static final Translation2d kAutoStartingCorner = new Translation2d(0.0, 12.5);
	public static final Translation2d kRightSwitchCloseCorner = new Translation2d(140.0 / 12.0, 27.0 - (85.25/12.0));
	public static final Translation2d kRightSwitchFarCorner = new Translation2d(196.0 / 12.0, 27.0 - (85.25/12.0));
	public static final Translation2d kLeftSwitchCloseCorner = new Translation2d(140.0 / 12.0, 85.25/12.0);
	public static final Translation2d kLeftSwitchFarCorner = new Translation2d(196.0 / 12.0, 85.25/12.0);
	public static final Translation2d kRightScaleCorner = new Translation2d(299.65 / 12.0, 27.0 - (95.25/12.0));
	public static final Translation2d kLeftScaleCorner = new Translation2d(299.65 / 12.0, 95.25 / 12.0);
	public static final Translation2d kRightMostCube = kRightSwitchFarCorner.translateBy(new Translation2d(kCubeWidth, 0.0));
	
	//Swerve Calculations Constants
    public static final double WHEELBASE_LENGTH = 18.5 / 12.0; //feet
    public static final double WHEELBASE_WIDTH  = 23.5 / 12.0; //feet
    public static final double SWERVE_DIAGONAL = Math.hypot(WHEELBASE_LENGTH, WHEELBASE_WIDTH);
    
    //Swerve Speed Constants
    public static final double SWERVE_ROTATION_MAX_SPEED = 1250.0;
    public static final double SWERVE_DRIVE_MAX_SPEED = 5432.0;
    
    //Swerve Module Wheel Offsets
	public static final int FRONT_RIGHT_ENCODER_STARTING_POS = 294;//done
	public static final int FRONT_LEFT_ENCODER_STARTING_POS = 301;//done
	public static final int REAR_LEFT_ENCODER_STARTING_POS = 2983;//done
	public static final int REAR_RIGHT_ENCODER_STARTING_POS = 3480;//done
	
	//Swerve Module Positions
	public static final Translation2d kVehicleToModuleOne = new Translation2d(WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleTwo = new Translation2d(WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleThree = new Translation2d(-WHEELBASE_LENGTH/2, -WHEELBASE_WIDTH/2);
	public static final Translation2d kVehicleToModuleFour = new Translation2d(-WHEELBASE_LENGTH/2, WHEELBASE_WIDTH/2);
	/*public static final Translation2d kVehicleToModuleOne = new Translation2d(0.0, 0.0);
	public static final Translation2d kVehicleToModuleTwo = new Translation2d(0.0, 0.0);
	public static final Translation2d kVehicleToModuleThree = new Translation2d(0.0, 0.0);
	public static final Translation2d kVehicleToModuleFour = new Translation2d(0.0, 0.0);*/
	
	//Scrub Factors
	//public static final double[] kWheelScrubFactors = new double[]{1.0, 0.9837398, 0.8832116788, 0.9837398};
	public static final double[] kWheelScrubFactors = new double[]{1.0, 1.0, 1.0, 1.0};
	
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
	public static final double ELEVATOR_SECOND_CUBE_HEIGHT = 0.73;
	public static final double ELEVATOR_HUMAN_LOAD_HEIGHT = 1.65;
	public static final double ELEVATOR_SWITCH_HEIGHT = 2.0; //feet
	public static final double ELEVATOR_BALANCED_SCALE_HEIGHT = 5.05; //feet
	public static final double ELEVATOR_HIGH_SCALE_HEIGHT = 5.3;
	public static final double ELEVATOR_LOW_SCALE_HEIGHT = 4.16;
	public static final double ELEVATOR_HANGING_HEIGHT = 4.8;
	public static final double ELEVATOR_MIN_HEIGHT = 0.0; //feet
	public static final double ELEVATOR_MAX_HEIGHT = 5.4; //feet
	public static final double ELEVATOR_MAX_CURRENT = 50.0;//amps
	
	//Swerve Speed Constraint Treemap
	public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kSwerveSpeedTreeMap = new InterpolatingTreeMap<>();
	static{
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(-0.1), new InterpolatingDouble(1.0));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(0.0), new InterpolatingDouble(1.0));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(ELEVATOR_INTAKING_HEIGHT), new InterpolatingDouble(1.0));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(ELEVATOR_MAX_HEIGHT), new InterpolatingDouble(0.5));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(ELEVATOR_MAX_HEIGHT + 0.2), new InterpolatingDouble(0.5));
	}
	
	//Wrist Constants
	public static final double WRIST_MAX_SPEED = 41.58 * 4096.0 / 600.0; //encoder units per 100 ms
	public static final double WRIST_STARTING_ANGLE = 90.0;
	/**
	 * Pulse width position of the wrist encoder when the wrist is upright (at 90 degrees).
	 */
	public static final int WRIST_STARTING_ENCODER_POSITION = 3639;
	/**
	 * The number of rotations the wrist encoder undergoes for every rotation of the wrist.
	 */
	public static final double WRIST_ENCODER_TO_OUTPUT_RATIO = 41.58 / 19.19;
	public static final double WRIST_ANGLE_TOLERANCE = 1.0; //degrees
	public static final double WRIST_MIN_ANGLE = -2.0; //degrees
	public static final double WRIST_MAX_ANGLE = 92.0; //degrees
	public static final double WRIST_INTAKING_ANGLE = 4.0;
	public static final double WRIST_PRIMARY_STOW_ANGLE = 80.0;
	public static final double WRIST_SECONDARY_STOW_ANGLE = 60.0;
	public static final double WRIST_MAX_STOW_HEIGHT = 3.5; //height of the elevator
	public static final double WRIST_MAX_CURRENT = 40.0;//amps
	
	public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kMaxWristAngleMap = new InterpolatingTreeMap<>();
	static{
		kMaxWristAngleMap.put(new InterpolatingDouble(-0.1), new InterpolatingDouble(90.0));
		kMaxWristAngleMap.put(new InterpolatingDouble(0.0), new InterpolatingDouble(90.0));
		kMaxWristAngleMap.put(new InterpolatingDouble(3.5), new InterpolatingDouble(90.0));
		kMaxWristAngleMap.put(new InterpolatingDouble(ELEVATOR_MAX_HEIGHT), new InterpolatingDouble(60.0));
		kMaxWristAngleMap.put(new InterpolatingDouble(ELEVATOR_MAX_HEIGHT + 0.2), new InterpolatingDouble(60.0));
	}
}
