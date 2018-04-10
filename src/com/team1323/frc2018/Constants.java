package com.team1323.frc2018;

import java.util.Arrays;
import java.util.List;

import com.team1323.lib.util.InterpolatingDouble;
import com.team1323.lib.util.InterpolatingTreeMap;
import com.team254.lib.util.math.RigidTransform2d;
import com.team254.lib.util.math.Rotation2d;
import com.team254.lib.util.math.Translation2d;

public class Constants {
	public static final double kLooperDt = 0.02;
	
	public static final double kEpsilon = 0.0001;
	
	public static final boolean kIsUsingCompBot = false;
	
	//Physical Robot Dimensions
	public static final double kRobotWidth = 39.0 / 12.0;
	public static final double kRobotLength = 34.0 / 12.0;
	public static final double kRobotHalfWidth = kRobotWidth / 2.0;
	public static final double kRobotHalfLength = kRobotLength / 2.0;
	public static final double kRobotIntakeExtrusion = 11.0/12.0;
	
	public static final double kCubeWidth = 13.0/12.0;
	
	//Field Landmarks
	public static final Translation2d kAutoStartingCorner = new Translation2d(0.0, 12.5);
	public static final Translation2d kRightSwitchCloseCorner = new Translation2d(140.0 / 12.0, 27.0 - (85.25/12.0));
	public static final Translation2d kRightSwitchFarCorner = new Translation2d(196.0 / 12.0, 27.0 - (85.25/12.0));
	public static final Translation2d kLeftSwitchCloseCorner = new Translation2d(140.0 / 12.0, 85.25/12.0);
	public static final Translation2d kLeftSwitchFarCorner = new Translation2d(196.0 / 12.0, 85.25/12.0);
	public static final Translation2d kRightScaleCorner = new Translation2d(299.65 / 12.0, 27.0 - (95.25/12.0));
	public static final Translation2d kLeftScaleCorner = new Translation2d(299.65 / 12.0, 95.25 / 12.0);
	public static final Translation2d kRightMostCube = kRightSwitchFarCorner.translateBy(new Translation2d(kCubeWidth, -0.25));
	public static final Translation2d kLeftMostCube = kLeftSwitchFarCorner.translateBy(new Translation2d(kCubeWidth, kCubeWidth/2.0));
	public static final Translation2d kLeftMostCubeCorner = kLeftSwitchFarCorner.translateBy(new Translation2d(kCubeWidth, 0.0));
	public static final Translation2d kSecondLeftCube = kLeftMostCube.translateBy(new Translation2d(0.0, kCubeWidth + (15.1/12.0)));
	public static final Translation2d kSecondLeftCubeCorner = kSecondLeftCube.translateBy(new Translation2d(0.0, -kCubeWidth/2.0));
	
	public static final Translation2d kLeftSwitchTarget = new Translation2d(140.0 / 12.0, 13.5 - (51.875 / 12.0));
	public static final Translation2d kRightSwitchTarget = new Translation2d(140.0 / 12.0, 13.5 + (51.875 / 12.0));
	public static final double kTargetHeight = 18.75 / 2.0 / 12.0;
	
	public static final RigidTransform2d kRobotStartingPose = new RigidTransform2d(new Translation2d(Constants.kRobotHalfLength, Constants.kAutoStartingCorner.y() + Constants.kRobotHalfWidth), Rotation2d.fromDegrees(0));
	public static final RigidTransform2d kRobotLeftStartingPose = new RigidTransform2d(new Translation2d(Constants.kRobotHalfWidth, 5.5 - Constants.kRobotHalfLength), Rotation2d.fromDegrees(-90));
	
	//Swerve Calculations Constants
    public static final double kWheelbaseLength = 18.5 / 12.0; //feet
    public static final double kWheelbaseWidth  = 23.5 / 12.0; //feet
    public static final double kSwerveDiagonal = Math.hypot(kWheelbaseLength, kWheelbaseWidth);
    
    //Camera Constants
    public static final double kCameraYOffset = 14.438 / 12.0;
    public static final double kCameraXOffset = 3.563 / 12.0;
    public static final double kCameraZOffset = 24.866 / 12.0;
    public static final double kCameraYawAngleDegrees = 0.0;
    public static final double kCameraPitchAngleDegrees = -2.0;
    
 // Goal tracker constants
    public static double kMaxGoalTrackAge = 0.1;
    public static double kMaxTrackerDistance = 18.0;
    public static double kCameraFrameRate = 90.0;
    public static double kTrackReportComparatorStablityWeight = 1.0;
    public static double kTrackReportComparatorAgeWeight = 1.0;
    
    //Swerve Speed Constants
    public static final double kSwerveRotationMaxSpeed = 1250.0;
    public static final double kSwerveDriveMaxSpeed = 5432.0;
    
    //Swerve Module Wheel Offsets
	public static final int kFrontRightEncoderStartingPos = kIsUsingCompBot ? -1739 : 450;// p 450 c -1739
	public static final int kFrontLeftEncoderStartingPos = kIsUsingCompBot ? -888 : 854;// p 854 c -888
	public static final int kRearLeftEncoderStartingPos = kIsUsingCompBot ? -40 : -1477;// p -1477 c -40
	public static final int kRearRightEncoderStartingPos = kIsUsingCompBot ? -3503 : -3216;// p -3216 c -3503
	
	//Swerve Module Positions
	public static final Translation2d kVehicleToModuleZero = new Translation2d(kWheelbaseLength/2, kWheelbaseWidth/2);
	public static final Translation2d kVehicleToModuleOne = new Translation2d(kWheelbaseLength/2, -kWheelbaseWidth/2);
	public static final Translation2d kVehicleToModuleTwo = new Translation2d(-kWheelbaseLength/2, -kWheelbaseWidth/2);
	public static final Translation2d kVehicleToModuleThree = new Translation2d(-kWheelbaseLength/2, kWheelbaseWidth/2);
	
	public static final List<Translation2d> kModulePositions = Arrays.asList(kVehicleToModuleZero,
			kVehicleToModuleOne, kVehicleToModuleTwo, kVehicleToModuleThree);
	
	//Scrub Factors
	public static final double[] kWheelScrubFactors = new double[]{1.0, 1.0, 1.0, /*5.0/5.15*/1.0};
	
	//Swerve Odometry Constants
	public static final double kSwerveWheelDiameter = 3.93; //inches
	public static final double kSwerveDriveEncoderResolution = 4096.0;
	/**
	 * The number of rotations the swerve drive encoder undergoes for every rotation of the wheel.
	 */
	public static final double kSwerveEncoderToWheelRatio = 10.0/9.0;
	public static final double kSwerveEncUnitsPerWheelRev = kSwerveDriveEncoderResolution * kSwerveEncoderToWheelRatio;
	public static final double kSwerveEncUnitsPerInch = kSwerveEncUnitsPerWheelRev / (Math.PI * kSwerveWheelDiameter);
	
	//Elevator Constants
	public static final double kElevatorMaxSpeedHighGear = 363.388 * 4096.0 / 600.0; //encoder units per 100 ms
	public static final double kElevatorMaxSpeedLowGear = 148.0 * 4096.0 / 600.0; //encoder units per 100 ms
	/**
	 * Pulse width position of the elevator encoder when it has fully descended.
	 */
	public static final int kElevatorEncoderStartingPosition = 0;
	public static final double kElevatorTicksPerFoot = 11983.0 / 2.5989583; //determined empirically
	public static final double kElevatorHeightTolerance = 0.1; //feet
	public static final double kElevatorIntakingHeight = 0.125; //feet
	public static final double kElevatorSecondCubeHeight = 0.97;
	public static final double kElevatorHumanLoadHeight = 1.836;
	public static final double kElevatorSwitchHeight = 2.0; //feet
	public static final double kELevatorBalancedScaleHeight = 5.05; //feet
	public static final double kElevatorHighScaleHeight = 5.3;
	public static final double kElevatorLowScaleHeight = 4.3;
	public static final double kELevatorHangingHeight = 4.9;
	public static final double kElevatorMinHeight = 0.0; //feet
	public static final double kElevatorMaxHeight = 5.4; //feet
	public static final double kElevatorMaxCurrent = 50.0;//amps
	public static final int kELevatorCurrentLimit = 20;
	public static final double kElevatorMinimumHangingHeight = 0.70;
	public static final double kElevatorMaximumHangingHeight = 3.25;
	public static final double kElevatorHangingRampHeight = 3.452;
	public static final double kElevatorTippingCubeHeight = 0.57;
	public static final double kElevatorTeleopManualSpeed = 0.5;
	//0.905
	
	//Swerve Speed Constraint Treemap
	public static InterpolatingTreeMap<InterpolatingDouble, InterpolatingDouble> kSwerveSpeedTreeMap = new InterpolatingTreeMap<>();
	static{
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(-0.1), new InterpolatingDouble(1.0));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(0.0), new InterpolatingDouble(1.0));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(kElevatorIntakingHeight), new InterpolatingDouble(1.0));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(kElevatorMaxHeight), new InterpolatingDouble(0.5));
		kSwerveSpeedTreeMap.put(new InterpolatingDouble(kElevatorMaxHeight + 0.2), new InterpolatingDouble(0.5));
	}
	
	//Wrist Constants
	public static final double kWristMaxSpeed = /*41.58 * 4096.0 / 600.0*/300.0; //encoder units per 100 ms
	public static final double kWristStartingAngle = 90.0;
	/**
	 * Pulse width position of the wrist encoder when the wrist is upright (at 90 degrees, parallel to the elevator).
	 */
	public static final int kWristStartingEncoderPosition = kIsUsingCompBot ? 533 : 2289; //p 2289 c 515
	/**
	 * The number of rotations the wrist encoder undergoes for every rotation of the wrist.
	 */
	public static final double kWristEncoderToOutputRatio = 41.58 / 19.19;
	public static final double kWristAngleTolerance = 10.0; //degrees
	public static final double kWristMinControlAngle = -2.0; //degrees
	public static final double kWristMaxControlAngle = 92.0; //degrees
	public static final double kWristMinPhysicalAngle = -20.0;
	public static final double kWristMaxPhysicalAngle = 110.0;//95.192
	public static final double kWristIntakingAngle = kIsUsingCompBot ? 5.5 : 9.0;//p 5.7 c 1.78
	public static final double kWristPrimaryStowAngle = 85.0;
	public static final double kWristSecondaryStowAngle = 60.0;
	public static final double kWristMaxStowHeight = 3.5; //height of the elevator
	public static final double kWristMaxCurrent = 40.0;//amps
	
	//Intake Constants
	public static final double kIntakeWeakEjectOutput = -0.4;
	public static final double kIntakeEjectOutput = -0.55;
	public static final double kIntakeStrongEjectOutput = -1.0;
}
